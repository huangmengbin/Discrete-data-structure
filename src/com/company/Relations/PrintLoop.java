package com.company.Relations;

import com.company.MyComponent.MyFrame;
import com.company.PrintableCollection.MyStack;

import java.util.*;

public class PrintLoop<T> {

    public static void main(String[]a){

        MyMap<Integer> myMap =  new MyMap<>();
        myMap.add(1,3,5);
        myMap.add(2,9,6);
        myMap.add(3,1,4,5,8);
        myMap.add(4,1,3);
        myMap.add(8,1,3,4,5);
        myMap.add(6,9);
        myMap.add(9,2);

        //myMap.makeTransitive();
        RelationGraph<Integer> graph = (new RelationGraph<>(myMap));
        MyFrame.getFrame().addGraph(graph.updateUI());

        System.out.print((new PrintLoop<Integer>()).run(graph));

    }



    private String run (RelationGraph<T> graph){

        Loops<T> loops = new Loops<>();
        final RelationSet<T> relationSet = graph.getRelationSet();
        for(int rtw=0;rtw<relationSet.getSize();rtw++) {
            ArrayList<RelationGraphNode<T>> arrayList = relationSet.getGraphNodes(rtw);

            for (RelationGraphNode<T> node : arrayList) {
                if(node!=null) {
                    node.valid = true;//当前是否有效
                    node.lvalid = true;//上一轮刚刚结束时是否有效
                }
            }

            for (RelationGraphNode<T> node : arrayList) {

                if (node != null && node.lvalid) {

                    MyStack<RelationGraphNode<T>> nodeStack = new MyStack<>();
                    nodeStack.setSleepTime(100);///<<<<<<<<<<<-------------------------
                    nodeStack.push(node);
                    node.valid = false;
                    Stack<Integer> integerStack = new Stack<>();
                    integerStack.push(0);

                    //dfs
                    while (!nodeStack.isEmpty()) {
                        if (nodeStack.peek().next.size() == integerStack.peek()) {//该退栈了
                            integerStack.pop();
                            nodeStack.pop();
                        }
                        else {//还能压栈
                            final RelationGraphNode<T> nextNode = nodeStack.peek().next.get(integerStack.peek());
                            final int index = nodeStack.indexOf(nextNode);
                            integerStack.push(integerStack.pop() + 1);  //.back++;

                            if (index >= 0) {
                                ArrayList<T> loop = new ArrayList<>();
                                for (int j = index; j < nodeStack.size(); j++) {//<=---------
                                    loop.add(nodeStack.get(j).data);
                                    nodeStack.get(j).valid = true;
                                    nodeStack.get(j).lvalid=false;
                                }
                                loops.add(loop);
                            }
                            else if (nextNode.valid ) { //如果前面几轮遍历过nextNode，但nextNode无法走到当前的源节点，则必定无环
                                nodeStack.push(nextNode);
                                nextNode.valid = false;
                                integerStack.push(0);
                            }
                        }
                    }//end dfs


                    for (RelationGraphNode<T> n : arrayList) {
                        if(n!=null) {
                            if (!n.valid) n.lvalid = false;
                            if (!n.lvalid) n.valid = false;
                        }
                    }
                    nodeStack.setNotUseful();
                }

            }
        }

        return loops.toString();
    }

    private class Loops< E >{
        private LinkedList<ArrayList<E>> loops = new LinkedList<>();
        private HashMap<E,Integer>hashMap=new HashMap<>();
        private int number=0;

        private void add(ArrayList<E> arrayList){

            ArrayList<E> loop = toLoop(arrayList);

            boolean shouldAdd = true;
            for(ArrayList<E> oldLoop:loops){
                if(hashMap.get(oldLoop.get(0)).equals(hashMap.get(loop.get(0)))){
                    if(isSameLoop(oldLoop,loop)){
                        shouldAdd = false;
                        break;
                    }
                }
            }

            if(shouldAdd){
                loops.add(loop);
            }
        }

        private ArrayList<E> toLoop (ArrayList<E> loop){

            int index = -1;
            int minValue = Integer.MAX_VALUE;

            for (int i = 0; i < loop.size(); i++) {
                final E e = loop.get(i);
                if (hashMap.get(e) == null) {
                    hashMap.put(e, number++);
                }
                if(hashMap.get(e)<minValue){
                    minValue=hashMap.get(e);
                    index=i;
                }
            }

            ArrayList<E> arrayList = new ArrayList<>(loop.size());
            for(int i=0;i<loop.size();i++){
                arrayList.add(loop.get(index));
                index++;
                if(index==loop.size())index=0;
            }
            return arrayList;
        }
        private boolean isSameLoop(ArrayList<E>oldLoop,ArrayList<E>newLoop){
            if(oldLoop.size()!=newLoop.size())
                return false;
            for(int i=0;i<newLoop.size();i++){
                if(!oldLoop.get(i).equals(newLoop.get(i))){
                    return false;
                }
            }
            return true;
        }
        public String toString(){
            StringBuilder sb=new StringBuilder();
            for(ArrayList<E> loop:loops){
                for(E e:loop){
                    sb.append(e.toString()).append(" -> ");
                }
                sb.append(loop.get(0).toString()).append('\n');
            }
            return sb.toString();
        }
    }
}
