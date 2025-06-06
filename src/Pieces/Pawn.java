package Pieces;

import Logic.BoardLogic;
import Logic.Move;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Pawn extends Piece
{
    public Pawn(String name, boolean isWhite, int xPos, int yPos, int row, int col) throws IOException
    {
        super(name, isWhite, xPos, yPos, row, col);
        image = getImage(name,isWhite) ;
        this.hasMoved = false ;
    }
    public BufferedImage getImage()
    {
        return image ;
    }

    @Override
    public boolean isValidMove(Move move, BoardLogic board)
    {
        ArrayList<Move> validMoves = validMoves(board) ;
        return validMoves.contains(move) ;
    }

    @Override
    public boolean attackedSquare(int row, int col, BoardLogic board)
    {
        int direction = (this.isWhite) ? -1 : 1;
        return (row == this.row + direction) && (Math.abs(col - this.col) == 1);
    }

    public ArrayList<Move> validMoves (BoardLogic board)
    {
        ArrayList<Move> moves = new ArrayList<>() ;
        int direction = (this.isWhite) ? -1 : 1;
        if (!hasMoved && board.arrBoard[row+2*direction][col] == null && board.arrBoard[row+direction][col] == null)
        {
            moves.add(new Move(board,this,row+2*direction,col)) ;
        }
        if (isInBounds(row + direction, col) && board.arrBoard[row+direction][col] == null)
        {
            moves.add(new Move(board,this,row+direction,col)) ;
        }
        for (int diag : new int [] {-1,1})
        {
            int nRow = this.row+direction ;
            int nCol = this.col+diag ;
            if (isInBounds(nRow,nCol) )
            {
                if (board.arrBoard[nRow][nCol] != null && !board.sameSide(this,board.arrBoard[nRow][nCol]))
                {
                    moves.add(new Move(board,this,nRow,nCol)) ;
                }
            }
        }
        // En Passant
        if ( board.lastMove != null && board.lastMove.piece instanceof Pawn )
        {
            int pawnRow = board.lastMove.finRow ;
            int pawnCol = board.lastMove.piece.col ;
            int rowDiff = pawnRow - board.lastMove.iniRow ;
            if ( rowDiff == -2*direction &&  pawnRow == this.row )
            {
                if (Math.abs(pawnCol-this.col) == 1)
                {
                    moves.add(new Move(board,this,row+direction,pawnCol,true)) ;
                }
            }
        }
        return moves ;
    }

    private boolean isInBounds(int r, int c)
    {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }

    public void promote(String piece,BoardLogic boardLogic) throws IOException
    {
        if (!boardLogic.engineTurn && boardLogic.engineWhite != boardLogic.whiteTurn)
        {
            switch (piece)
            {
                case "queen" :
                    Queen queen = new Queen("queen",this.isWhite,this.xPos,this.yPos,this.row,this.col) ;
                    boardLogic.arrBoard[this.row][this.col] = queen ;
                    break;
                case "rook" :
                    Rook rook = new Rook("rook",this.isWhite,this.xPos,this.yPos,this.row,this.col) ;
                    boardLogic.arrBoard[this.row][this.col] = rook ;
                    break;
                case "bishop" :
                    Bishop bishop = new Bishop("bishop",this.isWhite,this.xPos,this.yPos,this.row,this.col) ;
                    boardLogic.arrBoard[this.row][this.col] = bishop ;
                    break;
                case "knight" :
                    Knight knight = new Knight("knight",this.isWhite,this.xPos,this.yPos,this.row,this.col) ;
                    boardLogic.arrBoard[this.row][this.col] = knight ;
                    break;
            }
        }
        else
        {
            char pieceeng = boardLogic.engineProm ;
            switch (pieceeng)
            {
                case 'q' :
                    Queen queen = new Queen("queen",this.isWhite,this.xPos,this.yPos,this.row,this.col) ;
                    boardLogic.arrBoard[this.row][this.col] = queen ;
                    break;
                case 'r' :
                    Rook rook = new Rook("rook",this.isWhite,this.xPos,this.yPos,this.row,this.col) ;
                    boardLogic.arrBoard[this.row][this.col] = rook ;
                    break;
                case 'b' :
                    Bishop bishop = new Bishop("bishop",this.isWhite,this.xPos,this.yPos,this.row,this.col) ;
                    boardLogic.arrBoard[this.row][this.col] = bishop ;
                    break;
                case 'k' :
                    Knight knight = new Knight("knight",this.isWhite,this.xPos,this.yPos,this.row,this.col) ;
                    boardLogic.arrBoard[this.row][this.col] = knight ;
                    break;
            }

        }

    }



}
