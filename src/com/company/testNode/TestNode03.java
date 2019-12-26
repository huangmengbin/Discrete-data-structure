package com.company.testNode;
import com.company.PrintableNode.*;

import java.util.ArrayList;

public class TestNode03 extends PrintableNodeOfGraph {

    String data;
    ArrayList<TestNode03> nodeList = new ArrayList<>();//下一个顶点

    TestNode03(String data){this.data=data;}
    TestNode03(int data){this.data=String.valueOf(data);}

    void connect(TestNode03 node){
        nodeList.add(node);
    }



    protected void _Who_are_your_children() {
        My_Children_are_in(nodeList);
    }

    @Override
    protected String toNodeString() {
        return "["+data+"]";
    }

    @Override
    protected int _buttonHeight(){return 67;}
}
