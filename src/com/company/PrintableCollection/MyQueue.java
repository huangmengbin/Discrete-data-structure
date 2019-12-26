package com.company.PrintableCollection;

import com.company.MyComponent.MyFrame;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MyQueue<T> extends LinkedList<T> implements PrintableCollection{
    private JPanel panel=new JPanel(new FlowLayout());
    private LinkedList<JButton> buttonList =new LinkedList<>();


    final private static int HEIGHT = 30;
    final private static int WIDTH  = 80;

    protected int sleepTime=0;//未完成

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }
    public int getSleepTime() {
        return sleepTime;
    }

    public MyQueue(){
        super();
        MyFrame.getFrame().addList(panel);
    }

    public boolean add(T data){
        super.add(data);
        JButton button=new JButton(data.toString());
        button.setSize(WIDTH,HEIGHT);
        button.setBackground(Color.cyan);
        if(buttonList.size()>=2){buttonList.getLast().setBackground(Color.lightGray);}
        if(buttonList.size()==0){button.setBackground(Color.pink);}
        buttonList.add(button);
        panel.add(button);
        panel.updateUI();
        return true;
    }

    public T removeFirst(){
        T res = super.removeFirst();
        panel.remove(  buttonList.removeFirst() );
        if(!buttonList.isEmpty()){buttonList.getFirst().setBackground(Color.pink);}
        panel.updateUI();
        return res;
    }
    public void setNotUseful() {
        MyFrame.getFrame().removeList(panel);
    }

    @Override
    public void clear() {
        while (!isEmpty()){
            removeFirst();
            try {
                Thread.sleep(Math.min(15,sleepTime));
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
