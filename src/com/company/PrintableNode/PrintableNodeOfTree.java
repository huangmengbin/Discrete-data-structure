package com.company.PrintableNode;

import com.company.MyComponent.BrokenLine;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;

public abstract class PrintableNodeOfTree extends PrintableNode {

    //data:

    LinkedList<PrintableNodeOfTree> childrenList=new LinkedList<>();


    protected int width;//按钮宽度
    int space;//给予的水平宽度的总和，实际上是个中间变量
    protected int xPosition;//水平绝对位置

    protected String toNodeString(){
        return toString();
    }

    protected int _height(){//可被覆盖，指的是按钮的高度
        return 50;
    }
    protected int _verticalDistance(){//可被覆盖，指的是相邻按钮的垂直距离
        return 30;
    }

    protected int _charWidth(){//可被覆盖，指的是每增加一个字符后，按钮所增加的宽度
        return 14;
    }
    protected int _basicWidth(){//可被覆盖，指的是按钮的基本宽度
        return 35;
    }
    protected int _horizontalDistance(){//可被覆盖，相邻按钮的(最小)水平距离
        return 20;
    }

    protected int _horizonStart(){//可被覆盖
        return 20;
    }
    protected int _verticalStart(){//可被覆盖
        return 20;
    }

    //function:

    //public
    public void repaintAllButton(){
        repaintButton();
        getButton().setText(toNodeString());
        for(PrintableNodeOfTree node:this.childrenList){
            if(node!=null)node.repaintAllButton();
        }
    }


    public JPanel updatePanelUI(JPanel panel){
        panel.setLayout(null);
        setChildrenListAndCalculate();
        panel.removeAll();
        int deep = drawButtonAndLine(_verticalStart(),panel);
        Dimension dimension = new Dimension(this.space+_horizonStart()+23,deep+_height()+67);
        panel.setPreferredSize(dimension);//事先计算图形的总尺寸
        panel.setSize(dimension);
        panel.updateUI();    //---------------------------------//与 repaint 的区别 ？？？
        return panel;
    }


    //abstract
    abstract protected void _Who_are_your_children();//实类中实现，使用下面两个方法之一
    abstract protected void My_children_are(PrintableNodeOfTree... children);//抽象类中实现
    abstract protected void My_Children_are_in(Collection<?extends PrintableNodeOfTree> children);//抽象类中实现

    abstract void calculate();//执行完此操作后，width和position要得到正确的值
    protected int calculateWidth(){
        return _basicWidth()+toNodeString().length()* _charWidth();
    }

    //private:
    private JButton button;

    public JButton getButton() {
        return button;
    }
    public void setButton(JButton button) {
        this.button = button;
    }

    private void setChildrenList(){
        this._Who_are_your_children();//set childrenList
        for(PrintableNodeOfTree child:childrenList){
            if(child!=null)
                child.setChildrenList();
        }
    }

    private void setChildrenListAndCalculate(){
        setChildrenList();
        calculate();
    }

    protected int drawButtonAndLine(int Y, JPanel panel){//绘制按钮和折线，顺便计算最深的距离并返回

        int result = Y;
        this.setButton(new JButton());
        getButton().setText(toNodeString());
        repaintButton();//设置颜色
        getButton().setBounds(xPosition,Y, width, _height());
        panel.add(getButton());
        Y = Y+ _height()+ _verticalDistance();

        getButton().addActionListener(actionEvent ->  {///////---------------------------->  button 被点击
                paintPointedFrom();
                for(PrintableNodeOfTree node:PrintableNodeOfTree.this.childrenList){
                    if(node!=null){node.paintPointedTo();}
                }
        });


        for(PrintableNodeOfTree child:childrenList){
            if(child!=null) {
                panel.add(new BrokenLine(child.xPosition + child.width / 2,        Y,
                                                this.xPosition + width / 2,   Y - _verticalDistance()));
                result=Math.max(child.drawButtonAndLine(Y, panel),result);
            }
        }
        return result;
    }



}
