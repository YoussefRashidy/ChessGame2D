package Pieces;

import Logic.BoardLogic;
import Logic.Move;

import java.io.IOException;
import java.util.ArrayList;

public class King extends Piece
{
    public King(String name, boolean isWhite, int xPos, int yPos, int row, int col) throws IOException
    {
        super(name, isWhite, xPos, yPos, row, col);
        image = getImage(name ,isWhite) ;
    }

    @Override
    public boolean isValidMove(Move move, BoardLogic board)
    {
        ArrayList<Move> validMoves = validMoves(board) ;
        return validMoves.contains(move);
    }
    public ArrayList<Move> validMoves(BoardLogic board)
    {
        ArrayList<Move> validMoves = new ArrayList<>();
        int [][] directions = {{1,0},{0,1},{1,1},{1,-1},{-1,1},{-1,-1},{0,-1},{-1,0}} ;
        for (int [] dir : directions)
        {
            int nRow = this.row + dir[0] ;
            int nCol = this.col + dir[1] ;
            if (isInBounds(nRow,nCol))
            {
                if (board.sameSide(this,board.getPiece(nRow,nCol)))
                {
                    continue;
                }
                if (!isAttacked(nRow,nCol,board))
                {
                    validMoves.add(new Move(board,this,nRow,nCol)) ;
                }
            }
        }
        // Short Castling
        if (!this.hasMoved)
        {
            if (this.isWhite)
            {
                Piece rook = board.getPiece(7,7) ;
                if (board.getPiece(7,5) == null && board.getPiece(7,6) == null)
                {
                    if (rook != null && rook instanceof Rook && !rook.hasMoved)
                    {
                        if (!(isAttacked(7,4,board) || isAttacked(7,5,board) || isAttacked(7,6,board)))
                        {
                            validMoves.add(new Move(board,this,7,6)) ;
                        }
                    }
                }
            }
            else
            {
                Piece rook = board.getPiece(0,7) ;
                if (board.getPiece(0,5) == null && board.getPiece(0,6) == null)
                {
                    if (rook != null && rook instanceof Rook && !rook.hasMoved)
                    {
                        if (!(isAttacked(0,4,board) || isAttacked(0,5,board) || isAttacked(0,6,board)))
                        {
                            validMoves.add(new Move(board,this,0,6)) ;
                        }
                    }
                }
            }
        }
        // Long Castling
        if (!this.hasMoved)
        {
            if (this.isWhite)
            {
                Piece rook = board.getPiece(7,0) ;
                if (board.getPiece(7,1) == null && board.getPiece(7,2) == null && board.getPiece(7,3) == null)
                {
                    if (rook != null && rook instanceof Rook && !rook.hasMoved)
                    {
                        if (!(isAttacked(7,4,board) || isAttacked(7,3,board) || isAttacked(7,2,board)))
                        {
                            validMoves.add(new Move(board,this,7,2)) ;
                        }
                    }
                }
            }
            else
            {
                Piece rook = board.getPiece(0,0) ;
                if (board.getPiece(0,1) == null && board.getPiece(0,2) == null && board.getPiece(0,3) == null)
                {
                    if (rook != null && rook instanceof Rook && !rook.hasMoved)
                    {
                        if (!(isAttacked(0,4,board) || isAttacked(0,3,board) || isAttacked(0,2,board) ))
                        {
                            validMoves.add(new Move(board,this,0,2)) ;
                        }
                    }
                }
            }
        }
        return validMoves ;
    }
    public boolean isAttacked (int row , int col ,BoardLogic board)
    {
        boolean attacked = false ;
            for (int i = 0 ; i < 8 ; i++)
            {
                for (int j = 0 ; j < 8 ; j++)
                {
                    Piece p = board.getPiece(i, j);
                    if (p != null && p.isWhite != this.isWhite)
                    {
                        if (p.attackedSquare(row,col,board))
                        {
                            return true ;
                        }
                    }
                }
            }
            return attacked ;
    }
    public boolean attackedSquare(int row, int col, BoardLogic board)
    {
        int dRow = Math.abs(this.row - row);
        int dCol = Math.abs(this.col - col);
        return dRow <= 1 && dCol <= 1 && (dRow + dCol != 0);
    }

    private boolean isInBounds(int r, int c)
    {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }

}
