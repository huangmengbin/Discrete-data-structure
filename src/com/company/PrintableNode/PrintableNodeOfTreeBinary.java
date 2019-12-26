package com.company.PrintableNode;


import com.company.MyComponent.MyFrame;
import com.company.PrintableCollection.NodeStack;

import java.util.Collection;
import java.util.function.Consumer;

public abstract class PrintableNodeOfTreeBinary extends PrintableNodeOfTree {


    final protected void My_children_are(PrintableNodeOfTree... children){ //即使null也加入，为了占位以区分左右节点
        this.childrenList.clear();
        if(children.length != 2){throw new ArrayIndexOutOfBoundsException();}
        for(PrintableNodeOfTree child:children){
            if(child!=null  &&  !(child instanceof PrintableNodeOfTreeBinary)){
                throw new RuntimeException();
            }
            childrenList.add(child);
        }
    }
    final protected void My_Children_are_in(Collection<?extends PrintableNodeOfTree> children){//即使null也加入，为了占位以区分左右节点
        this.childrenList.clear();
        if(children.size()!=2){throw new RuntimeException();}
        for(PrintableNodeOfTree child:children){
            if(child!=null  &&  !(child instanceof PrintableNodeOfTreeBinary)){
                throw new RuntimeException();
            }
            childrenList.add(child);
        }
    }

    //func:

    private int calculateSpace(){//返回space,同时顺便计算length。//////////////////////////////////
        this.width = calculateWidth();
        this.space=0;
        for(PrintableNodeOfTree child: childrenList){
            if(child!=null){ ////可能会空指针,那就相当于加零吧
                space+=((PrintableNodeOfTreeBinary)child).calculateSpace();
            }
        }
        this.space = space  +   width + _horizontalDistance() ;
        return space;
    }

    private void calculatePosition(int X){
        this.xPosition =_horizontalDistance()/2 + X;//父节点不能再居中了
        if(childrenList.size()!=0  && childrenList.get(0)!=null){
            xPosition +=childrenList.get(0).space;
        }
        for(PrintableNodeOfTree child:childrenList){
            if(child!=null) { ////可能会空指针
                ((PrintableNodeOfTreeBinary) child).calculatePosition(X);
                X += child.space;
            }
            X += (this._horizontalDistance()+this.width);//右节点位置：必须要加上父节点的宽度
        }
    }

    void calculate(){//
        this.calculateSpace();
        this.calculatePosition( _horizonStart() );
    }

    //<----------------------------------以下为遍历

    PrintableNodeOfTreeBinary lhs(){
        return (PrintableNodeOfTreeBinary) childrenList.getFirst();
    }
    PrintableNodeOfTreeBinary rhs(){
        return (PrintableNodeOfTreeBinary) childrenList.getLast();
    }
    private boolean isLeaf(){
        return lhs()==null&&rhs()==null;
    }

    private static int sleepTime = 200;

    public void preOrder(Consumer<PrintableNodeOfTreeBinary> consumer){

        NodeStack<PrintableNodeOfTreeBinary> stack = new NodeStack<>(sleepTime);

        PrintableNodeOfTreeBinary node =this;//----------------------------------------->初始化已经完成，栈内什么都没有
        while ( true ){
            MyFrame.enablePulse();
            consumer.accept(node);//<--------------------------------twinkle
            MyFrame.enablePulse();
            if (node.rhs() != null && node.lhs()!=null) {stack.push(node.rhs());node=node.lhs();}//左右均非空，才有必要将右节点压栈
            else if (node.rhs() != null) node= node.rhs();//左空 右不空
            else if (node.lhs() != null) node= node.lhs();//右空 左不空
            else if( ! stack.isEmpty()) node=stack.pop();//左右均空，欲图获取栈顶的元素
            else break;//获取失败 <--> 所有遍历已经完成
        }
        stack.setNotUseful();//所以就return了
    }

    public void inOrder(Consumer<PrintableNodeOfTreeBinary> consumer){

        NodeStack<PrintableNodeOfTreeBinary> stack = new NodeStack<>(sleepTime);
        PrintableNodeOfTreeBinary node =this ;//----------------------------------------->初始化已经完成，栈内什么都没有

        while (true){

            while (node.lhs()!=null ) {
                MyFrame.enablePulse();
                stack.push(node);node=node.lhs();
            }//只要左非空，就有必要将自己压栈,再往左边跑，直至左边触底

            while (true){
                MyFrame.enablePulse();
                consumer.accept(node);//左边已经为空了.注意 node 并不在栈中 //<----------------twinkle
                MyFrame.enablePulse();
                if(node.rhs()!=null)          {node=node.rhs();     break;}//左空右非空，此时可以一直往左跑
                else if( ! stack.isEmpty()) {node=stack.pop();}//左空右空，弹栈并获取之。说明它左边已经被访问，就应该跳过一个劲往左跑的过程
                else {  stack.setNotUseful(); return; }// 获取栈顶失败，return 了
            }//end while
        }//end while

    }

