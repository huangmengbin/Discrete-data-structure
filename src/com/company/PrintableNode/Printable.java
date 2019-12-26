package com.company.PrintableNode;

import javax.swing.*;
import java.awt.*;

public interface Printable {
    JButton getButton();
    void setButton(JButton jButton);

    default void repaintButton(){//-----------------》 恢复button的默认颜色
        if(getButton()!=null)getButton().setBackground(Color.orange);
    }

    default void twinkle(Color color, int time){
        try {
            Color oldColor = getButton().getBackground();
            for(int i=0;i<2;i++) {
                getButton().setBackground(color);
                Thread.sleep(time/4);
                getButton().setBackground(oldColor);
                Thread.sleep(time/2);
            }
            Thread.sleep(time);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    default void twinkle(int time){
        twinkle(Color.CYAN,time);
    }

}
