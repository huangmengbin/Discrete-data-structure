package com.company.expression;

import com.company.PrintableNode.PrintableNodeOfTreeBinary;

public class ExpressionNode extends PrintableNodeOfTreeBinary {
    private String data="";
    void setData(String data){this.data=data;}
    void setData(char data)  {setData(String.valueOf(data));}
    void setData(double data){setData(String.valueOf(data));}
    void setData(Object data){setData(String.valueOf(data));}
    private ExpressionNode left=null;
    private ExpressionNode right=null;
    ExpressionNode getLeft(){
        return left;
    }
    ExpressionNode getRight(){
        return right;
    }
    void setLeft(ExpressionNode left){
        this.left =left;
        this.left.father=this;
    }
    void setRight(ExpressionNode right){
        this.right=right;
        this.right.father=this;
    }
    ExpressionNode father=null;

    public ExpressionNode(){    }
    public ExpressionNode(String data){new ExpressionNode();setData(data);}
    public ExpressionNode( char  data){new ExpressionNode();setData(data);}
    public ExpressionNode(double data){new ExpressionNode();setData(data);}
    public ExpressionNode(Object data){new ExpressionNode();setData(data);}

    @Override
    protected void _Who_are_your_children() {
        My_children_are(left,right);
    }
    public String toString(){
        return data;
    }


}
