package GUI;

import Listeners.BoardListener;
import Logic.BoardLogic;
import Logic.Move;
import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ChessBoard extends JPanel
{
    public static final int squareSize = 80 ;
    public BoardLogic board ;
    public BoardListener mouse ;
    boolean lastMoveHL ;
    public static JFrame game ;
    public ChessBoard(BoardLogic boardLogic , JFrame game)
    {
        board = boardLogic ;
        mouse = new BoardListener(board,this) ;
        board.initializeBoard();
        this.setPreferredSize(new Dimension(squareSize*8,squareSize*8));
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);
        this.setDoubleBuffered(true) ;
        this.game = game ;
    }
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g ;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int row = 0 ; row < 8 ; row++ )
        {
            for (int col = 0 ; col < 8 ; col++)
            {
                if ((row+col) % 2 == 0)
                {
                    g2d.setPaint(new Color(255, 255, 255));
                    int drawCol = board.flipBoard ? 7 - col : col;
                    int drawRow = board.flipBoard ? 7 - row : row;
                    int drawX = drawCol * squareSize;
                    int drawY = drawRow * squareSize;

                    g2d.fillRect(drawX,drawY,squareSize,squareSize);

                }
                else
                {
                    g2d.setPaint(new Color(118, 150, 86));
                    int drawCol = board.flipBoard ? 7 - col : col;
                    int drawRow = board.flipBoard ? 7 - row : row;
                    int drawX = drawCol * squareSize;
                    int drawY = drawRow * squareSize;
                    g2d.fillRect(drawX,drawY,squareSize,squareSize);
                }
                if (board.arrBoard[row][col] != null && board.arrBoard[row][col] != board.selectedPiece)
                {
                    Piece piece = board.arrBoard[row][col] ;
                    int drawCol = board.flipBoard ? 7 - col : col;
                    int drawRow = board.flipBoard ? 7 - row : row;
                    int drawX = drawCol * squareSize;
                    int drawY = drawRow * squareSize;
                    g2d.drawImage(piece.image,drawX,drawY,squareSize,squareSize,null);
                }
                if (board.lastMove != null )
                {
                    if (board.lastMove.iniRow == row && board.lastMove.iniCOl == col)
                    {
                        g2d.setColor(new Color(255, 255, 120, 120)); // from square
                        int drawCol = board.flipBoard ? 7 - col : col;
                        int drawRow = board.flipBoard ? 7 - row : row;
                        int drawX = drawCol * squareSize;
                        int drawY = drawRow * squareSize;
                        g2d.fillRect(drawX, drawY, squareSize, squareSize);
                    }
                    else if (board.lastMove.finRow == row && board.lastMove.finCol == col)
                    {
                        g2d.setColor(new Color(190, 230, 80, 120)); // to square
                        int drawCol = board.flipBoard ? 7 - col : col;
                        int drawRow = board.flipBoard ? 7 - row : row;
                        int drawX = drawCol * squareSize;
                        int drawY = drawRow * squareSize;
                        g2d.fillRect(drawX, drawY, squareSize, squareSize);
                    }
                }
            }
            if (board.selectedPiece != null)
            {
                Piece piece = board.selectedPiece;
                g2d.drawImage(piece.image, piece.xPos, piece.yPos, squareSize, squareSize, null);
            }
        }
        if (board.selectedPiece != null)
        {
            for (Move move : board.selectedPiece.validMoves(board))
            {
                if (board.isValid(move))
                {
                    int row = move.finRow ;
                    int col = move.finCol ;
                    if (board.flipBoard)
                    {
                        col = 7 - col;
                        row = 7 - row;
                    }
                    int x = col * squareSize;
                    int y = row * squareSize;

                    // Dot size
                    int dotSize = squareSize / 5;
                    int dotX = x + (squareSize - dotSize) / 2;
                    int dotY = y + (squareSize - dotSize) / 2;

                    // Color: light gray or blueish
                    g2d.setColor(new Color(30, 144, 255, 180));
                    g2d.fillOval(dotX, dotY, dotSize, dotSize);
                }
            }
        }
    }
    public static String promotionMenu(boolean isWhite)
    {
        String[] pieceNames = { "queen", "rook", "bishop", "knight" };
        AtomicReference<String> selectedPiece = new AtomicReference<>("queen");

        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));

        Map<JButton, String> buttonMap = new HashMap<>();

        try {
            for (String name : pieceNames)
            {
                BufferedImage original = Piece.getImage(name, isWhite);
                Image scaled = original.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaled);

                JButton button = new JButton(icon);
                button.setFocusable(false);
                button.setBorderPainted(false);
                button.setContentAreaFilled(false);

                buttonMap.put(button, name);
                panel.add(button);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to load promotion piece images", e);
        }

        // Create a dialog with custom buttons
        JDialog dialog = new JDialog((Frame) null, "Pawn Promotion", true);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        // Add button listeners
        for (Map.Entry<JButton, String> entry : buttonMap.entrySet())
        {
            entry.getKey().addActionListener(e ->
            {
                selectedPiece.set(entry.getValue());
                dialog.dispose(); // close dialog
            });
        }
        dialog.setVisible(true);
        return selectedPiece.get();
    }
}
