package com.company.PrintableCollection;
import com.company.PrintableNode.PrintableNodeOfTree;

import java.awt.Color;

public class NodeStack<T extends PrintableNodeOfTree> extends MyStack<T> {
    public NodeStack(){
        super();
    }
    public NodeStack(int sleepTime){
        super();
        setSleepTime(sleepTime);
    }

    @Override
    public T push(T node) {
        try {

            node.getButton().setBackground (Color.GRAY);
            if(!isEmpty())peek().getButton().setBackground(Color.LIGHT_GRAY);
            Thread.sleep(sleepTime);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        return super.push(node);
    }

    @Override
    public T pop() {
        try {
            peek().getButton().setBackground(Color.YELLOW);
            Thread.sleep(sleepTime);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        return super.pop();
    }
}
