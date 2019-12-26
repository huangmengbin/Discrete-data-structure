package com.company.PrintableCollection;

import com.company.MyComponent.MyFrame;
import com.company.PrintableNode.PrintableNodeOfTreeBinary;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MyHeap<T> extends MyList<MyHeap.Node>{


    public MyHeap(Comparator<T>comparator){
        add(null);
        setSleepTime(300);///<-----------------===============
        MyFrame.getFrame().addGraph(heapPane);
        this.comparator=comparator;
    }
    public MyHeap(Comparator<T>comparator, int sleepTime ){
        this(comparator);
        setSleepTime(sleepTime);
    }

    public void setComparator(Comparator<? super T> comparator) {
        this.comparator = comparator;
    }

    private Comparator<? super T> comparator;
    private JPanel heapPane = new JPanel();
    private MyHeap.Node first(){return isEmpty()?null:get(1); }
    private MyHeap.Node last(){return isEmpty()?null:get(arraySize()-1);}
    private MyHeap.Node father(int i){return get(i/2);}
    private MyHeap.Node lhs(int i){return 2*i<arraySize()?this.get(2*i):null;}
    private MyHeap.Node rhs(int i){return (2*i+1)<arraySize()?this.get(2*i+1):null;}

    public void create(Collection<T>collection){
        if(collection==null)return;
        try {
            for (T t : collection) {
                add(new Node(t, arraySize()));
                Thread.sleep(getSleepTime());
                updateUI();
            }
            for(MyHeap.Node node: this){
                if(node!=null)new Thread(()->node.twinkle(Color.cyan,getSleepTime())).start();
            }
            Thread.sleep(getSleepTime()*3);
            for(int i=size()/2;i>=1;i--){
                down(get(i));
            }
            for(Node node: this){
                if(node!=null)new Thread(()->node.twinkle(Color.green,getSleepTime())).start();
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void enqueue(T data){
        try {
            Node node = new Node(data, this.arraySize());
            this.add(node);
            Thread.sleep(getSleepTime());
            updateUI();
            node.getButton().setBackground(Color.pink);
            Thread.sleep(getSleepTime());
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        up();
        updateUI();
    }
    public T peek(){
        return (T) first().data;
    }
    public void poll(){
        //T result = first().data;
        if(first()==null)return;
        MyFrame.enablePulse();
        try {
            Thread.sleep(getSleepTime());
            first().getButton().setText("<html><center>del<br>"+first().getButton().getText()+"</center></html>");
            first().twinkle(Color.GRAY,getSleepTime());
            Thread.sleep(getSleepTime());
            last().getButton().setBackground(Color.pink);
            last().twinkle(Color.lightGray,getSleepTime());
            Thread.sleep(getSleepTime());
            Node tmp = (Node) this.remove(arraySize()-1 );
            if(arraySize()<=1){updateUI();return;}
            first().data = tmp.data;
            updateUI();
            Thread.sleep(getSleepTime());
            this.set(1, tmp);
            first().ptr=1;
            updateUI();
            Thread.sleep(getSleepTime());
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        if(arraySize()>=2){
            down();
            first().updatePanelUI(heapPane);
        }
        return;
    }
    private void updateUI(){
        if(!isEmpty() && first()!=null)
            first().updatePanelUI(heapPane);
        else {
            heapPane.removeAll();
        }
        heapPane.updateUI();
    }

    private void up(){
        try {
            Node current = last();
            while (current.father()!=null){

                int compared = comparator.compare(current.data,current.father().data);
                current.getButton().setBackground(Color.pink);
                Thread.sleep(getSleepTime());
                if(compared<0){
                    current.father().twinkle(getSleepTime());
                    swap(current,current.father());
                }
                else {
                    current.father().getButton().setBackground(Color.lightGray);
                    Thread.sleep(getSleepTime());
                    break;
                }
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    private void down(Node current){
        try {
            while (current.lhs()!=null){
                MyFrame.enablePulse();
                current.getButton().setBackground(Color.pink);
                Thread.sleep(getSleepTime());
                Node next = current.lhs();
                if(current.rhs()!=null && comparator.compare(current.rhs().data,current.lhs().data)<0){
                    next = current.rhs();
                }
                next.getButton().setBackground(Color.pink);
                Thread.sleep(getSleepTime());
                next.getButton().setBackground(Color.orange);
                Thread.sleep(getSleepTime());

                if(comparator.compare(next.data,current.data)<0){
                    next.twinkle(getSleepTime());
                    swap(current,next);
                }
                else {
                    next.getButton().setBackground(Color.lightGray);
                    Thread.sleep(getSleepTime());
                    updateUI();
                    Thread.sleep(getSleepTime());
                    break;
                }
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    private void down(){
        Node current = first();
        down(current);
    }
    private void swap(Node a,Node b){
        try{
            int tmpPtr   = b.ptr;
            set(a.ptr,b);
            get(a.ptr).ptr=a.ptr;
            set(tmpPtr,a);
            get(tmpPtr).ptr=tmpPtr;
            Thread.sleep(getSleepTime());
            first().updatePanelUI(heapPane);
        }
        catch (InterruptedException e){e.printStackTrace();}
    }

    public static void main(String[] d){
        MyHeap<Integer> heap = new MyHeap<>( Integer::compareTo);
    }


    @Override
    public boolean isEmpty() {
        return arraySize()<=1;
    }

    private int arraySize(){
        return super.size();
    }
    @Override
    public int size(){
        return arraySize()-1;
    }
    @Override
    public void clear() {
        super.clear();
        heapPane.removeAll();
        heapPane.updateUI();
        add(null);
    }
    @Override
    public void setNotUseful(){
        super.setNotUseful();
        MyFrame.getFrame().removeGraph(heapPane);
    }
    class Node extends PrintableNodeOfTreeBinary{
        private T data;
        private int ptr;
        private Node father(){return get(ptr/2);}
        private Node lhs(){return 2*ptr<arraySize()?get(2*ptr):null;}
        private Node rhs(){return 2*ptr+1<arraySize()?get(2*ptr+1):null;}
        Node(T data,int ptr){
            this.data=data;
            this.ptr=ptr;
        }

        @Override
        protected void _Who_are_your_children() {
            My_children_are(lhs(),rhs());
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }
}
