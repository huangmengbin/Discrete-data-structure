package com.company.PrintableNode;

import com.company.MyComponent.MyButton;
import com.company.MyComponent.MyVector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;
import java.util.Stack;

public abstract class PrintableNodeOfGraph extends PrintableNode {

    //data:
    private MyButton button = new MyButton();

    public MyButton getButton() {
        return button;
    }
    public void setButton(JButton button) {
        this.button = (MyButton) button;
    }

    final static private double _2PI = Math.PI*2;

    private ArrayList<PrintableNodeOfGraph> childrenList =new ArrayList<>();//内部不允许有空节点

    private int x,y;

    protected String toNodeString(){
        return toString();
    }

    protected int _horizonStart(){//可被覆盖
        return 300;
    }
    protected int _verticalStart(){//可被覆盖
        return _buttonHeight()+20;
    }
    protected int _buttonHeight(){return 30;}
    protected int _charWidth(){//可被覆盖，指的是每增加一个字符后，按钮所增加的宽度
        return 14;
    }
    protected int _basicWidth(){//可被覆盖，指的是按钮的基本宽度
        return 35;
    }
    protected int _multiple(){//倍数
        return 24;
    }
    protected int _R(int number){//传入总顶点数目number ，返回半径
        return number * _multiple();
    }


    // get children

    final protected void My_children_are(PrintableNodeOfGraph... children){
        this.childrenList.clear();
        for(PrintableNodeOfGraph child:children){
            if(child!=null){            //使得childrenList内没有空指针
                this.childrenList.add(child);
            }
        }
    }
    final protected void My_Children_are_in(Collection<?extends PrintableNodeOfGraph> children){
        this.childrenList.clear();
        for(PrintableNodeOfGraph child:children){
            if(child!=null){
                childrenList.add(child);
            }
        }
    }



    //function:

    //public：
    public void repaintAllButton(){
        Stack<PrintableNodeOfGraph> stack = new Stack<>();
        ArrayList<PrintableNodeOfGraph> list = new ArrayList<>();
        stack.push(this);
        while ( ! stack.isEmpty()){
            PrintableNodeOfGraph currentNode = stack.pop();
            if( ! list.contains(currentNode)) {
                list.add(currentNode);
                currentNode.repaintButton();
                for(PrintableNodeOfGraph node:currentNode.childrenList){
                    stack.push(node);
                }
            }
        }
    }

    public JPanel updatePanelUI(JPanel panel)  {
        panel.setLayout(null);
        panel.removeAll();
        ArrayList<PrintableNodeOfGraph> nodeArrayList = this.toList();
        int totalWidth = calculateXY(nodeArrayList);
        //end initial point x y


        for(int i =0;i<nodeArrayList.size();i++){
            nodeArrayList.get(i).drawButtonAndLine(i,nodeArrayList.size(),panel);
        }//end draw

        Dimension dimension = new Dimension(totalWidth+67,
                23+_buttonHeight()+_verticalStart()+2*_R(nodeArrayList.size()));

        panel.setPreferredSize(dimension);
        panel.setSize(dimension);
        ///要事先计算图形的总尺寸

        panel.updateUI();

        return panel;
    }




    //private work
    private ArrayList<PrintableNodeOfGraph> toList() {
        ArrayList<PrintableNodeOfGraph> list = new ArrayList<>();
        Stack<PrintableNodeOfGraph>stack = new Stack<>();
        stack.push(this);
        toList(list,stack);
        return list;
    }


    private static void toList(ArrayList<PrintableNodeOfGraph> list , Stack<PrintableNodeOfGraph> stack){//扩列,深度优先.非递归

        while ( ! stack.isEmpty()){
            PrintableNodeOfGraph currentNode = stack.pop();
            currentNode._Who_are_your_children();

            if( ! list.contains(currentNode)) {
                list.add(currentNode);

                ListIterator<PrintableNodeOfGraph> iterator = currentNode.childrenList.listIterator(currentNode.childrenList.size());
                while (iterator.hasPrevious()){
                    stack.push(iterator.previous());
                }

            }
        }
    }


    private int calculateXY(ArrayList<PrintableNodeOfGraph> nodeArrayList){ //顺便返回最大的宽
        final int number=nodeArrayList.size();
        final int R = _R(number);
        final int X =  R + _horizonStart();
        final int Y =  R + _verticalStart();
        int result=0;

        for(int i=0;i<number;i++) {
            final double alpha = i * _2PI / number;//double
            final double deltaX = R * Math.sin(alpha);
            final double deltaY = R * Math.cos(alpha);
            nodeArrayList.get(i).x = (int) (X + deltaX);
            nodeArrayList.get(i).y = (int) (Y - deltaY);

            result=Math.max(result, nodeArrayList.get(i).x+nodeArrayList.get(i).toNodeString().length()*_charWidth()+_basicWidth());
        }

        return result;
    }

    private void drawButtonAndLine(int i,int number,JPanel panel){
        repaintButton();//设置 button 的默认颜色
        getButton().setText(toNodeString());
        final int width = toNodeString().length()*_charWidth()+_basicWidth();

        if(i==0){//+y轴
            getButton().setBounds(x-width/2,    y-_buttonHeight() ,    width,_buttonHeight());
        }
        else if(i*4<number){//1象限,+x轴
            getButton().setBounds(x,            y-_buttonHeight()   ,      width,_buttonHeight());
        }
        else if(i*4==number){
            getButton().setBounds(x,            y-_buttonHeight()/2   ,      width,_buttonHeight());
        }
        else if(i*2<number){//4象限
            getButton().setBounds(x,               y,                         width,_buttonHeight());
        }
        else if(i*2==number){//-y轴
            getButton().setBounds(x-width/2 ,   y,                       width,_buttonHeight());
        }
        else if(i*4<number*3){//3象限
            getButton().setBounds(x-width,      y,                         width,_buttonHeight());
        }
        else if(i*4==number*3){//-x轴
            getButton().setBounds(x-width,   y-_buttonHeight()/2,        width,_buttonHeight());
        }
        else{//2象限
            getButton().setBounds(x-width,      y-_buttonHeight(),     width,_buttonHeight());
        }

        getButton().setLoop(false);//画线,同时判断是否自环
        for(PrintableNodeOfGraph endNode:  childrenList){//画线,同时判断是否自环
            if( this != endNode){
                panel.add(new MyVector(x,y,endNode.x,endNode.y));
            }
            else{
                getButton().setLoop(true);
            }
        }//end for


        getButton().addActionListener(actionEvent ->  { /////////------------------------->  button 被点击
            paintPointedFrom();
            for(PrintableNodeOfGraph child: PrintableNodeOfGraph.this.childrenList){
                if(child!=null){child.paintPointedTo();}
            }

        });
        panel.add(getButton());///
    }









}
