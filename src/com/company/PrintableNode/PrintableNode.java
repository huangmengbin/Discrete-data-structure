package com.company.PrintableNode;

import javax.swing.*;
import java.awt.*;

public abstract class PrintableNode implements Printable{

    //abstract

    abstract protected void _Who_are_your_children();//实类中实现，没什么用

    public JPanel updatePanelUI(){return updatePanelUI(new JPanel());}
    abstract public JPanel updatePanelUI(JPanel panel);//由根节点调用

    abstract public void repaintAllButton();//由根节点调用

    public void createUI(){//由根节点调用
        JPanel panel=new JPanel();
        createUI(panel);
    }

    public void createUI(JPanel smallPanel){//由根节点调用
        //smallPanel.setLayout(null); 交给 updatePanelUI 去完成
        JFrame frame=new JFrame();
        JPanel bigPanel=new JPanel(null);
        bigPanel.setBackground(Color.WHITE);
        frame.add(bigPanel);

        //smallPanel.setSize(99999,99999);
        JButton updateButton=new JButton("update"){
            protected void paintBorder(Graphics g) {
                setFocusPainted(false);
            }
        };
        JButton repaintButton=new JButton("repaint"){
            protected void paintBorder(Graphics g) {
                setFocusPainted(false);
            }
        };

        frame.setBounds(0,0,1900,1020);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点 X 关闭所有程序

        JScrollPane scrollPane = new JScrollPane(smallPanel);
        scrollPane.setBounds(40,60,1800,900);
        scrollPane.createHorizontalScrollBar();
        scrollPane.createVerticalScrollBar();

        updateButton. addActionListener( event -> updatePanelUI(smallPanel) );//
        repaintButton.addActionListener( event -> repaintAllButton() );//按钮被按下
        updateButton.setBounds(1080,10,200,45);
        repaintButton.setBounds(560,10,200,45);
        updateButton.setBackground(bigPanel.getBackground());///////////////////////////
        repaintButton.setBackground(bigPanel.getBackground());
        updateButton.setFont(new Font("宋体",Font.BOLD,25));
        repaintButton.setFont(new Font("宋体",Font.BOLD,25));
        bigPanel.add(updateButton);
        bigPanel.add(repaintButton);

        this.updatePanelUI(smallPanel);//updateUI
        bigPanel.add(scrollPane);
    }

    //一些没用的东西



    void paintPointedTo(){
        if(getButton()!=null)getButton().setBackground(Color.pink);
    }
    void paintPointedFrom(){
        getButton().setBackground(Color.green);
    }



}