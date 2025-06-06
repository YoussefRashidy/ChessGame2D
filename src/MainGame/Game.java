package MainGame;

import GUI.ChessBoard;
import GUI.Menu;
import GUI.MoveList;
import Logic.BoardLogic;

import javax.swing.*;
import java.awt.*;

public class Game
{
    static JFrame game ;
    static String gameMode ;
    public static void main(String [] args)
    {
        JFrame game = new JFrame() ;
        ImageIcon icon = new ImageIcon("src/resources/icons8-chess-com-1000.png") ;
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        game.setSize(1000,1000);
        game.setTitle("Chess");
        game.setIconImage(icon.getImage());
        Menu menu = new Menu(game,mode -> {startGame(mode);}) ;
        game.setLayout(new GridBagLayout());
        game.getContentPane().add(menu);
        game.setVisible(true);
        game.setLocationRelativeTo(null);
        game.pack();

    }
    public static void startGame(String mode)
    {
        gameMode = mode ;
        MoveList list = new MoveList() ;
        JScrollPane scrollPane = new JScrollPane(list) ;
        scrollPane.setPreferredSize(new Dimension(150,480));
        BoardLogic boardLogic = new BoardLogic(list);
        game = new JFrame("Chess Game");
        ImageIcon icon =new ImageIcon("src/resources/icons8-chess-com-1000.png") ;
        game.setIconImage(icon.getImage());
        ChessBoard boardUI = new ChessBoard(boardLogic,game);
        boardLogic.setGameMode(mode);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.getContentPane().setBackground(new Color(25,24,22));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        game.setLayout(new GridBagLayout());
        game.add(boardUI,gbc);
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(5,5,5,5) ;
        game.add(scrollPane,gbc);
        game.pack();
        game.setLocationRelativeTo(null);
        game.setVisible(true);

    }

    public static void resetGame()
    {
        game.getContentPane().removeAll();
        MoveList list = new MoveList() ;
        JScrollPane scrollPane = new JScrollPane(list) ;
        scrollPane.setPreferredSize(new Dimension(150,480));
        BoardLogic boardLogic = new BoardLogic(list);
        ChessBoard boardUI = new ChessBoard(boardLogic,game);
        boardLogic.setGameMode(gameMode);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        game.setLayout(new GridBagLayout());
        game.add(boardUI,gbc);
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(5,5,5,5) ;
        game.add(scrollPane,gbc);
        game.pack();
    }

}