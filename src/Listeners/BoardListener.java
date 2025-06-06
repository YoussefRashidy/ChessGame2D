package Listeners;

import GUI.ChessBoard;
import Logic.BoardLogic;
import Logic.Move;
import Pieces.Piece;
import Pieces.Rook;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardListener extends MouseAdapter
{
    public BoardLogic boardLogic ;
    public ChessBoard board ;
    public BoardListener (BoardLogic board , ChessBoard chess)
    {
        boardLogic = board ;
        this.board = chess ;
    }
    @Override
    public void mousePressed(MouseEvent e)
    {
        if (!boardLogic.engineTurn)
        {
            int row = e.getY()/Piece.square ;
            int col = e.getX()/Piece.square ;
            if (row < 0 || row >= 8 || col < 0 || col >= 8)
            {
                return;
            }
            if (boardLogic.flipBoard)
            {
                col = 7 - col;
                row = 7 - row;
            }
            Piece piece = boardLogic.getPiece(row,col);

            if (piece != null)
            {
                if (piece.isWhite == boardLogic.whiteTurn)
                {
                    boardLogic.selectedPiece = piece ;
                }
                else
                {
                    return;
                }
            }
        }

    }
    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (boardLogic.selectedPiece != null && !boardLogic.engineTurn)
        {
            boardLogic.selectedPiece.xPos = e.getX() - Piece.square/2 ;
            boardLogic.selectedPiece.yPos = e.getY() - Piece.square/2 ;
            board.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        int x = e.getX() ;
        int y = e.getY() ;
        int col = x/Piece.square;
        int row = y/Piece.square;
        if (boardLogic.flipBoard)
        {
            col = 7 - col;
            row = 7 - row;
        }
        if (!boardLogic.engineTurn && boardLogic.selectedPiece != null)
        {
            boolean isEnpassant = Move.isEnPassantAttempt(boardLogic, boardLogic.selectedPiece, row, col);
            Move move = isEnpassant
                    ? new Move(boardLogic, boardLogic.selectedPiece, row, col, true)
                    : new Move(boardLogic, boardLogic.selectedPiece, row, col);
            move.castlingAttempt(boardLogic);
            if (boardLogic.isValid(move))
            {
                if (move.castlingAttempt)
                {
                    if (move.finCol == 6)
                    {
                        Piece rook ;
                        if (boardLogic.selectedPiece.isWhite)
                        {
                           rook = boardLogic.getPiece(7,7) ;
                            rook.row = 7 ;
                            rook.col = 5 ;
                            boardLogic.arrBoard[7][5] = rook ;
                            boardLogic.arrBoard[7][7] = null ;
                        }
                        else
                        {
                            rook = boardLogic.getPiece(0,7) ;
                            rook.row = 0 ;
                            rook.col = 5 ;
                            boardLogic.arrBoard[0][5] = rook ;
                            boardLogic.arrBoard[0][7] = null ;
                        }
                    }
                    else if (move.finCol == 2)
                    {
                        Piece rook ;
                        if (boardLogic.selectedPiece.isWhite)
                        {
                            rook = boardLogic.getPiece(7,0) ;
                            rook.row = 7 ;
                            rook.col = 3 ;
                            boardLogic.arrBoard[7][3] = rook ;
                            boardLogic.arrBoard[7][0] = null ;
                        }
                        else
                        {
                            rook = boardLogic.getPiece(0,0) ;
                            rook.row = 0 ;
                            rook.col = 3 ;
                            boardLogic.arrBoard[0][3] = rook ;
                            boardLogic.arrBoard[0][0] = null ;
                        }
                    }
                }
                boardLogic.movePiece(move);
            }
            else
            {
                boardLogic.selectedPiece.xPos = boardLogic.selectedPiece.col * Piece.square ;
                boardLogic.selectedPiece.yPos = boardLogic.selectedPiece.row * Piece.square ;
            }
            if (!boardLogic.againstHuman)
            {
                boardLogic.engineTurn = true ;
            }
            boardLogic.selectedPiece = null ;
            board.repaint( );
            if (boardLogic.listMove)
                SwingUtilities.invokeLater(()-> boardLogic.list.addMove(move.toText(),move.piece.isWhite));
            boardLogic.listMove = false ;
        }
        if (boardLogic.engineTurn)
        {
            new Thread(()-> {
                try {
                    boardLogic.engineTurn = false ;
                    boardLogic.engineMove();
                    board.repaint();
                    SwingUtilities.invokeLater(()-> boardLogic.list.addMove(boardLogic.lastMove.toText(),boardLogic.lastMove.piece.isWhite));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }).start();
        }
    }

}
