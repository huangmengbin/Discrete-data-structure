package com.company.MyComponent;

import javax.management.RuntimeErrorException;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MyFrame extends JFrame{
    private final static Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int width=dimension.width,height=dimension.height*19/20;
    private static final Color backgroundColor=Color.pink;
    private JPanel bigPanel=new JPanel(null);//<-----------------
    public static final int RUNNING=0,PULSE=1, STEP_IN =2;
    private static int currentState = RUNNING;
    private static final JButton controlButton=new JButton("II");
    private static final JButton nextStepButton=new JButton(">>");
    public static void setCurrentState(int number){
        if (number != RUNNING && number != PULSE && number != STEP_IN)
            throw new RuntimeException();

        synchronized (getFrame()) {
            currentState = number;
            if (number == RUNNING) {
                controlButton.setText("II");
                nextStepButton.setEnabled(false);
            } else if (number == PULSE) {
                controlButton.setText("▸");
                nextStepButton.setEnabled(true);
            } else {
                controlButton.setText("▸");
                nextStepButton.setEnabled(true);
            }
            getFrame().notify();
        }

    }

    private void addControlButton(){
        controlButton.setFocusPainted(false);
        nextStepButton.setFocusPainted(false);
        controlButton.setBounds( width/500,height/67,width/40,height/25);
        controlButton.addActionListener(e-> {if(currentState==RUNNING)setCurrentState(PULSE);else setCurrentState(RUNNING);});
        nextStepButton.setBounds(width/500,height/17,width/40,height/35);
        nextStepButton.addActionListener(e->setCurrentState(STEP_IN));
        bigPanel.add(controlButton);
        nextStepButton.setEnabled(false);
        bigPanel.add(nextStepButton);
    }

    public static int getCurrentState() {
        return currentState;
    }
    public static void enablePulse(){
        synchronized (getFrame()){
            while (currentState==PULSE){
                try {
                    getFrame().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(currentState==STEP_IN){
                setCurrentState(PULSE);
            }
        }
    }
    private static MyFrame frame = new MyFrame();//  <<----------[[[[[[[[[[[[[[[[[[[[[[[[[

    public static MyFrame getFrame(){return frame;}
    private MyFrame(){
        createUI();
    }

    public void updateUI() {
        for(JPanel panel:graphPanelArrayList)panel.updateUI();
    }

    private ArrayList<JPanel> graphPanelArrayList = new ArrayList<>();
    private ArrayList<JPanel> stackPanelArrayList = new ArrayList<>();
    private ArrayList<JPanel> listPanelArrayList  = new ArrayList<>();

    private JPanel graphPanel =new JPanel();
    private Box stackBox= Box.createHorizontalBox();
    private Box listBox = Box.createVerticalBox();
    private JScrollPane stackScroll;
    private JScrollPane listScroll;



    private void createUI(){
        this.setTitle("离散树据结构");
        this.setBounds(0,0,width,height);
        setLayout(new BorderLayout());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点 X 关闭所有程序

        addControlButton();
        bigPanel.setBackground(backgroundColor);
        add(bigPanel);

        JScrollPane graphScroll = new JScrollPane(graphPanel);
        graphScroll.setBounds(width/33,height/8,width*8/9,height*9/11);
        graphScroll.createHorizontalScrollBar();
        graphScroll.createVerticalScrollBar();
        graphPanel.setLayout(new FlowLayout());//<-----------------

        JPanel stackPanel =new JPanel();
        stackScroll = new JScrollPane(stackPanel);
        stackScroll.setBounds(width*62/67,height/100,width/16,height*94/100);
        //width*62/67,height/100,width/16,height*94/100
        //width*26/67,height/7,width/4,height*7/9
        stackScroll.createHorizontalScrollBar();
        stackScroll.createVerticalScrollBar();
        stackPanel.setLayout(new FlowLayout());//<-----------------
        stackPanel.add(stackBox);

        JPanel listPanel  =new JPanel();
        listScroll = new JScrollPane(listPanel);
        listScroll.setBounds(width/33,height/100,width*8/9,height*12/110);
        listScroll.createVerticalScrollBar();
        listScroll.createHorizontalScrollBar();

        listPanel.setLayout(new FlowLayout());//<-----------------
        listPanel.add(listBox);

        bigPanel.add(graphScroll);
        bigPanel.add(stackScroll);
        bigPanel.add(listScroll);
        bigPanel.updateUI();
    }

    /**
     graphPanel.add(panel);
     graphPanelArrayList.add(panel);
     graphPanel.updateUI();*/
    public void addGraph(JPanel panel) {
        addPanel(graphPanel,graphPanelArrayList,panel);
    }
    public void removeGraph(JPanel panel){
        int ptr=graphPanelArrayList.indexOf(panel);
        if(ptr>=0)removePanel(graphPanel,graphPanelArrayList,ptr);
    }
    /**
     JPanel panel = graphPanelArrayList.get(graphPanelArrayList.size()-1);
     graphPanel.remove(panel);
     graphPanelArrayList.remove(graphPanelArrayList.size()-1);
     panel.removeAll();
     graphPanel.updateUI();*/
    public void removeLastGraph(){
        removePanel(graphPanel,graphPanelArrayList,graphPanelArrayList.size()-1);
    }

    public void addStack(JPanel panel){
        addPanel(stackBox,stackPanelArrayList,panel);
        stackScroll.getViewport().setViewPosition(new Point(99999999, 9999999));//输出框自动滚到最底部
    }
    public void removeStack(JPanel panel){
        int ptr=stackPanelArrayList.indexOf(panel);
        if(ptr>=0)removePanel(stackBox,stackPanelArrayList,ptr);
    }
    public void addList(JPanel panel){
        addPanel(listBox,listPanelArrayList,panel);
    }
    public void removeList(JPanel panel){
        int ptr=listPanelArrayList.indexOf(panel);
        if(ptr>=0)removePanel(listBox,listPanelArrayList,ptr);
    }

    private void addPanel(JComponent old,ArrayList<JPanel> arrayList,JPanel addPane ){
        old.add(addPane);
        arrayList.add(addPane);
        old.updateUI();
    }
    private void removePanel(JComponent old,ArrayList<JPanel> arrayList,int ptr){
        old.remove(arrayList.get(ptr));
        arrayList.get(ptr).removeAll();
        arrayList.remove(ptr);
        old.updateUI();
    }
}
