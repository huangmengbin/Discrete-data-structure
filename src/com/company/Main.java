package com.company;

import com.company.expression.*;
import com.company.PrintableCollection.*;
import com.company.PrintableNode.*;
import javax.swing.*;

public class Main {


    public static void main(String [] _67666 ){
        //Main main =new Main();
        //test03();
        //test04();
    }

    private static void test01() {
        try {
            MyQueue<Integer> queue = new MyQueue<>();
            for (int i = 0; i < 10; ++i) {queue.add(i);Thread.sleep(500);}
            for (int i = 0; i < 5; ++i)  {queue.removeFirst();Thread.sleep(500);}
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void test02() {
        MyQueue<Integer> queue=new MyQueue<>();
        for(int i=0;i<30;++i)queue.add(i);
        MyQueue<Integer> queue1=new MyQueue<>();
        for(int i=0;i<65;++i)queue1.add(i);
        MyQueue<Integer> queue2=new MyQueue<>();
        queue2.add(16);
        MyQueue<Integer> queue3=new MyQueue<>();
        MyQueue<Integer> queue4=new MyQueue<>();
        MyQueue<Integer> queue5=new MyQueue<>();
        queue5.add(5);
        queue1.clear();
        queue1.setNotUseful();
        queue.setNotUseful();
        queue3.setNotUseful();
        queue4.setNotUseful();
    }
    private static void test03() { ///三个遍历
        InfixExpression infixExpression = new InfixExpression();
        infixExpression.setData("( ( a+b )*(c - d )) + ( a * f   +(x*y+y*z))");
        /**
        *  ( ( a+b )*(c - d )) + ( a * f   +(x*y+y*z))
        */
        PostfixExpression postfixExpression = infixExpression.toPostfix();
        println(postfixExpression);

        JPanel panel = new JPanel();
        ExpressionNode node = postfixExpression.toTreeNode(panel);

        //PrintableNode::visit相当于(PrintableNodeOfTree hmb) -> { hmb.twinkle();}  简写为 hmb -> hmb.twinkle()
        //lambda 表达式 实现类似 C++ 函数指针

        MyQueue<PrintableNode> queue1 = new MyQueue<>();
        MyQueue<PrintableNode> queue2 = new MyQueue<>();

        DrawThread d = new DrawThread();

        node.inOrder( ( PrintableNodeOfTreeBinary p ) -> {
            p.twinkle(500);
            d.drawThread(p,panel);
            queue1.add(p);
            p.getButton().setText("<html>"+"<center>"+p.getButton().getText()+"<br>"+"visit "+"</center>"+"</html>");
        });

        println();
        node.updatePanelUI(panel);
        d.clear();

        node.traverse(  PrintableNodeOfTreeBinary.Way.postOrder,     p -> {
            p.twinkle(500);
            d.drawThread(p,panel);
            queue2.add(p);
            p.getButton().setText("<html>"+"<center>"+p.getButton().getText()+"<br>"+" visit"+"</center>"+"</html>");
        });

        //queue1.clear();
        //queue1.setNotUseful();
        println();

    }
    private static void test04(){//第一题，postfixExpression to value

        InfixExpression infixExpression = new InfixExpression();
        String s="2.7-1.3";///(-3+(!1+3)*(8%6))+(5*4.6-(6*0.5+5/!0))+0*(60/8)
        infixExpression.setData(s);
        double d = infixExpression.toValue();
        println(d);
    }



    public static void print(Object... objects){
        for(Object i:objects)System.out.print(i+"  ");
    }

    public static void println(Object... objects){
        if(objects.length==0)
            System.out.println();
        else
            for(Object i:objects){
                System.out.println(i);
            }
    }


}