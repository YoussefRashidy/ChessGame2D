package Pieces;

import Logic.BoardLogic;
import Logic.Move;

import java.io.IOException;
import java.util.ArrayList;


public class Rook extends Piece
{
    public Rook(String name, boolean isWhite, int xPos, int yPos, int row, int col) throws IOException
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
        if (!((dRow == 0 && dCol != 0)||(dRow != 0 && dCol == 0)))
        {
            return false ;
        }
        int rowStep ;
        int colStep ;
        rowStep = Integer.compare(dRow, 0);
        colStep = Integer.compare(dCol, 0);
        int r = this.row + rowStep;
        int c = this.col + colStep;

        while (r != row || c != col)
        {
            if (board.getPiece(r, c) != null) return false;
            r += rowStep;
            c += colStep;
        }
        return true ;
    }

    public ArrayList<Move> validMoves(BoardLogic board)
    {
        ArrayList<Move> validMoves = new ArrayList<>();
        int[][] directions = {
                {1, 0},   // ↓
                {-1, 0},  // ↑
                {0, 1},   // →
                {0, -1}   // ←
        };
        for (int[] dir : directions)
        {
            int nRow = this.row;
            int nCol = this.col;

            while (true) {
                nRow += dir[0];
                nCol += dir[1];

                if (!isInBounds(nRow, nCol)) break;

                Piece target = board.getPiece(nRow, nCol);

                if (target == null)
                {
                    validMoves.add(new Move(board, this, nRow, nCol));
                }
                else
                {
                    if (!board.sameSide(this, target))
                    {
                        validMoves.add(new Move(board, this, nRow, nCol));
                    }
                    break;
                }
            }
        }
        return validMoves;
    }
    private boolean isInBounds(int r, int c)
    {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }
}
