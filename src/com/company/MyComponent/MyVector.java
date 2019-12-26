package com.company.MyComponent;

import java.awt.*;

public class MyVector extends MyLine {  //这仅仅是一条箭头而已，用法是:直接 new 即可
    private int len = 27;  //默认
    public MyVector(int x1,int y1, int x2, int y2, int len, Color color){this(x1,y1,x2,y2,len);this.color=color;}
    public MyVector(int x1,int y1,int x2,int y2,int len){this(x1,y1,x2,y2);this.len=len;}
    public MyVector(int x1,int y1,int x2,int y2){super(x1,y1,x2,y2);}
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g.setColor(color);
        g.drawLine(x1,y1,x2-(x2-x1)*len/(Math.abs(x2-x1)+Math.abs(y2-y1)),y2-(y2-y1)*len/(Math.abs(x2-x1)+Math.abs(y2-y1)));
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(3f));
        g2.drawLine(x2-(x2-x1)*len/(Math.abs(x2-x1)+Math.abs(y2-y1)),y2-(y2-y1)*len/(Math.abs(x2-x1)+Math.abs(y2-y1)),x2,y2);
    }
}


