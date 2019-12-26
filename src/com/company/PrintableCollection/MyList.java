package com.company.PrintableCollection;

import com.company.MyComponent.MyFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class MyList <T>  extends ArrayList <T> implements PrintableCollection{

    final private static int HEIGHT = 24;
    final private static int WIDTH  = 80;

    private JPanel panel=new JPanel(new FlowLayout());
    private ArrayList<JButton> buttonList =new ArrayList<>();
    private int sleepTime = 0 ;

    public void setSleepTime(int sleepTime) {
        if(sleepTime>=0)this.sleepTime = sleepTime;
    }
    public int getSleepTime() {
        return sleepTime;
    }

    public MyList() {
        super();
        MyFrame.getFrame().addList(panel);
    }
    @Override
    public boolean add(T data){
        super.add(data);
        JButton button=new JButton();
        if(data!=null)button.setText(data.toString());
        else button.setText("Ø");
        button.setSize(WIDTH,HEIGHT);
        button.setBackground(Color.YELLOW);
        buttonList.add(button);
        panel.add(button);
        panel.updateUI();
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for(T t:c){
            add(t);
        }
        return c.size()!=0;
    }

    @Override
    public T set(int index, T element) {

        T res = super.set(index, element);
        try {
            Color  color = buttonList.get(index).getBackground();
            buttonList.get(index).setBackground(Color.pink);
            Thread.sleep(getSleepTime());
            buttonList.get(index).setBackground(color);
            buttonList.get(index).setText(element.toString());
        }
        catch (InterruptedException e){e.printStackTrace();}
        return res;
    }

    @Override
    public T remove(int index) {
        T result = super.remove(index);
        JButton button=buttonList.remove(index);
        panel.remove(button);
        panel.updateUI();
        return result;
    }

    @Override
    public T get(int index) {
        return super.get(index);
    }

    @Override
    public void clear() {
        super.clear();
        panel.removeAll();
        buttonList.clear();
        panel.updateUI();
    }

    public void makeTextEmpty(int ptr){
        buttonList.get(ptr).setText("");
    }

    public void setColor(int i,Color color){
        setColor(i,i+1,color);
    }
    public void setColor(int from,int to,Color color){
        for(int i=from;i<to;i++){
            buttonList.get(i).setBackground(color);
        }
    }
    public void twinkle(int from, int to, Color color,int times){
        try {
            Color[] colors = new Color[to - from];
            for (int i = from; i < to; i++) {
                colors[i - from] = buttonList.get(i).getBackground();
            }
            for(int t=0;t<times;t++) {
                for (int i = from; i < to; i++) {
                    buttonList.get(i).setBackground(color);
                }
                Thread.sleep(sleepTime);
                for (int i = from; i < to; i++) {
                    buttonList.get(i).setBackground(colors[i - from]);
                }
                Thread.sleep(sleepTime);
            }
        }
        catch (InterruptedException err){
            err.printStackTrace();
        }
    }
    public void twinkle(int i, Color color,int times){
        twinkle(i,i+1,color,times);
    }
    public void setNotUseful(){
        MyFrame.getFrame().removeList(panel);
    }

}
