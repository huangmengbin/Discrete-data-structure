package com.company.AdvancedSearchTree;

import com.company.MyComponent.MyLine;
import com.company.PrintableNode.PrintableNodeOfTreeMultiWay;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class BTNode extends PrintableNodeOfTreeMultiWay {
    BTNode father = null;
    private BTNode zeroth;
    private ArrayList<MyPair> pairs = new ArrayList<>();//阶数太大的话，可能要考虑效率的问题
    private final int emptyWidth = 4;


    int searchFirstLarger(int number){
        for(int i=0;i<pairs.size();i++){
            if(pairs.get(i).key > number){
                return i;
            }
        }
        return pairs.size();
    }
    int size(){
        return pairs.size()+1;
    }
    void insert(int key){
        this.insert(key, (BTNode) null);
    }
    void insert(int key , BTNode node){//测试用
        int ptr = this.searchFirstLarger(key);
        this.insert( ptr , key , node );
    }
    void insert(int ptr , int key , BTNode node){
        insert( ptr , new MyPair(key,node));
    }
    void insert(int ptr , MyPair pair){
        pairs.add( ptr, pair);
    }

    MyPair remove(int ptr ){
        return this.pairs.remove(ptr);
    }
    void setKey (int ptr,int key){
        pairs.get(ptr).key = key;
    }
    void setNode(int ptr,BTNode node){
        if(ptr==0)  {zeroth=node;}
        else { pairs.get(ptr-1).node = node;}
    }

    int getKey(int number){
        return pairs.get(number).key;
    }
    BTNode getNode(int number){
        if(number==0){return zeroth;}
        else {return pairs.get(number-1).node;}
    }

    void setKeyColor(int ptr,Color color){
        getButton().keyButtons.get(ptr).setBackground(color);
    }

    void setNodeColor(int ptr,Color color){
        if(ptr>0){
            getButton().jButtons.get(ptr-1).setBackground(color);
        }
        else {
            getButton().setBackgroundOnce(color);
        }
    }
    boolean isLeaf(){//原理是叶子都在同一层
        return getNode(0)==null;
    }


    @Override
    protected String toNodeString() {
        return "";
    }
    @Override
    protected int _height() {
        return 20;
    }
    @Override
    protected int _verticalDistance(){//可被覆盖，指的是相邻按钮的垂直距离
        return 67;
    }

    @Override
    protected int _basicWidth() {
        return 33;
    }

    @Override
    protected int _verticalStart() {
        return 20;
    }

    @Override
    protected int _charWidth() {
        return 8;
    }

    public BTNode(){}
    public BTNode(int...ints){
        zeroth = null;
        for(int i:ints){
            pairs.add(new MyPair(i,null));
        }
    }
    @Override
    protected void _Who_are_your_children() {
        ArrayList<BTNode> arrayList = new ArrayList<>(pairs.size()+1);
        arrayList.add(zeroth);
        for(MyPair p: pairs){
            arrayList.add(p.getNode());
        }
        My_Children_are_in(arrayList);
    }

    @Override
    protected int calculateWidth() {
        int result = 0;
        result += emptyWidth * (pairs.size()+1);
        for(MyPair pair : pairs){
            result += _basicWidth()+String.valueOf(pair.getKey()).length()* _charWidth();
        }
        return result;
    }

    @Override
    protected int drawButtonAndLine(int Y, JPanel panel) {

        int result = Y;
        this.setButton(new B_TreeButton());
        repaintButton();//设置颜色

        getButton().setBounds(xPosition,Y, width, _height());
        panel.add(getButton());
        for(JButton button:getButton().keyButtons){
            panel.add(button);
        }
        for (JButton button:getButton().jButtons){
            panel.add(button);
        }

        Y = Y + _height()+ _verticalDistance();

        int X = this.xPosition; //下面画线条
        if(zeroth!=null){
            panel.add(new MyLine( xPosition + emptyWidth / 2 , Y - _verticalDistance() ,
                             zeroth.xPosition+zeroth.width/2 ,      Y));
            result=Math.max(zeroth.drawButtonAndLine(Y, panel),result);
        }
        int i=0;
        for(MyPair pair:pairs){
            if(pair.getNode()!=null){
                panel.add(new MyLine(getButton().jButtons.get(i).getX()+emptyWidth/2 , Y-_verticalDistance(),
                                    pair.getNode().xPosition+pair.getNode().width/2,      Y));
                result=Math.max(pair.getNode().drawButtonAndLine(Y, panel),result);
            }
            ++i;
        }

        return result;
    }

    @Override
    public B_TreeButton getButton() {
        return (B_TreeButton) super.getButton();
    }

    @Override
    public void setButton(JButton button) {
        super.setButton(button);
    }

    private class B_TreeButton extends JButton{
        private LinkedList<JButton> keyButtons ;
        private ArrayList<JButton>   jButtons  ;

        B_TreeButton(){
            this.setText("");
        }

        private void setBackgroundOnce (Color bg){
            super.setBackground(bg);
        }
        @Override
        public void setBackground(Color bg) {
            if(keyButtons==null&jButtons==null){
                keyButtons = new LinkedList<>();
                jButtons   = new ArrayList<>();
                for(MyPair p : pairs){
                    keyButtons. add(new JButton( String.valueOf(p.getKey())));
                    jButtons.   add(new JButton(""));
                }
            }

            super.setBackground(bg);
            for(JButton button: keyButtons){
                button.setBackground(bg);
            }
            for(JButton button: jButtons){
                button.setBackground(bg);
            }
        }

        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, emptyWidth, height);
            ListIterator<MyPair> p = pairs.listIterator(0);
            ListIterator<JButton> keyButton = keyButtons.listIterator(0);
            ListIterator<JButton> jButton   =   jButtons.listIterator(0);

            int newX = x + emptyWidth;
            while (p.hasNext()){
                final int newWidth  =  String.valueOf(p.next().getKey()).length()*_charWidth() + _basicWidth();
                keyButton.next().setBounds(newX,y,newWidth,height);
                newX += newWidth;
                jButton.next().setBounds(newX,y,emptyWidth,height);
                newX += emptyWidth;
            }
            if(newX-x !=width)throw new RuntimeException("x="+(newX-x)+";width="+width);
        }
    }

    private class MyPair {
        private int key;
        private BTNode node;
        int getKey(){
            return key;
        }
        BTNode getNode(){
            return node;
        }
        void setKey(int key){
            this.key = key;
        }
        void setNode(BTNode value){
            this.node = value;
        }
        MyPair(){}
        MyPair(Integer key, BTNode value){
            this.key = key;
            this.node = value;
        }

    }

}