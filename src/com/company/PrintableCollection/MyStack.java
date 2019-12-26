package com.company.PrintableCollection;

import com.company.MyComponent.MyFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class MyStack <T> extends Stack<T> implements PrintableCollection {

    final private static int HEIGHT = 24;
    final private static int WIDTH  = 60;//60
    final private static int X = 1;
    final private static int Y = 6766;

    protected int sleepTime=0;
    public void setSleepTime(int time){
        sleepTime=time;
    }
    public int getSleepTime() {
        return sleepTime;
    }

    private JPanel panel=new JPanel(null);
    private Stack<JButton> buttonStack =new Stack<>();

    private T lastVisit=null;

    public T getLastVisit(){
        return lastVisit;
    }
    public void setLastVisit(T lastVisit){
        if(size()==0){
            this.lastVisit=lastVisit;
        }
        else throw new RuntimeException();
    }


    public MyStack(){
        super();
        panel.setPreferredSize(new Dimension(WIDTH + 2,Y +15));
        MyFrame.getFrame().addStack(panel);
    }

    @Override
    public T pop() {
        T res= super.pop();
        lastVisit = res;
        panel.remove(this.buttonStack.pop());
        if( ! buttonStack.isEmpty()){buttonStack.peek().setBackground(Color.GRAY);}
        panel.updateUI();//<----------------------------
        try{
            Thread.sleep(sleepTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public T push(T node) {
        if(node==null)throw new RuntimeException();
        if( ! isEmpty())lastVisit = peek();
        JButton button = new JButton(node.toString());
        if( ! buttonStack.isEmpty()){buttonStack.peek().setBackground(Color.lightGray);}
        buttonStack.push(button);
        button.setBackground(Color.GRAY);
        if(Y-buttonStack.size()*(HEIGHT+1)<0){throw new StackOverflowError();}////“栈”溢出..........
        button.setBounds(X,Y-buttonStack.size()*(HEIGHT+1) , WIDTH,HEIGHT);
        panel.add(button);
        panel.updateUI();//---------------------------
        try{
            Thread.sleep(sleepTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.push(node);
    }

    public void setNotUseful(){
        MyFrame.getFrame().removeStack(panel);
        //frame.dispose();
    }
    @Override
    public void clear(){
        int tmp=sleepTime;
        setSleepTime(10);
        while (!isEmpty()){
            pop();
        }
        sleepTime=tmp;
    }
}
