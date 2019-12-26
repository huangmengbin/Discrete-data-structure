package com.company.PrintableNode;

import javax.swing.*;
import java.awt.*;

public class DrawThread {
    private PrintableNodeOfTreeBinary before=null;
    private boolean hasSucc = false;
    public void clear(){
        this.before=null;
        this.hasSucc = false;
    }
    public void drawThread(PrintableNodeOfTreeBinary node, JPanel panel){

        if(before!=null) {
            final int x1= node.getButton().getX()+  node.getButton().getWidth()/2,   y1= node.getButton().getY()+  node.getButton().getHeight()/2;
            final int x2= before.getButton().getX()+ before.getButton().getWidth()/2, y2= before.getButton().getY()+ before.getButton().getHeight()/2;
            if(node.lhs()==null){
                panel.add(new Line(x1- node.getButton().getWidth()/2,y1,x2,y2, Color.BLUE));//<前，blue
            }
            if(hasSucc){//实际上是它的前驱(before)hasSucc
                panel.add(new Line(x1,y1,x2+ before.getButton().getWidth()/2,y2, Color.pink));//<后，pink
            }
        }

        before=node;
        hasSucc = (node.rhs()==null) ;

        panel.updateUI();
    }

    private class Line extends JPanel {
        private int x1,x2,y1,y2;
        Color color;
        Line(int x1,int y1,int x2,int y2,Color color){
            this.x1=x1;
            this.x2=x2;
            this.y1=y1;
            this.y2=y2;
            this.color=color;
            this.setBounds(0,0,Math.max(x1,x2)+66,Math.max(y1,y2)+67);//理论上加 1 就 ok
        }
        protected void paintComponent(Graphics g){
            g.setColor(color);
            g.drawLine(x1,y1,x2,y2);
        }
    }
}
