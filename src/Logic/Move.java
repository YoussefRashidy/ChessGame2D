package Logic;

import Pieces.King;
import Pieces.Pawn;
import Pieces.Piece;

import java.util.Objects;

public class Move
{
    public int iniRow ;
    public int iniCOl ;
    public int finRow ;
    public int finCol ;
    public Piece piece ;
    public Piece capture ;
    public boolean castlingAttempt ;
    public boolean isEnpassant ;
    public Move (BoardLogic boardLogic , Piece piece , int newRow,int newCol)
    {
        iniRow = piece.row ;
        iniCOl = piece.col ;
        finRow = newRow ;
        finCol = newCol ;
        this.piece = piece ;
        if (isInBounds(newRow,newCol))
        {
            this.capture = boardLogic.arrBoard[newRow][newCol] ;
        }
        else
        {
            capture = null ;
        }
        castlingAttempt(boardLogic);
        isEnpassant = false ;
    }
    public Move (BoardLogic boardLogic , Piece piece , int newRow,int newCol,boolean isEnpassant)
    {
        iniRow = piece.row ;
        iniCOl = piece.col ;
        finRow = newRow ;
        finCol = newCol ;
        this.piece = piece ;
        if (isInBounds(newRow,newCol))
        {
            this.capture = boardLogic.arrBoard[boardLogic.lastMove.finRow][boardLogic.lastMove.finCol] ;
        }
        this.isEnpassant = isEnpassant ;
    }
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;
        Move move = (Move) o;
        return iniRow == move.iniRow &&
                iniCOl == move.iniCOl &&
                finRow == move.finRow &&
                finCol == move.finCol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(iniRow, iniCOl, finRow, finCol);
    }
    public void castlingAttempt(BoardLogic board)
    {
        if (!(piece instanceof King))
        {
            castlingAttempt = false ;
            return;
        }
        else
        {
            if (piece.isWhite)
            {
                if (finRow == 7)
                {
                    if (finCol == 6 || finCol == 7)
                    {
                        if (board.canCastleShort(true))
                        {
                            castlingAttempt = true ;
                            if (finCol == 7) finCol = 6 ;
                            return;
                        }
                    }
                    else if (finCol == 0 || finCol == 2)
                    {
                        if (board.canCastleLong(true))
                        {
                            castlingAttempt = true ;
                            if (finCol == 0) finCol = 2 ;
                            return;
                        }
                    }
                }
            }
            else
            {
                if (finRow == 0)
                {
                    if (finCol == 6 || finCol == 7)
                    {
                        if (board.canCastleShort(false))
                        {
                            castlingAttempt = true;
                            if (finCol == 7) finCol = 6;
                            return;
                        }
                    }
                    else if (finCol == 0 || finCol == 2)
                    {
                        if (board.canCastleLong(false))
                        {
                            castlingAttempt = true;
                            if (finCol == 0) finCol = 2;
                            return;
                        }
                    }
                }
            }
        }
    }
    public static boolean isEnPassantAttempt(BoardLogic board, Piece piece, int targetRow, int targetCol)
    {
        if (!(piece instanceof Pawn) || board.lastMove == null || !(board.lastMove.piece instanceof Pawn))
        {
            return false;
        }

        Move last = board.lastMove;
        int direction = piece.isWhite ? -1 : 1;

        if (Math.abs(last.finRow - last.iniRow) != 2)
        {
            return false;
        }
        if (piece.row == last.finRow && Math.abs(piece.col - last.finCol) == 1)
        {
            return targetRow == last.finRow + direction && targetCol == last.finCol;
        }

        return false;
    }

    private boolean isInBounds(int r, int c)
    {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }
    public String toText()
    {
        char intFile = (char)('a' + this.iniCOl) ;
        char finFile = (char)('a' + this.finCol) ;
        int intRank = 8 - iniRow ;
        int finRank = 8 - finRow ;
        return ""+intFile+intRank+finFile+finRank ;
    }

}
