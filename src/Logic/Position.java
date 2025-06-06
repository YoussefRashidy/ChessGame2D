package Logic;

import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Rook ;

import java.util.HashMap;
import java.util.Map;

public class Position
{
    String fenString ;
    BoardLogic board ;
    int halfMoves ;
    private Map<String, Integer> fenHistory;
    public String capturedW ;
    public String capturedB ;
    public Position(BoardLogic board)
    {
        this.fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" ;
        fenHistory = new HashMap<>();
        this.fenHistory.put(getRepetitionKey(fenString),1) ;
        this.board = board ;
        halfMoves = 0 ;
        capturedW = new String() ;
        capturedB = new String() ;
    }
    public void toFen()
    {
        StringBuilder fen = new StringBuilder() ;
        for (int i =0 ; i < 8 ; i++)
        {
            int blankSquares = 0 ;
            for (int j = 0 ; j < 8 ; j++)
            {
                Piece piece = board.getPiece(i,j) ;
                if (piece == null)
                {
                    blankSquares++ ;
                }
                else
                {
                    if (blankSquares != 0)
                    {
                        fen.append(blankSquares) ;
                        blankSquares = 0 ;
                    }
                    switch (piece.name)
                    {
                        case "rook":
                            fen.append(piece.isWhite ? 'R' : 'r'); break;
                        case "knight":
                            fen.append(piece.isWhite ? 'N' : 'n'); break;
                        case "bishop":
                            fen.append(piece.isWhite ? 'B' : 'b'); break;
                        case "queen":
                            fen.append(piece.isWhite ? 'Q' : 'q'); break;
                        case "king":
                            fen.append(piece.isWhite ? 'K' : 'k'); break;
                        case "pawn":
                            fen.append(piece.isWhite ? 'P' : 'p'); break;
                    }
                }

            }
            if (blankSquares != 0)
            {
                fen.append(blankSquares) ;
                blankSquares = 0 ;
            }
            if (i != 7)
            {
                fen.append('/') ;
            }
        }
        fen.append(' ') ;
        fen.append(board.whiteTurn ? 'w' : 'b') ;
        fen.append(' ') ;
        StringBuilder castling = new StringBuilder();
        Piece a1 = board.getPiece(7, 0); // White queenside rook
        Piece h1 = board.getPiece(7, 7); // White kingside rook
        Piece a8 = board.getPiece(0, 0); // Black queenside rook
        Piece h8 = board.getPiece(0, 7); // Black kingside rook
        if (!board.whiteKing.hasMoved)
        {
            if (h1 instanceof Rook && !h1.hasMoved && h1.isWhite) castling.append('K');
            if (a1 instanceof Rook && !a1.hasMoved && a1.isWhite) castling.append('Q');
        }
        if (!board.blackKing.hasMoved)
        {
            if (h8 instanceof Rook && !h8.hasMoved && !h8.isWhite) castling.append('k');
            if (a8 instanceof Rook && !a8.hasMoved && !a8.isWhite) castling.append('q');
        }
        fen.append(castling.isEmpty() ? "-" : castling.toString());
        fen.append(' ') ;
        String enpassant = "-" ;
        if (board.lastMove!= null && board.lastMove.piece instanceof Pawn)
        {
            int iniRow = board.lastMove.iniRow ;
            int finRow = board.lastMove.finRow ;
            if (Math.abs(finRow-iniRow) == 2)
            {
                int row = (finRow+iniRow) / 2 ;
                int col = board.lastMove.finCol ;
                char file = (char) ('a' + col);
                int rank = 8 - row;
                enpassant = "" + file + rank ;
            }
        }
        fen.append(enpassant) ;
        updateFen(fen.toString());
        fen.append(' ') ;
        if (board.lastMove != null)
        {
            if (board.lastMove.capture != null || board.lastMove.piece instanceof Pawn)
            {
                halfMoves = 0 ;
            }
            else
            {
                halfMoves ++ ;
            }
        }
        fen.append(halfMoves) ;
        fen.append(' ') ;
        fen.append(board.fullMove) ;
        fenString = fen.toString() ;
    }

    public void updateFen(String fen)
    {
        fenHistory.put(fen ,fenHistory.getOrDefault(fen, 0) + 1 ) ;
    }
    public boolean threeFold()
    {
        String fen =  getRepetitionKey(fenString) ;
        int repetition = fenHistory.getOrDefault(fen, 0);
        return repetition == 3;
    }
    public boolean fiftyMove()
    {
        return halfMoves == 100 ;
    }

    public String getRepetitionKey(String fenString)
    {
        String[] parts = fenString.split(" ");
        return String.join( " ",parts[0], parts[1], parts[2], parts[3]);
    }

    public void updateCapture(Move move)
    {
        if (move.capture == null)
            return;
        switch (move.capture.name)
        {
            case "pawn" :
                if (move.capture.isWhite)
                {
                    capturedW +='P' ;
                }
                else
                {
                    capturedB+= 'p' ;
                }
                break;
            case "knight" :
                if (move.capture.isWhite)
                {
                    capturedW +='N' ;
                }
                else
                {
                    capturedB+= 'n' ;
                }
                break;
            case "bishop" :
                if (move.capture.isWhite)
                {
                    capturedW +='B' ;
                }
                else
                {
                    capturedB+= 'b' ;
                }
                break;
            case "queen" :
                if (move.capture.isWhite)
                {
                    capturedW +='Q' ;
                }
                else
                {
                    capturedB+= 'q' ;
                }
                break;
            case "rook" :
                if (move.capture.isWhite)
                {
                    capturedW +='R' ;
                }
                else
                {
                    capturedB+= 'r' ;
                }
                break;

        }
    }

    public int charCount(char c , String string)
    {
        int counter = 0 ;
        for (int i = 0 ; i < string.length() ; i++ )
        {
            counter = string.charAt(i)==c ? 1 : 0 ;
        }
        return counter ;
    }




}
