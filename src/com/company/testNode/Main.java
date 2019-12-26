package com.company.testNode;

import javax.swing.*;

public class Main {

    public static void main(String [] rtw666 ){
        test01();
    }

    private static void test01() {

        TestNode01 node1 = new TestNode01(1);
        TestNode01 node2 = new TestNode01("2222222222222");
        TestNode01 node3 = new TestNode01("3333333333");
        TestNode01 node4 = new TestNode01(4);
        TestNode01 node5 = new TestNode01(5);
        TestNode01 node6 = new TestNode01(67666);

        node1.lhs=node2;
        node1.rhs=node3;
        node1.createUI(new JPanel());
        node2.lhs=node4;
        node2.rhs=node5;
        node3.rhs=node6;


    }

    private static void test02(){
        TestNode02 []nodes = new TestNode02[16];
        for(int i=0;i<16;i++){
            nodes[i]=new TestNode02 ( i );
        }
        JPanel panel=new JPanel();
        nodes[0].createUI(panel);
        for(int i=0;i<15;i++){
            nodes[i].next=nodes[i+1];
        }
        nodes[15].next=nodes[0];
        nodes[0].updatePanelUI(panel);
    }
    private static void test03(){

        TestNode03 node1=new TestNode03(1);
        TestNode03 node2=new TestNode03(2);
        TestNode03 node3=new TestNode03(3);
        TestNode03 node4=new TestNode03(4);
        TestNode03 node5=new TestNode03(5);
        TestNode03 node6=new TestNode03(6);
        TestNode03 node7=new TestNode03(7);
        TestNode03 node8=new TestNode03(8);


        node1.connect(node8);
        node1.connect(node3);
        node2.connect(node1);
        node2.connect(node3);

        node1.createUI();

        node1.connect(node1);
        node3.connect(node1);
        node3.connect(node5);
        node4.connect(node6);
        node5.connect(node8);
        node5.connect(node5);
        node5.connect(node7);
        node7.connect(node2);
        node8.connect(node7);


    }




    public static void print(Object... objects){
        if(objects==null)
            return;
        for(Object i:objects)
            if(i!=null)
                System.out.print(i+"  ");
            else
                System.err.print(i+"  ");
    }


    public static void println(Object... objects){
        if(objects==null)
            return;
        for(Object i:objects)
            if(i!=null)
                System.out.println(i);
            else
                System.err.println(i);
    }

}