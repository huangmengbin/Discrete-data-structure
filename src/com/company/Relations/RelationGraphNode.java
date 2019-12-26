package com.company.Relations;

import com.company.MyComponent.MyButton;
import com.company.PrintableNode.Printable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

class RelationGraphNode<T> implements Printable {//用于辅助处理序关系
    //private static  int i;
    RelationGraphNode(T data) {
        this.data=data;
        //button.addActionListener(event -> new Thread(  () -> twinkle(1000) ).start());
        button.addActionListener ( event -> new Thread(  () -> {
            //button.setText(i++ +": "+button.getText());
            Color color = Color.orange;
            valid = false ;
            int j1=0,j2=0;
            ArrayList<RelationGraphNode<T>> nodeArrayList1 = new ArrayList<>();
            for (RelationGraphNode<T> node : before){node.valid=false;nodeArrayList1.add(node);}
            while (j1<nodeArrayList1.size()){
                for(RelationGraphNode<T>node:nodeArrayList1.get(j1).before){
                    if(node.valid){
                        node.valid=false;
                        nodeArrayList1.add(node);
                    }
                }
                j1++;
            }
            ArrayList<RelationGraphNode<T>> nodeArrayList2 = new ArrayList<>();
            for (RelationGraphNode<T> node : next){node.valid=false;nodeArrayList2.add(node);}
            while (j2<nodeArrayList2.size()){
                for(RelationGraphNode<T>node:nodeArrayList2.get(j2).next){
                    if(node.valid){
                        node.valid=false;
                        nodeArrayList2.add(node);
                    }
                }
                j2++;
            }


            try {
                for(int i=1;i<=2;++i){
                    getButton().setBackground(Color.cyan);
                    for(RelationGraphNode<T>node:nodeArrayList1){node.getButton().setBackground(new Color(200,255,200));}
                    for(RelationGraphNode<T>node:nodeArrayList2){node.getButton().setBackground(new Color(255,211,222));}
                    for (RelationGraphNode<T> node : before) {node.getButton().setBackground(Color.green);}
                    for (RelationGraphNode node : next)   {node.getButton().setBackground(Color.pink);}
                    Thread.sleep(400);
                    getButton().setBackground(color);
                    for (RelationGraphNode<T> node : before) {node.getButton().setBackground(color);}
                    for (RelationGraphNode<T> node : next)   {node.getButton().setBackground(color);}
                    for(RelationGraphNode<T>node:nodeArrayList1){node.getButton().setBackground(color);}
                    for(RelationGraphNode<T>node:nodeArrayList2){node.getButton().setBackground(color);}
                    Thread.sleep(200);
                }
            }
            catch (InterruptedException e1){
                e1.printStackTrace();
            }
            finally {
                for(RelationGraphNode<T>node:nodeArrayList1){node.valid=true;}
                for(RelationGraphNode<T>node:nodeArrayList2){node.valid=true;}
                valid=true;
            }
        } ).start() );
    }

    T data;
    private MyButton button = new MyButton();

    @Override
    public void setButton(JButton button) {
        this.button = (MyButton) button;
    }
    @Override
    public MyButton getButton() {
        return button;
    }
    @Override
    public String toString() {
        return data.toString();
    }
    @Override
    public int hashCode(){
        return data.hashCode()+1;
    }

    boolean valid=true;
    boolean lvalid =false;
    int depth = 0;
    LinkedList<RelationGraphNode<T>> before=new LinkedList<>();
    LinkedList<RelationGraphNode<T>> next  =new LinkedList<>();

}
