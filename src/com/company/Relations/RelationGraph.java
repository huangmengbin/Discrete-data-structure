package com.company.Relations;

import com.company.Relations.specialRelation.*;
import com.company.Relations.specialRelation.SpecialRelation;
import com.company.MyComponent.MyFrame;
import com.company.MyComponent.MyLine;
import com.company.MyComponent.MyVector;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class RelationGraph<T> {

    private RelationSet<T> relationSet ;
    RelationSet<T> getRelationSet() {
        return relationSet;
    }

    public RelationGraph(Collection<T> collection , Relation<T> relation){this(collection,relation,RelationProperty.common);}
    public RelationGraph(Collection<T> collection , Relation<T> relation, RelationProperty property ){
        this.relationSet = new RelationSet<>(collection , relation);
        relationSet.initial(property);
    }
    public RelationGraph(SpecialRelation<T> r){this(r,RelationProperty.common);}
    public RelationGraph(SpecialRelation<T> r, RelationProperty property){
        this.relationSet = new RelationSet<>(r.getCollection(),r.getRelation());
        relationSet.initial(property);
    }
    public RelationGraph(MyMap<T> myMap){this(myMap,RelationProperty.common);}
    public RelationGraph(MyMap<T> myMap, RelationProperty property){
        this.relationSet = new RelationSet<>(myMap);
        relationSet.initial(property);
    }

    public JPanel updateUI(){
        return updateUI(new JPanel());
    }
    public JPanel updateUI(JPanel panel){
        panel.setLayout(new FlowLayout());
        for(int i=0;i<relationSet.getSize();i++){
            ArrayList<RelationGraphNode<T>> arrayList = relationSet.getGraphNodes (i);
            if(relationSet.getPropertyOfGraph(i)==RelationProperty.order){
                panel.add(printHasseDiagram(arrayList));
            }
            else{
                panel.add(printCommonDiagram(arrayList , relationSet.getPropertyOfGraph(i)));
            }
        }
        return panel;
    }
    private JPanel printHasseDiagram(ArrayList<RelationGraphNode<T>> arrayList){ //arrayList 中含有一些null作为不同链分隔的标记

        JPanel result = new JPanel(null);
        int maxHeight = 0;
        int maxWidth  = 0;
        int x=_horizonStart();
        ArrayList<RelationGraphNode<T>> currentList=new ArrayList<>();

        for(RelationGraphNode<T> node:arrayList){
            if(node!=null){
                currentList.add(node);
                maxWidth=Math.max(maxWidth,node.toString().length()*_charWidth()+_basicWidth());
            }
            else { //遍历完一条链，统一处理
                for(RelationGraphNode<T> currentNode : currentList){
                    int y=getY(currentNode);
                    maxHeight = Math.max(maxHeight,y);
                    currentNode.getButton().setLoop(relationSet.getRelation().relative(currentNode.data,currentNode.data));
                    currentNode.getButton().setBounds(x,y,maxWidth,_buttonHeight());
                    currentNode.getButton().setText(currentNode.toString());
                    result.add(currentNode.getButton());
                }
                x = x + maxWidth + _horizonSpace();
                currentList.clear();
                maxWidth=0;
            }
        }
        for(RelationGraphNode<T> currentNode:arrayList) {
            if(currentNode!=null) {
                for (RelationGraphNode nextNode : currentNode.next) {
                    result.add(new MyLine(currentNode.getButton().getX() + currentNode.getButton().getWidth() / 2, currentNode.getButton().getY(),
                            nextNode.getButton().getX() + nextNode.getButton().getWidth() / 2, nextNode.getButton().getY() + _buttonHeight(),new Color(0,0,167)));
                }
            }
        }
        result.setPreferredSize(new Dimension(x,maxHeight+_buttonHeight()+_verticalSpace()));
        return result;
    }

    private JPanel printCommonDiagram(ArrayList<RelationGraphNode<T>> list,RelationProperty property){
        int maxWIDTH = 0 ;
        for(RelationGraphNode<T>node:list){
            maxWIDTH = Math.max(maxWIDTH,node.toString().length()*_charWidth()+_basicWidth());
        }
        JPanel resultPane = new JPanel(null);
        final int N =list.size();
        final int R = _R(N);
        final int centerY =  R + _verticalStart();
        final int centerX =  R + _horizonStart() + maxWIDTH;
        if(property==RelationProperty.equivalence){
            JButton button = new JButton("equivalence");
            button.setBounds(centerX-60,0,120,27);
            button.setBackground(new Color(200,255,200));
            resultPane.add(button);
        }

        for(RelationGraphNode<T>node:list){
            node.getButton().setLoop(relationSet.getRelation().relative(node.data,node.data));
            node.getButton().setText(node.toString());
        }
        for(int i=0;i<N;i++) {
            final double alpha = i * _2PI / N;
            final double deltaY = R * Math.cos(alpha);
            final double deltaX = R * Math.sin(alpha);
            list.get(i).getButton().setBounds((int) (centerX + deltaX),(int) (centerY - deltaY),
                    list.get(i).toString().length()*_charWidth()+_basicWidth(),_buttonHeight());
        }

        for(RelationGraphNode<T>node:list){ //画线
            for (RelationGraphNode<T> nextNode : node.next) {
                if (nextNode != node) {
                    if(RelationProperty.common == property) {
                        resultPane.add(new MyVector(node.getButton().getX(), node.getButton().getY(),
                                nextNode.getButton().getX(), nextNode.getButton().getY()));
                    }
                    else {
                        resultPane.add(new MyVector(node.getButton().getX(), node.getButton().getY(),
                                nextNode.getButton().getX(), nextNode.getButton().getY() ,0,Color.gray));
                    }
                }
            }
        }

        for(int i=0;i<N;i++) {
            final JButton BUTTON = list.get(i).getButton();
            final int WIDTH = BUTTON.getWidth();
            final int X = BUTTON.getX();
            final int Y = BUTTON.getY();
            if(i==0){//+y轴
                BUTTON.setBounds(X-WIDTH/2,    Y-_buttonHeight() ,    WIDTH,_buttonHeight());
            }
            else if(i*4<N){//1象限,+x轴
                BUTTON.setBounds(X,            Y-_buttonHeight()   ,      WIDTH,_buttonHeight());
            }
            else if(i*4==N){
                BUTTON.setBounds(X,            Y-_buttonHeight()/2   ,      WIDTH,_buttonHeight());
            }
            else if(i*2<N){//4象限
                BUTTON.setBounds(X,               Y,                         WIDTH,_buttonHeight());
            }
            else if(i*2==N){//-y轴
                BUTTON.setBounds(X-WIDTH/2 ,   Y,                       WIDTH,_buttonHeight());
            }
            else if(i*4<N*3){//3象限
                BUTTON.setBounds(X-WIDTH,      Y,                         WIDTH,_buttonHeight());
            }
            else if(i*4==N*3){//-x轴
                BUTTON.setBounds(X-WIDTH,   Y-_buttonHeight()/2,        WIDTH,_buttonHeight());
            }
            else{//2象限
                BUTTON.setBounds(X-WIDTH,      Y-_buttonHeight(),     WIDTH,_buttonHeight());
            }

            resultPane.add(BUTTON);
        }
        resultPane.setPreferredSize(new Dimension(centerX+R+maxWIDTH+1,centerY+R+_buttonHeight()+1));
        return resultPane;
    }

    final static private double _2PI = Math.PI*2;
    private int getY(RelationGraphNode node){//
        return _verticalStart()+node.depth*(_verticalSpace()+_buttonHeight());
    }
    private int _horizonStart(){//可被覆盖
        return 30;
    }
    private int _verticalStart(){//可被覆盖
        return _buttonHeight()+67;
    }
    private int _horizonSpace(){return 10;}
    private int _verticalSpace(){return 80;}
    private int _buttonHeight(){return 50;}
    private int _charWidth(){//可被覆盖，指的是每增加一个字符后，按钮所增加的宽度
        return 14;
    }
    private int _basicWidth(){//可被覆盖，指的是按钮的基本宽度
        return 35;
    }
    private int _multiple(){//倍数
        return 20;
    }
    private int _R(int number){//传入总顶点数目number ，返回半径
        return number * _multiple();
    }

    public static void main(String[] _67666 ){
        test04();
        while (true){MyFrame.getFrame().updateUI();}
    }

    private static void test01(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(int i=-16;i<=56;++i) {
            arrayList.add(i);
        }
        RelationGraph<Integer> hmb = new RelationGraph<>(arrayList,(a,b)->a%2==b%2);
        JPanel panel=new JPanel();
        MyFrame.getFrame().addGraph(hmb.updateUI(panel));
    }
    private static void test02(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(int i=-10;i<=9;++i) {
            arrayList.add(i);
        }
        arrayList.add(-12);
        RelationGraph<Integer> hmb = new RelationGraph<>(arrayList ,(first, second) -> first<=second&&(first%2==second%2||Math.signum(first)<Math.signum(second)) );
        MyFrame.getFrame().addGraph(hmb.updateUI() );
    }
    private static void test03(){
        long t1 = System.currentTimeMillis();
        RelationGraph<Long> hmb = new RelationGraph<>(new D(121*3602177082000000L),RelationProperty.order);
        System.out.println("time: "+(System.currentTimeMillis()-t1)+" ms");
        MyFrame.getFrame().addGraph(hmb.updateUI());
        System.out.println("time: "+(System.currentTimeMillis()-t1)+" ms");
    }
    private static void test04(){
        RelationGraph<String> hmb = new RelationGraph<>(new B(4));
        MyFrame.getFrame().addGraph(hmb.updateUI());
    }
    private static void test05(){
        MyFrame.getFrame().addGraph((new RelationGraph<>(new PowerSet("67666","rtw666","HL666"))).updateUI());
    }
    private static void test06(){
        MyMap<Integer> myMap =  new MyMap<>();
        myMap.add(1,2,3,4,5,6,7,8);
        myMap.add(2,4,5,6,7,8);
        myMap.add(3,4,5,6,7,8);
        myMap.add(4,5,6,7,8);
        myMap.add(5,7,8);
        myMap.add(6,7,8);
        myMap.add(7,8);
        myMap.makeReflexive();
        MyFrame.getFrame().addGraph((new RelationGraph<>(myMap)).updateUI());
    }
    private static void test07(){
        MyMap<Integer> myMap =  new MyMap<>();
        myMap.add(1,2,3);
        myMap.add(2,4);
        myMap.add(3,4);
        myMap.add(4,5,6);
        myMap.add(5,7);
        myMap.add(6,7);
        myMap.add(7,8);
        myMap.add(8,1);
        myMap.makeReflexive();
        MyFrame.getFrame().addGraph((new RelationGraph<>(myMap)).updateUI());
    }

}
