package com.company.testNode;

import com.company.PrintableNode.*;

public class TestNode02 extends PrintableNodeOfGraph {
    TestNode02 next;
    String data;
    TestNode02(int data){
        this.data=String.valueOf(data);
    }
    TestNode02(String data){
        this.data=data;
    }

    protected void _Who_are_your_children() {
        My_children_are(next);
    }


    public String toString() {
        return data;
    }









}
