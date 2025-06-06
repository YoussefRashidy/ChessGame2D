package Logic;

import GUI.ChessBoard;
import GUI.EndGame;
import GUI.MoveList;
import MainGame.Game;
import Pieces.*;
import Sound.Sounds;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
public class BoardLogic
{
    public Piece[][]arrBoard ;
    public static final int squareSize = 80 ;
    public Piece selectedPiece ;
    public boolean whiteTurn ;
    public Move lastMove ;
    public King whiteKing ;
    public King blackKing ;
    public int fullMove ;
    public Position position ;
    public boolean flipBoard ;
    public boolean againstHuman;
    public boolean engineTurn ;
    public Sounds sounds ;
    public Engine engine ;
    public boolean engineWhite ;
    public char engineProm ;
    public MoveList list ;
    public boolean listMove ;
    public BoardLogic(MoveList list)
    {
        arrBoard = new Piece[8][8];
        lastMove = null ;
        fullMove = 1 ;
        position = new Position(this) ;
        flipBoard = false ;
        engine = new Engine() ;
        engineTurn = false ;
        listMove = false ;
        this.list = list ;
        try
        {
            sounds = new Sounds() ;
        }
        catch (UnsupportedAudioFileException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (LineUnavailableException e)
        {
            throw new RuntimeException(e);
        }
        if (!againstHuman)
        {
            engine.startEngine("src/stockfish/stockfish-windows-x86-64-avx2.exe");
        }
    }
    public void initializeBoard()
    {
        try {
            // Place pawns
            for (int col = 0; col < 8; col++)
            {
                int xPos = col * squareSize;
                arrBoard[1][col] = new Pawn("pawn", false, xPos, squareSize, 1, col);
                arrBoard[6][col] = new Pawn("pawn", true, xPos, 6 * squareSize, 6, col);
            }

            // Place Rooks
            arrBoard[0][0] = new Rook("rook", false, 0 * squareSize, 0, 0, 0);
            arrBoard[0][7] = new Rook("rook", false, 7 * squareSize, 0, 0, 7);
            arrBoard[7][0] = new Rook("rook", true, 0 * squareSize, 7 * squareSize, 7, 0);
            arrBoard[7][7] = new Rook("rook", true, 7 * squareSize, 7 * squareSize, 7, 7);

            // Place Knights
            arrBoard[0][1] = new Knight("knight", false, 1 * squareSize, 0, 0, 1);
            arrBoard[0][6] = new Knight("knight", false, 6 * squareSize, 0, 0, 6);
            arrBoard[7][1] = new Knight("knight", true, 1 * squareSize, 7 * squareSize, 7, 1);
            arrBoard[7][6] = new Knight("knight", true, 6 * squareSize, 7 * squareSize, 7, 6);

            // Place Bishops
            arrBoard[0][2] = new Bishop("bishop", false, 2 * squareSize, 0, 0, 2);
            arrBoard[0][5] = new Bishop("bishop", false, 5 * squareSize, 0, 0, 5);
            arrBoard[7][2] = new Bishop("bishop", true, 2 * squareSize, 7 * squareSize, 7, 2);
            arrBoard[7][5] = new Bishop("bishop", true, 5 * squareSize, 7 * squareSize, 7, 5);

            // Place Queens
            arrBoard[0][3] = new Queen("queen", false, 3 * squareSize, 0, 0, 3);
            arrBoard[7][3] = new Queen("queen", true, 3 * squareSize, 7 * squareSize, 7, 3);

            // Place Kings
            arrBoard[0][4] = new King("king", false, 4 * squareSize, 0, 0, 4);
            arrBoard[7][4] = new King("king", true, 4 * squareSize, 7 * squareSize, 7, 4);
            whiteKing = (King) arrBoard[7][4] ;
            blackKing = (King) arrBoard[0][4] ;
            whiteTurn = true ;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    public Piece getPiece(int row , int col)
    {
        return arrBoard[row][col] ;
    }
    public void movePiece(Move move)
    {
            if (move == null || move.piece == null)
            {
                return;
            }
            arrBoard[move.finRow][move.finCol] = move.piece;
            arrBoard[move.iniRow][move.iniCOl] = null;
            move.piece.row = move.finRow;
            move.piece.col = move.finCol;
            move.piece.yPos = move.finRow * squareSize;
            move.piece.xPos = move.finCol * squareSize;
            move.piece.hasMoved = true ;
            if (move.isEnpassant)
            {
                arrBoard[move.capture.row][move.capture.col] = null;
            }
            if (move.piece instanceof Pawn && ( move.finRow == 0 || move.finRow == 7 ) )
            {
                if ( !engineTurn && engineWhite != whiteTurn)
                {
                    String promotion = ChessBoard.promotionMenu(move.piece.isWhite) ;
                    try
                    {
                        ((Pawn) move.piece).promote(promotion,this);
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
                else
                {
                    try
                    {
                        ((Pawn) move.piece).promote("",this);
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                }

            }
            if (!whiteTurn)
            {
                fullMove++ ;
            }
            if (move.capture != null)
            {
                sounds.playCaptureSound();
            }
            else
            {
                sounds.playMoveSound();
            }
            position.updateCapture(move);
            lastMove = move ;
            whiteTurn = !whiteTurn ;
            position.toFen();
            listMove = true ;
            if (againstHuman)
            {
                flipBoard();
            }
        if (isMate(!move.piece.isWhite))
        {
            SwingUtilities.invokeLater(() ->
            {
                String winner = move.piece.isWhite ? "White" : "Black" ;
                String pieceUnicode = move.piece.isWhite ? whiteKing.getUnicode() : blackKing.getUnicode() ;
                String text = "" + "CheckMate "+winner+" wins "+pieceUnicode ;
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ChessBoard.game);
                new EndGame(frame, "MainGame.Game Over", text, move.piece.isWhite,
                        e -> Game.resetGame(),
                        e -> System.exit(0)
                ).setVisible(true);
            });
        }
        else if (staleMate(!move.piece.isWhite))
        {
            SwingUtilities.invokeLater(() ->
            {
                String text = "" + "StaleMate "+" Draw" ;
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ChessBoard.game);
                new EndGame(frame, "MainGame.Game Over", text, move.piece.isWhite,
                        e -> Game.resetGame(),
                        e -> System.exit(0)
                ).setVisible(true);
            });
        }
        else if (insufficientM())
        {
            SwingUtilities.invokeLater(() ->
            {
                String text = "" + "Insufficient Material "+" Draw" ;
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ChessBoard.game);
                new EndGame(frame, "MainGame.Game Over", text, move.piece.isWhite,
                        e -> Game.resetGame(),
                        e -> System.exit(0)
                ).setVisible(true);
            });
        }
        else if (position.fiftyMove())
        {
            SwingUtilities.invokeLater(() ->
            {
                String text = "" + "50 move "+" Draw" ;
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ChessBoard.game);
                new EndGame(frame, "MainGame.Game Over", text, move.piece.isWhite,
                        e -> Game.resetGame(),
                        e -> System.exit(0)
                ).setVisible(true);
            });
        }
        else if (position.threeFold())
        {
            SwingUtilities.invokeLater(() ->
            {
                String text = "" + "3 Times Repetition "+" Draw" ;
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ChessBoard.game);
                new EndGame(frame, "MainGame.Game Over", text, move.piece.isWhite,
                        e -> Game.resetGame(),
                        e -> System.exit(0)
                ).setVisible(true);
            });
        }

    }
    public  boolean sameSide(Piece piece1, Piece piece2)
    {
        if (piece1 == null || piece2 == null)
        {
            return false ;
        }
        return piece1.isWhite  == piece2.isWhite  ;
    }

    public boolean isValid(Move move)
    {
        if (move.piece.isValidMove(move,this))
        {
            simulateMove(move);
            if (isCheck(move.piece.isWhite))
            {
                undoMove(move);
                return false ;
            }
            else
            {
                undoMove(move);
                return true ;
            }
        };
        return false ;
    }

    public boolean canCastleShort(boolean isWhite)
    {
        int row = isWhite ? 7 : 0;

        Piece king = getPiece(row, 4);
        Piece rook = getPiece(row, 7);

        if (!(king instanceof King) || king.hasMoved) return false;
        if (!(rook instanceof Rook) || rook.hasMoved) return false;


        if (getPiece(row, 5) != null || getPiece(row, 6) != null) return false;
        if (((King) king).isAttacked(row, 4,this) || ((King) king).isAttacked(row, 5, this) || ((King) king).isAttacked(row, 6,this)) return false;

        return true;
    }
    public boolean canCastleLong(boolean isWhite)
    {
        int row = isWhite ? 7 : 0;

        Piece king = getPiece(row, 4);
        Piece rook = getPiece(row, 0);

        if (!(king instanceof King) || king.hasMoved) return false;
        if (!(rook instanceof Rook) || rook.hasMoved) return false;

        if (getPiece(row, 1) != null || getPiece(row, 2) != null || getPiece(row, 3) != null) return false;
        if (((King) king).isAttacked(row, 4,this) || ((King) king).isAttacked(row, 3,this) || ((King) king).isAttacked(row, 2,this)) return false;

        return true;
    }
    public boolean isCheck(boolean isWhite)
    {
        King king = isWhite ? whiteKing : blackKing ;
        return king.isAttacked(king.row,king.col,this) ;
    }
    public void simulateMove(Move move)
    {
        if (move.piece instanceof Pawn && move.isEnpassant)
        {
            this.arrBoard[move.capture.row][move.capture.col] = null;
        }
        arrBoard[move.finRow][move.finCol] = move.piece ;
        arrBoard[move.iniRow][move.iniCOl] = null ;
        move.piece.row = move.finRow ;
        move.piece.col = move.finCol ;
    }
    public void undoMove(Move move)
    {
        arrBoard[move.iniRow][move.iniCOl] = move.piece ;
        move.piece.row = move.iniRow ;
        move.piece.col = move.iniCOl ;
        if (move.capture != null)
        {
            arrBoard[move.capture.row][move.capture.col] = move.capture ;
        }
        else
        {
            arrBoard[move.finRow][move.finCol] = null;
        }
    }

    public boolean isMate(boolean isWhite)
    {
        if (!isCheck(isWhite))
        {
            return false ;
        }
        for (Piece[] piece : arrBoard)
        {
            for (Piece piece1 : piece)
            {
                if (piece1 == null || piece1.isWhite != isWhite)
                {
                    continue;
                }
                for (Move move : piece1.validMoves(this))
                {
                    simulateMove(move);
                    if (isCheck(isWhite))
                    {
                        undoMove(move) ;
                    }
                    else
                    {
                        undoMove(move);
                        return false ;
                    }
                }
            }
        }
        return true ;
    }

    public boolean staleMate(boolean isWhite)
    {
        if (isCheck(isWhite))
        {
            return false;
        }
        for (Piece[] row : arrBoard)
        {
            for (Piece piece : row)
            {
                if (piece == null || piece.isWhite != isWhite)
                    continue;

                for (Move move : piece.validMoves(this))
                {
                    simulateMove(move);
                    boolean kingInCheck = isCheck(isWhite);
                    undoMove(move);

                    if (!kingInCheck)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void setGameMode(String mode)
    {
        againstHuman = mode.equals("HUMAN");
    }
    public void flipBoard()
    {
        flipBoard =! flipBoard ;
    }

    public boolean insufficientM()
    {
        if (position.capturedW.length() == 15 && position.capturedB.length() == 15)
        {
            return true ;
        }
        if (position.capturedW.length() <= 13 || position.capturedB.length() <= 13 )
        {
            return false ;
        }
        else
        {
            int pawns = 16 - position.charCount('p',position.capturedB) + position.charCount('P', position.capturedW) ;
            int queens =  2 - position.charCount('q',position.capturedB) + position.charCount('Q', position.capturedW) ;
            int rooks =  4- position.charCount('r',position.capturedB) + position.charCount('R', position.capturedW) ;
            return !( pawns > 0 || queens > 0 || rooks > 0 ) ;
        }
    }

    public Move toMove(String string)
    {
        if (string != null)
        {
            int intRow,intCol,finRow,finCol ;
            char promotion ;
            intCol = string.charAt(0) - 'a' ;
            intRow = 8 - Character.getNumericValue(string.charAt(1))  ;
            finCol = string.charAt(2) - 'a' ;
            finRow =  8 - Character.getNumericValue(string.charAt(3)) ;
            if (string.length() == 5)
            {
                promotion = string.charAt(4) ;
            }
            Piece piece = arrBoard[intRow][intCol];
            if (piece == null)
            {
                System.err.println("Null piece in toMove: " + string + " -> [" + intRow + "," + intCol + "]");
                return null;
            }

            return new Move(this, piece, finRow, finCol);
        }
        return null ;

    }
    public void engineMove()
    {
        if (!againstHuman && whiteTurn == engineWhite)
        {
            String bestMove = null;
            try {
                bestMove = engine.getBestMove(position.fenString, 5);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Move move = toMove(bestMove);
            if (bestMove.length() == 5)
            {
                engineProm = bestMove.charAt(4) ;
            }
            move.castlingAttempt(this);
            if (move.castlingAttempt) {
                if (move.finCol == 6) {
                    Piece rook;
                    if (engineWhite)
                    {
                        rook = this.getPiece(7, 7);
                        rook.row = 7;
                        rook.col = 5;
                        this.arrBoard[7][5] = rook;
                        this.arrBoard[7][7] = null;
                    } else {
                        rook = this.getPiece(0, 7);
                        rook.row = 0;
                        rook.col = 5;
                        this.arrBoard[0][5] = rook;
                        this.arrBoard[0][7] = null;
                    }
                } else if (move.finCol == 2) {
                    Piece rook;
                    if (this.selectedPiece.isWhite) {
                        rook = this.getPiece(7, 0);
                        rook.row = 7;
                        rook.col = 3;
                        this.arrBoard[7][3] = rook;
                        this.arrBoard[7][0] = null;
                    } else {
                        rook = this.getPiece(0, 0);
                        rook.row = 0;
                        rook.col = 3;
                        this.arrBoard[0][3] = rook;
                        this.arrBoard[0][0] = null;
                    }
                }
            }
            engineTurn = false ;
            movePiece(move);
        }
    }
}
