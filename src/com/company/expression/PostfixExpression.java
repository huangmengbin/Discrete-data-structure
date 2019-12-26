package com.company.expression;

import com.company.PrintableCollection.*;
import com.company.MyComponent.MyFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class PostfixExpression extends Expression {
    PostfixExpression(){}
    PostfixExpression(String data){super(data);}

    public double toValue(){
        Stack<Double> numbers = new Stack<>();
        ptr=0;
        char ch = data.charAt(ptr);

        while (  ch !='#'  ){
            if( Character.isLetter(ch) ){
                throw new ArithmeticException();
            }
            else if(  Character.isDigit(ch) ){
                numbers.push(  nextDouble()  );
                ptr++;//越过分隔符
            }
            else if(isDanMuFu(ch)){
                numbers.push( calculate(numbers.pop(),ch) );
                ptr++;
            }
            else {
                numbers.push(   calculate(  numbers.pop(), ch , numbers.pop()  )  );
                ptr++;
            }

            ch = data.charAt(ptr);

        }

        return numbers.pop();
    }
    public ExpressionNode toTreeNode(){
        JPanel panel = new JPanel();
        return toTreeNode(panel);
    }

    public ExpressionNode toTreeNode(JPanel panel){///<------------------------------------::表达式树的生成：

        MyFrame frame = MyFrame.getFrame();

        MyList<Character> myList = new MyList<>();
        for(Character c:data.toCharArray()) {
            myList.add(c);
        }

        ptr=0;
        char ch = data.charAt(ptr);
        MyStack<ExpressionNode> nodes = new MyStack<>();
        final int sleepTime = 25;
        nodes.setSleepTime(sleepTime);
        ///------------------------------------------|
        try {
            int beforeptr = 0;
            while (ch != '#') {
                if (Character.isLetter(ch)) {////
                    ExpressionNode node = new ExpressionNode(nextWord());
                    frame.addGraph(node.updatePanelUI());
                    nodes.push(node);
                    ptr++;

                } else if (Character.isDigit(ch)) {//数字
                    ExpressionNode node = new ExpressionNode(nextDouble());
                    frame.addGraph(node.updatePanelUI());
                    nodes.push(node);
                    ptr++;//越过分隔符

                } else if (isDanMuFu(ch)) {///单目
                    if (ch == '~') {
                        ch = '-';
                    }//////  '~'  ->  '-'
                    ExpressionNode newNode = new ExpressionNode(ch);
                    frame.addGraph(newNode.updatePanelUI());
                    nodes.push(newNode);//只是拿来看的
                    nodes.pop();//只是拿来看的
                    newNode.setRight(nodes.pop());
                    frame.removeLastGraph();
                    frame.removeLastGraph();
                    frame.addGraph(newNode.updatePanelUI());
                    nodes.push(newNode);
                    ptr++;

                } else {                  //双目
                    ExpressionNode newNode = new ExpressionNode(ch);
                    frame.addGraph(newNode.updatePanelUI());
                    nodes.push(newNode);//只是拿来看的
                    nodes.pop();//只是拿来看的
                    newNode.setRight(nodes.pop());
                    newNode.setLeft(nodes.pop());
                    frame.removeLastGraph();
                    frame.removeLastGraph();
                    frame.removeLastGraph();
                    frame.addGraph(newNode.updatePanelUI());
                    nodes.push(newNode);
                    ptr++;
                }

                Thread.sleep(sleepTime);
                myList.setColor(beforeptr , beforeptr=ptr, Color.GRAY);//让已经访问的换一种颜色
                myList.setColor( ptr , Color.CYAN);
                ch = data.charAt(ptr);
            }//end while

        }catch (InterruptedException e){
            e.printStackTrace();
        }

        ExpressionNode result=nodes.pop();
        frame.removeLastGraph();
        result.updatePanelUI(panel);
        frame.addGraph(panel);
        myList.setNotUseful();
        nodes.setNotUseful();
        return result;
    }
}
