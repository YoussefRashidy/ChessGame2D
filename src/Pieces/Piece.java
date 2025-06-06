package Pieces;

import Logic.BoardLogic;
import Logic.Move ;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Piece
{
    public String name ;
    public boolean isWhite ;
    public int xPos,yPos;
    public int row,col ;
    public boolean isPinned ;
    public boolean hasMoved ;
    public BufferedImage image ;
    public static final int heigt = 854 / 2;
    public static final int width = 2560 / 6;
    public static final int square = 80 ;
    public static String [] pieceOrder = {"king", "queen", "bishop", "knight", "rook", "pawn"};
    public Piece(String name , boolean isWhite , int xPos , int yPos, int row , int col)
    {
        this.name = name ;
        this.isWhite = isWhite ;
        this.xPos = xPos ;
        this.yPos = yPos ;
        this.row = row ;
        this.col = col ;
        this.isPinned = false ;
        this.hasMoved = false ;
    }
    public static BufferedImage getImage(String piece , boolean side) throws IOException
    {
        int row = side ? 0 : 1 ;
        int order = getOrder(piece) ;
        BufferedImage image = ImageIO.read(ClassLoader.getSystemResourceAsStream("Chess_Pieces_Sprite.png")) ;
        BufferedImage subImage = image.getSubimage(order * width,row*heigt,width,heigt) ;
        return subImage ;
    }
    public static int getOrder(String piece)
    {
        for (int i = 0 ; i < 6 ; i++ )
        {
            if (piece.equals(pieceOrder[i]))
            {
                return  i ;
            }
        }
        throw new IllegalArgumentException("Invalid piece name: " + piece);
    }
    public String getUnicode()
    {
        switch (name) {
            case "pawn":   return isWhite ? "♙" : "♟";
            case "rook":   return isWhite ? "♖" : "♜";
            case "knight": return isWhite ? "♘" : "♞";
            case "bishop": return isWhite ? "♗" : "♝";
            case "queen":  return isWhite ? "♕" : "♛";
            case "king":   return isWhite ? "♔" : "♚";
            default:       return "?";
        }
    }

    public BufferedImage getImage()
    {
        return image ;
    }
    public abstract boolean isValidMove(Move move , BoardLogic board);
    public abstract boolean attackedSquare (int row , int col , BoardLogic board) ;
    public abstract ArrayList<Move> validMoves(BoardLogic board) ;

}
