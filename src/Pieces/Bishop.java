package Pieces;

import Logic.BoardLogic;
import Logic.Move;

import java.io.IOException;
import java.util.ArrayList;

public class Bishop extends Piece
{
    public Bishop(String name, boolean isWhite, int xPos, int yPos, int row, int col) throws IOException
    {
        super(name, isWhite, xPos, yPos, row, col);
        image = getImage(name,isWhite) ;
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
        int dRow = row - this.row ;
        int dCol = col - this.col ;
        if (Math.abs(dCol) != Math.abs(dRow) || ( dCol == 0  || dRow == 0 ))
        {
            return false ;
        }
        int rowStep = dRow > 0 ? 1 : -1;
        int colStep = dCol > 0 ? 1 : -1;

        int r = this.row + rowStep;
        int c = this.col + colStep;

        while (r != row || c != col && isInBounds(r,c))
        {
            if (board.getPiece(r, c) != null) return false;
            r += rowStep;
            c += colStep;
        }

        return true;

    }

    public ArrayList<Move> validMoves (BoardLogic board )
    {
        ArrayList<Move> validMoves = new ArrayList<>() ;
        int [][] diagonal = {{1,1},{1,-1},{-1,-1},{-1,1}} ;
        for (int [] coor : diagonal)
        {
            int nRow = this.row ;
            int nCol = this.col ;
            while (true)
            {
                 nRow += coor[0]  ;
                 nCol += coor[1]  ;
                 if (isInBounds(nRow,nCol))
                 {
                     if (board.getPiece(nRow,nCol) == null)
                     {
                         validMoves.add(new Move(board,this,nRow,nCol)) ;
                     }
                     else
                     {
                         if (board.sameSide(this,board.getPiece(nRow,nCol)))
                         {
                             break;
                         }
                         else
                         {
                             validMoves.add(new Move(board,this,nRow,nCol)) ;
                             break;
                         }
                     }
                 }
                 else
                 {
                     break;
                 }
            }

        }
        return validMoves ;
    }
    private boolean isInBounds(int r, int c)
    {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }
}
