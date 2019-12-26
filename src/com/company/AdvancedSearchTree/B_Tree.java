package com.company.AdvancedSearchTree;

import com.company.MyComponent.MyFrame;
import com.company.PrintableCollection.MyQueue;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class B_Tree {

    private int searchSleepTime = 500;
    private int solveFlowSleepTime = 600;
    private int sleepTime = 600;

    private int order;                                    //阶数

    private int size;

    private MyQueue<String> myQueue;

    private BTNode root;

    public void setRoot(BTNode root) {
        this.root = root;
        updateUI();
    }

    private BTNode lastVisit;

    private JPanel bigPane = new JPanel();

    private JPanel treePane = new JPanel();

    private void updateUI(){
        root.updatePanelUI(treePane);
    }

    private void repaintAll(){
        root.repaintAllButton();
    }

    private void setNotUseful(){
        bigPane.removeAll();
        myQueue.setNotUseful();
        MyFrame.getFrame().removeGraph(bigPane);
    }

    private void solveOverFlow(BTNode bigNode){
        try {

            if (this.order >= bigNode.size()) {
                return;
            }
            int newSize = this.order / 2;
            bigNode.setKeyColor(newSize,Color.CYAN);
            Thread.sleep(solveFlowSleepTime);
            BTNode newNode = new BTNode();
            for (int i = 0; i < order - newSize - 1; i++) {
                newNode.insert(i, bigNode.remove(newSize + 1));
            }
            newNode.setNode(0, bigNode.getNode(newSize + 1));//<---====？？？？？
            if (newNode.getNode(0) != null) {
                for (int i = 0; i < order - newSize; i++) {
                    newNode.getNode(i).father = newNode;
                }
            }
            BTNode pp = bigNode.father;
            if (pp == null) {
                this.root = pp = new BTNode();
                pp.setNode(0, bigNode);
                bigNode.father = pp;
            }
            int rank = pp.searchFirstLarger(bigNode.getKey(0));
            pp.insert( rank , bigNode.remove(newSize));
            pp.setNode(rank + 1, newNode);
            newNode.father = pp ;
            updateUI();
            pp.setKeyColor(rank,Color.CYAN);
            Thread.sleep(solveFlowSleepTime);
            repaintAll();
            solveOverFlow(pp);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private boolean fatEnough(BTNode node){
        return node.size() > (order+1)/2; /**指node.size - 1 后， 仍然满足 >= order/2 的要求 */
    }
    private void connect(BTNode left , BTNode right){
        left.setNode(left.size()-1,right.getNode(0));//right 即将被垃圾回收
        if(left.getNode(left.size()-1)!=null){left.getNode(left.size()-1).father = left;}////小心
        while(right.size()>1){
            left.insert(left.size()-1,right.remove(0));
            if(left.getNode(left.size()-1)!=null){left.getNode(left.size()-1).father = left;}////小心
        }
    }
    private void solveUnderFlow(BTNode smallNode){
        final Color color = Color.yellow;
        try {
            if (smallNode.size() >= (order+1) / 2) {
                return;
            }
            BTNode pp = smallNode.father;
            if (pp == null) {           //父节点没有size>=order/2的要求
                if (smallNode.size() == 1 && !smallNode.isLeaf()) { //但无关键码时可以降层
                    root = smallNode.getNode(0);
                    root.father = null;//smallNode即将会被垃圾回收
                }
                return;
            }

            int ptr = 0;
            while (pp.getNode(ptr) != smallNode) {
                ptr++;
            }
            if (ptr > 0) {//有左兄弟
                BTNode lhs = pp.getNode(ptr - 1);
                if (fatEnough(lhs)) { //右旋修正
                    lhs.setKeyColor(lhs.size()-2,color);
                    pp.setKeyColor(ptr-1,color);
                    Thread.sleep(solveFlowSleepTime);
                    smallNode. insert(0, pp.getKey(ptr - 1), null);
                    smallNode.setNode(1, smallNode.getNode(0));
                    smallNode.setNode(0, lhs.getNode(lhs.size() - 1));
                    pp.setKey(ptr - 1, lhs.getKey(lhs.size() - 2));
                    lhs.remove(lhs.size() - 2);
                    if (smallNode.getNode(0) != null) {
                        smallNode.getNode(0).father = smallNode;
                    }
                    return;
                }
            }
            if (ptr < (pp.size() - 1)) {//有右兄弟
                BTNode rhs = pp.getNode(ptr + 1);
                if (fatEnough(rhs)) { //左旋修正
                    rhs.setKeyColor(0,color);
                    pp.setKeyColor(ptr,color);
                    Thread.sleep(solveFlowSleepTime);
                    smallNode.insert(smallNode.size() - 1, pp.getKey(ptr), rhs.getNode(0));
                    pp.setKey(ptr, rhs.getKey(0));
                    BTNode tmp1 = rhs.getNode(1);
                    rhs.remove(0);
                    rhs.setNode(0, tmp1);
                    if (smallNode.getNode(smallNode.size() - 1) != null) {
                        smallNode.getNode(smallNode.size() - 1).father = smallNode;
                    }
                    return;
                }
            }
            //至此，已经无法旋转修正，需要合并
            ((JButton)smallNode.getButton()).setBackground(color);
            if (ptr > 0) {//有左兄弟
                BTNode lhs = pp.getNode(ptr - 1);
                ((JButton)lhs.getButton()).setBackground(color);
                pp.setKeyColor(ptr-1,color);
                Thread.sleep(solveFlowSleepTime);
                lhs.insert(lhs.size() - 1, pp.remove(ptr - 1));
                connect(lhs, smallNode);
            }
            else {//无左兄弟，所以必定有右兄弟
                BTNode rhs = pp.getNode(ptr+1);
                ((JButton)rhs.getButton()).setBackground(color);
                pp.setKeyColor(ptr,color);
                Thread.sleep(solveFlowSleepTime);
                smallNode.insert(smallNode.size() - 1, pp.remove(ptr));
                connect(smallNode, rhs);
            }
            updateUI();
            Thread.sleep(solveFlowSleepTime);
            solveUnderFlow(pp);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public B_Tree(int order){
        if(order<3){throw new ArithmeticException("我不会"+order+"阶的B树。。。");}
        this.order = order;
        Box box=Box.createVerticalBox();
        bigPane.add(box);
        JPanel textPane = new JPanel();
        JTextArea textArea = new JTextArea();
        textArea.setEditable(true);
        textArea.setPreferredSize(new Dimension(167,20));
        textPane.add(textArea);
        JButton search = new JButton("search");
        JButton insert = new JButton("insert");
        JButton remove = new JButton("remove");
        textPane.add(search);
        textPane.add(insert);
        textPane.add(remove);
         //按需求添加
        JButton update = new JButton("update");
        update.addActionListener( event -> updateUI() );
        textPane.add(update);

        search.addActionListener(event ->new Thread( () -> {
            search.setEnabled(false);insert.setEnabled(false);remove.setEnabled(false);try{search(Integer.parseInt(textArea.getText()));}catch (NumberFormatException err){textArea.setText("输入不合法");try{Thread.sleep(1000);}catch (InterruptedException e){e.printStackTrace();}}textArea.setText("");search.setEnabled(true);insert.setEnabled(true);remove.setEnabled(true);}).start());
        insert.addActionListener(event ->new Thread( () -> {
            search.setEnabled(false);insert.setEnabled(false);remove.setEnabled(false);try{insert(Integer.parseInt(textArea.getText()));}catch (NumberFormatException err){textArea.setText("输入不合法");try{Thread.sleep(1000);}catch (InterruptedException e){e.printStackTrace();}}textArea.setText("");search.setEnabled(true);insert.setEnabled(true);remove.setEnabled(true);}).start());
        remove.addActionListener(event ->new Thread( () -> {
            search.setEnabled(false);insert.setEnabled(false);remove.setEnabled(false);try{remove(Integer.parseInt(textArea.getText()));}catch (NumberFormatException err){textArea.setText("输入不合法");try{Thread.sleep(1000);}catch (InterruptedException e){e.printStackTrace();}}textArea.setText("");search.setEnabled(true);insert.setEnabled(true);remove.setEnabled(true);}).start());
        box.add(textPane);
        box.add(treePane);
        myQueue = new MyQueue<>();
        myQueue.setSleepTime(1);
        MyFrame.getFrame().addGraph(bigPane);
    }

    public B_Tree(){this(3);}

    public BTNode search(int number){
        try {
            repaintAll();
            MyQueue<String> myQueue = new MyQueue();
            myQueue.add("search");
            myQueue.add(String.valueOf(number));
            Thread.sleep(searchSleepTime);
            BTNode currentNode = root;
            lastVisit = null;
            while (currentNode != null) {
                int ptr = currentNode.searchFirstLarger(number); //找到首次关键码大于number的下标
                if (ptr > 0 && number == currentNode.getKey(ptr - 1)) {
                    myQueue.add("success");
                    currentNode.setKeyColor(ptr-1,Color.green);
                    Thread.sleep(searchSleepTime);
                    repaintAll();
                    myQueue.clear();
                    myQueue.setNotUseful();
                    return currentNode;
                }//查找成功
                lastVisit = currentNode;
                if(!currentNode.isLeaf()){
                    currentNode.setNodeColor(ptr,Color.CYAN);
                }
                else {
                    myQueue.add("fail");
                    currentNode.setNodeColor(ptr,Color.RED);
                    Thread.sleep(searchSleepTime);
                    repaintAll();
                }
                Thread.sleep(searchSleepTime);
                currentNode = currentNode.getNode(ptr);
            }
            myQueue.clear();
            myQueue.setNotUseful();
            return null;//查找失败
        }
        catch (InterruptedException error){
            error.printStackTrace();
            return null;
        }
    }

    private boolean insert(int number,BTNode newNode){//测试用
        try {
            myQueue.clear();
            myQueue.add("insert");
            myQueue.add(String.valueOf(number));

            BTNode node = search(number);
            if (node != null) { //查找成功，没必要插入
                return false;//返回false
            }

            lastVisit.insert(number, newNode);//插入新关键码
            size++;
            updateUI();
            Thread.sleep(sleepTime);
            solveOverFlow(lastVisit);
            updateUI();
            return true;
        }
        catch (InterruptedException e){
            e.printStackTrace();
            return false;
        }
    }
    public  boolean insert(int number){
        return this.insert(number,null);
    }

    public boolean remove (int number){
        try {
            myQueue.clear();
            myQueue.add("remove");
            myQueue.add(String.valueOf(number));
            BTNode node = search(number);
            if (node == null) {
                return false;
            }
            int ptr = node.searchFirstLarger(number);
            if (!node.isLeaf()) {
                BTNode node2 = node.getNode(ptr);
                while (!node2.isLeaf()) {//就需要一直走到叶子
                    node2 = node2.getNode(0);
                }
                node.setKey(ptr - 1, node2.getKey(0));
                node = node2;//node指向node2
                ptr = 1;
            }
            size--;
            ptr--;
            updateUI();
            node.setKeyColor(ptr,Color.pink);
            Thread.sleep(sleepTime);
            node.remove(ptr);
            updateUI();
            Thread.sleep(sleepTime);
            solveUnderFlow(node);
            Thread.sleep(sleepTime);
            updateUI();
            return true;
        }
        catch (InterruptedException e){
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[]test) {
        test02();
    }

    private static void test01(){

        final int bound = 5000 ;
        boolean insert = false;

        BTNode node = new BTNode();
        B_Tree bTree = new B_Tree(5);
        bTree.setRoot(node);

        ArrayList<Integer>arrayList = new ArrayList<>(10000);
        for(;;){
            System.out.println(bTree.size);
            if(insert || new Random(System.nanoTime()).nextInt()%2==0 || bTree.size<=500 ) {
                int number = ((new Random(System.nanoTime()).nextInt()+1) % bound) + bound;
                if(bTree.insert(number) ){
                    arrayList.add(number);
                    insert = false;
                }
                else {
                    insert = true;
                }
            }
            else {
                Integer number = arrayList.get((((new Random(System.nanoTime()).nextInt()+2) % bound) + bound)%arrayList.size());
                bTree.remove(number);
                arrayList.remove((Integer) number);
            }
        }
    }

    private static void test02(){
        BTNode node = new BTNode();
        B_Tree bTree = new B_Tree(3);
        bTree.setRoot(node);
    }
}
