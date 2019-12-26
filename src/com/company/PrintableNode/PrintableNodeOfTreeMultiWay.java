package com.company.PrintableNode;

import java.util.Collection;

public abstract class PrintableNodeOfTreeMultiWay extends PrintableNodeOfTree {

    //get your subTree:

    final protected void My_children_are(PrintableNodeOfTree... children){
        this.childrenList.clear();
        for(PrintableNodeOfTree child:children){
            if(child!=null){            //使得childrenList内没有空指针
                if(! (child instanceof PrintableNodeOfTreeMultiWay)){
                    throw new RuntimeException();
                }
                this.childrenList.add(child);
            }
        }
    }
    final protected void My_Children_are_in(Collection<?extends PrintableNodeOfTree> children){
        childrenList.clear();
        for(PrintableNodeOfTree child:children){
            if(child!=null){
                if(! (child instanceof PrintableNodeOfTreeMultiWay)){
                    throw new RuntimeException();
                }
                childrenList.add(child);
            }
        }
    }


    //func:

    private int calculateSpace(){//返回space,同时顺便计算length。进行之前要不断setChildrenlist();
        this.width = calculateWidth();
        this.space=0;
        for(PrintableNodeOfTree child: childrenList){
            space+=((PrintableNodeOfTreeMultiWay)child).calculateSpace(); //childrenList没有空指针
        }
        this.space = Math.max(space,   width + _horizontalDistance()  );
        return space;
    }

    private void calculatePosition(int X){
        this.xPosition =(space- width)/2 + X;
        for(PrintableNodeOfTree child:childrenList){
            ((PrintableNodeOfTreeMultiWay)child).calculatePosition(X);
            X+=child.space;
        }
    }

    @Override
    void calculate(){//
        this.calculateSpace();
        this.calculatePosition( _horizonStart() );
    }

}
