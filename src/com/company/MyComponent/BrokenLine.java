package com.company.MyComponent;

import java.awt.*;

public class BrokenLine extends MyLine  {  //这仅仅是一条折线而已，用法是:直接 new 即可
    public BrokenLine(int x1,int y1,int x2,int y2){super(x1,y1,x2,y2);}
    protected void paintComponent(Graphics g){
        //g.drawLine(x1,y1,x2,y2);
        g.setColor(color);
        g.drawLine(x1,           y1,    x1,(y1+y2)/2);
        g.drawLine(x2,           y2,    x2,(y1+y2)/2);
        g.drawLine(x1,(y1+y2)/2,    x2,(y1+y2)/2);
    }
}

