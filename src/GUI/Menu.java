package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Consumer;

public class Menu extends JPanel
{
    public Menu (JFrame frame , Consumer <String> onModeSelected )
    {
        this.setPreferredSize(new Dimension(600,600));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.NONE;
        setBackground(new Color(46, 46, 46));
        JButton vsHuman = new JButton() ;
        vsHuman.setPreferredSize(new Dimension(300, 80));
        vsHuman.setBackground(new Color(62, 60, 60, 255));
        vsHuman.setForeground(new Color(240, 240, 240));
        vsHuman.setFocusPainted(false);
        vsHuman.setOpaque(true);
        vsHuman.setContentAreaFilled(true);
        vsHuman.setFont(new Font("Serif", Font.BOLD, 24));
        JButton vsStockfish = new JButton() ;
        vsStockfish.setPreferredSize(new Dimension(300, 80));
        vsStockfish.setBackground(new Color(62, 60, 60, 255));
        vsStockfish.setForeground(new Color(240, 240, 240));
        vsStockfish.setFocusPainted(false);
        vsStockfish.setOpaque(true);
        vsStockfish.setFont(new Font("Serif", Font.BOLD, 24));
        JButton exitbut = new JButton() ;
        exitbut.setPreferredSize(new Dimension(300, 80));
        exitbut.setBackground(new Color(62, 60, 60, 255));
        exitbut.setForeground(new Color(240, 240, 240));
        exitbut.setFocusPainted(false);
        exitbut.setOpaque(true);
        exitbut.setFont(new Font("Serif", Font.BOLD, 24));
        vsHuman.setText("♟ Player vs Player ♙");
        vsStockfish.setText("♜ Player vs Engine ♞");
        exitbut.setText("♔ Exit ♛");

        this.add(vsHuman,gbc) ;
        gbc.gridy = 1;
        this.add(vsStockfish,gbc) ;
        gbc.gridy = 2 ;
        this.add(exitbut,gbc) ;
        vsHuman.addActionListener(e ->
        {
            onModeSelected.accept("HUMAN");
            frame.dispose();
        });

        vsStockfish.addActionListener(e ->
        {
            onModeSelected.accept("STOCKFISH");
            frame.dispose();
        });

        exitbut.addActionListener(e ->
        {
            System.exit(0);
        });
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g ;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        BufferedImage image ;
        try
        {
            image = ImageIO.read(ClassLoader.getSystemResourceAsStream("icons8-chess-com-1000.png")) ;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        g2d.drawImage(image ,0,0,getWidth(),getHeight(),this);
    }
}
