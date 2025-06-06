package GUI;

import javax.swing.*;
import java.awt.*;

public class MoveList extends JList <String>
{
    public DefaultListModel<String> listModel ;
    public int noMoves ;
    String whiteMove ;
    public MoveList()
    {
        noMoves = 1 ;
        listModel = new DefaultListModel<>() ;
        setModel(listModel);
        setFont(new Font("JetBrains Mono",Font.PLAIN,18));
        setForeground(new Color(220,220,220));
        this.setBackground(new Color(38, 36, 33));
    }

    public void addMove(String move , boolean isWhite)
    {
        if (isWhite)
        {
            whiteMove = move ;
            String text = ""+noMoves+"."+move ;
            listModel.addElement(text);
        }
        else
        {
             String text = ""+noMoves+"."+whiteMove+"   "+move ;
            listModel.remove(listModel.size()-1) ;
            listModel.addElement(text);
            noMoves++ ;
        }
    }

    public void clear()
    {
        listModel.clear();
        noMoves = 0 ;
        whiteMove = null ;
    }



}
