package com.company.MyComponent;

import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton {

    private boolean isLoop;

    public MyButton(){
        super();
        this.setBackground(Color.orange);
    }

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    protected void paintBorder(Graphics g) {
        if(isLoop){  //外部类中
            g.setColor(Color.black);
            g.drawOval(0,0,getWidth()-1,getHeight()-1);
            final int x=(int)( (Math.pow(2,0.5)/4+0.5)*getWidth() );
            final int y=(int)( (0.5-Math.pow(2,0.5)/4)*getHeight() )+1;
            final int k=8;
            //((Graphics2D)g).setStroke(new BasicStroke(2.5f));
            g.drawLine(x,y,x-k,y);
            g.drawLine(x,y,x-k/2,y-k);
        }
    }
}

