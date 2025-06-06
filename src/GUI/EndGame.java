package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

public class EndGame extends JDialog
{
    public EndGame(JFrame parent, String title, String message, boolean whiteWins, ActionListener reset, ActionListener exit)
    {
        super(parent, title, true);
        setUndecorated(true);
        JPanel panel = new JPanel() ;
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25,30,25,30));
        JLabel label = new JLabel(message) ;
        label.setFont(new Font("Impact", Font.BOLD, 48));
        label.setForeground(new Color(230, 230, 230));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(20,20,10,20));
        JButton resetBut = new JButton("Play Agian") ;
        JButton exitBut = new JButton("Exit") ;
        resetBut.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        resetBut.setBackground( new Color(60, 60, 60));
        resetBut.setForeground(Color.WHITE);
        resetBut.setPreferredSize(new Dimension(150,40));
        resetBut.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetBut.setFocusPainted(false);

        exitBut.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        exitBut.setBackground( new Color(60, 60, 60));
        exitBut.setForeground(Color.WHITE);
        exitBut.setPreferredSize(new Dimension(150, 40));
        exitBut.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBut.setFocusPainted(false);



        resetBut.addActionListener(e->{
            dispose();
            reset.actionPerformed(e);
        });

        exitBut.addActionListener(e->
        {
            dispose();
            System.exit(0);
        });

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(resetBut);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(exitBut);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

}
