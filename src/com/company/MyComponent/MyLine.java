package com.company.MyComponent;

import javax.swing.*;
import java.awt.*;

public class MyLine extends JPanel {
    int x1,x2,y1,y2;
    Color color=Color.black;
    public MyLine(int x1,int y1,int x2,int y2,Color color){
        this(x1,y1,x2,y2);
        this.color=color;

    }
    public MyLine(int x1,int y1,int x2,int y2){
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
        this.setBounds(0,0,Math.max(x1,x2)+67,Math.max(y1,y2)+67);//理论上加 1 就 ok
    }
    protected void paintComponent(Graphics g){
        g.setColor(color);
        g.drawLine(x1,y1,x2,y2);
    }
}