    public void postOrder(Consumer<PrintableNodeOfTreeBinary> consumer){
        if(this.isLeaf()){consumer.accept(this);return;}
        NodeStack<PrintableNodeOfTreeBinary> stack = new NodeStack<>(sleepTime);
        stack.push(this);
        PrintableNodeOfTreeBinary node = this ;//----------------------------------------->初始化已经完成，栈内存放了根节点
        PrintableNodeOfTreeBinary tmp = this;                          //用于暂存node

        while (true){
            MyFrame.enablePulse();
            if(tmp==node.lhs() || tmp==node.rhs() || node.isLeaf() ){ //从儿子节点处回来，可断定子树都已经访问完；或者这里就是叶子
                consumer.accept(node);
                MyFrame.enablePulse();
                if(stack.isEmpty()){stack.setNotUseful(); return;}                      //return
                else {                                                           //栈非空不允许return
                    tmp  = node;
                    if(stack.peek().rhs()==node||stack.peek().lhs()==node||stack.peek().isLeaf()) { //即将回到父节点或一个叶子
                        node = stack.pop();
                    }
                    else { //即将回到非叶子的弟弟节点
                        node = stack.peek();
                    }
                }
            }
            else{                                   //来源为父节点或长兄节点 ，并且此处不是叶子
                if(node.rhs()!=null&&node.lhs()!=null){//左右都非空
                    if(stack.peek()!=node)stack.push(node);
                    stack.push( node.rhs() );
                    node = node.lhs();
                }
                else if(node.lhs()!=null){//左非空，右空
                    if(stack.peek()!=node)stack.push(node);
                    node = node.lhs();
                }
                else if(node.rhs()!=null){//左空，右非空
                    if(stack.peek()!=node)stack.push(node);
                    node = node.rhs();
                }
                else {
                    throw new RuntimeException();
                }
            }
        }
    }

    /**
    @postOrder
    while ( true ) {
        if ( node.lhs()==null && node.rhs()==null) {//if 左右均空,即叶子
            do {
                consumer.accept(node);//<--------------------------------------------- twinkle
                temp=stack.pop();//栈被pop了.  被pop的东西正是node ; tmp暂时存储一下node
                if( ! stack.isEmpty() )  node=stack.peek();  //node被修改。。不再是原来的那个了 ； node可能回到父节点，可能到他弟弟
                else       {  stack.setNotUseful(); return; }//说明刚刚根节点被pop了
            }while (node.lhs()==temp || node.rhs()==temp);//说明回到的是：父节点。所以：继续循环，pop。 （否则就有权利去push）
        }
        else{
            if(node.rhs()!=null)stack.push(node.rhs());
            if(node.lhs()!=null)stack.push(node.lhs());
            node=stack.peek();
        }
    }
    */

    public enum Way{
        preOrder,
        inOrder,
        postOrder,
    }

    public void traverse(Way traverseWay,Consumer<PrintableNodeOfTreeBinary> consumer){

        NodeStack<PrintableNodeOfTreeBinary> stack = new NodeStack<>(sleepTime);
        stack.setLastVisit(this);////////////////嘿嘿嘿
        stack.push(this);//初始化完成

        while (!stack.isEmpty()){//loop
            MyFrame.enablePulse();
            PrintableNodeOfTreeBinary node = stack.peek();
            if(stack.getLastVisit()!=node.lhs() && stack.getLastVisit()!=node.rhs()){// <---不是从左右节点回来 <等价于> 从父节点过来
                if(traverseWay == Way. preOrder )   consumer.accept(node);   //@先序
                if(node.lhs()!=null){                                        //  <--欲图遍历左树
                    stack.push(node.lhs());                          //  <--左树非空，允许遍历左树，则将其压栈
                    continue;                                              //  <--重新循环，下次循环将会从左树出发，遍历成功
                }
            }

            if(stack.getLastVisit()!=node.rhs()){// <---不是从右节点回来 <等价于> 从左节点过来，或 从父节点过来但左节点为空 <等价于> 已经遍历完左子树或者遍历左子树失败 <等价于> 不再需要遍历其左子树
                if(traverseWay == Way.  inOrder)    consumer.accept(node);   //@中序
                if(node.rhs()!=null){                                         //  <--欲图遍历右树
                    stack.push(node.rhs());                           //  <--右树非空，允许遍历右树，则将其压栈
                    continue;                                               //  <--重新循环，下次循环将会从右树出发，遍历成功
                }
            }
            //已经遍历完右树，或者遍历右树失败<等价于>不再需要遍历其右子树<等价于>不再需要遍历其左右子树
            if(traverseWay == Way.  postOrder  )    consumer.accept(node);   //@后序
            stack.pop();//此时自身必定已完成遍历，则将其完全退出
        }//end while
        stack.setNotUseful();
    }
}