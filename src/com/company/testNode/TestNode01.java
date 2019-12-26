package com.company.testNode;

import com.company.PrintableNode.*;

class TestNode01 extends PrintableNodeOfTreeMultiWay {
    TestNode01 lhs;
    TestNode01 rhs;
    String data;
    TestNode01(int data){
        this.data=String.valueOf(data);
    }
    TestNode01(String data){
        this.data=data;
    }



    protected void _Who_are_your_children() {
        My_children_are(lhs,rhs);
    }

    public String toString() {//最好重写toString()，或者直接重写toNodeString()
        return data;
    }










    @Override
    protected int _horizonStart() {
        return 500;
    }

    @Override
    protected int _verticalStart() {
        return 200;
    }
}
