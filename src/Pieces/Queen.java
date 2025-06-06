package Pieces;

import Logic.BoardLogic;
import Logic.Move;

import java.io.IOException;
import java.util.ArrayList;

public class Queen extends Piece
{

    public Queen(String name, boolean isWhite, int xPos, int yPos, int row, int col) throws IOException
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
    public ArrayList<Move> validMoves(BoardLogic board)
    {
        // Queen Moves are the union of Bishop and Rock moves
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
        int [][] diagonal = {
                {1,1},
                {1,-1},
                {-1,-1},
                {-1,1}
        } ;
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
        return validMoves;
    }
    public boolean attackedSquare(int row, int col, BoardLogic board)
    {
        int dRow = row - this.row;
        int dCol = col - this.col;

        // Diagonal movement (bishop-like)
        if (Math.abs(dRow) == Math.abs(dCol) && ( dRow != 0 && dCol != 0) )
        {
            int rowStep = Integer.compare(dRow, 0);
            int colStep = Integer.compare(dCol, 0);
            int r = this.row + rowStep;
            int c = this.col + colStep;
            while (r != row || c != col) {
                if (board.getPiece(r, c) != null) return false;
                r += rowStep;
                c += colStep;
            }
            return true;
        }

        // Straight line movement (rook-like)
        if ((dRow == 0 && dCol != 0) || (dRow != 0 && dCol == 0)) {
            int rowStep = Integer.compare(dRow, 0);
            int colStep = Integer.compare(dCol, 0);
            int r = this.row + rowStep;
            int c = this.col + colStep;
            while (r != row || c != col) {
                if (board.getPiece(r, c) != null) return false;
                r += rowStep;
                c += colStep;
            }
            return true;
        }

        // Not a valid queen move
        return false;
    }

    private boolean isInBounds(int r, int c)
    {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }
}
