package Pieces;

import Logic.BoardLogic;
import Logic.Move;

import java.io.IOException;
import java.util.ArrayList;

public class Knight extends Piece
{
    public Knight(String name, boolean isWhite, int xPos, int yPos, int row, int col) throws IOException
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

    public ArrayList<Move> validMoves (BoardLogic board )
    {
        ArrayList<Move> validMoves = new ArrayList<>() ;
        for (int i : new int[]{2,-2})
        {
            for (int j : new int[]{1,-1})
            {
                int nRow = this.row + i ;
                int nCol = this.col + j ;
                if (isInBounds(nRow,nCol))
                {
                    Piece target = board.getPiece(nRow,nCol);
                    if (target == null || ! board.sameSide(this,target))
                    {
                        validMoves.add(new Move(board,this,nRow,nCol )) ;
                    }
                }
            }
        }
        for (int i : new int[]{2,-2})
        {
            for (int j : new int[]{1,-1})
            {
                int nRow = this.row + j ;
                int nCol = this.col + i ;
                if (isInBounds(nRow,nCol))
                {
                    Piece target = board.getPiece(nRow,nCol);
                    if (target == null || ! board.sameSide(this,target))
                    {
                        validMoves.add(new Move(board,this,nRow,nCol )) ;
                    }

                }
            }
        }
        return validMoves ;
    }

    public boolean attackedSquare(int row, int col, BoardLogic board)
    {
        int dRow = Math.abs(this.row - row) ;
        int dCol = Math.abs(this.col - col) ;
        return (dRow == 2 && dCol == 1 || dRow == 1 && dCol == 2) ;
    }

    private boolean isInBounds(int r, int c)
    {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }
}
