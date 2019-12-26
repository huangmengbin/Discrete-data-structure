package com.company.Sorting;

import com.company.PrintableCollection.MyList;
import com.company.PrintableCollection.MyQueue;
import com.company.PrintableCollection.MyStack;

import java.awt.*;
import java.util.Random;

public class QuickSort<T extends Comparable<T>> {

    private MyList<T> list;

    private final static int sleepTime1 = 10;
    private final static int sleepTime2 = 20;
    private final static Color twinkleColor =Color.cyan;        //代表指针所在的位置
    private final static Color runningColor = Color.green;      //代表正在处理
    private final static Color finalColor   = Color.lightGray;  //代表处理完成


    private MyQueue<String> strings = new MyQueue<>();
    private MyQueue<String> queue = new MyQueue<>();


    private static int getBasePtr(int before, int last){
        return  Math.abs(new Random(System.nanoTime()+1).nextInt() )%(last-before)+before;
        //return before;
    }

    private QuickSort(MyList<T> list){

        this.list = list;

        try {

            MyStack<Integer> ptrStack = new MyStack<>();
            ptrStack.setSleepTime(sleepTime1 /3);
            list.setSleepTime(sleepTime1);
            ptrStack.push(list.size() - 1);
            ptrStack.push(0);

            Thread.sleep(sleepTime1);

            while(!ptrStack.isEmpty()){

                final int first = ptrStack.pop();
                final int last  = ptrStack.pop();
                if( first > last )throw new RuntimeException(first+" > "+last);
                int basePtr = getBasePtr(first,last);
                {
                    list.twinkle(first, last + 1, runningColor, 1);
                    list.setColor(first, last + 1, runningColor);
                    Thread.sleep(sleepTime1 * 2);


                    strings.add("key");
                    strings.add(list.get(basePtr).toString());
                    list.twinkle(basePtr, twinkleColor, 2);
                    list.makeTextEmpty(basePtr);

                    Thread.sleep(sleepTime1);
                }
                basePtr = partition(first,last,basePtr);


                if(basePtr+1 < last){
                    ptrStack.push(last);
                    ptrStack.push(basePtr+1);
                }
                if(basePtr-1 > first){
                    ptrStack.push(basePtr-1);
                    ptrStack.push(first);
                }
                {
                    Thread.sleep(sleepTime1);

                    list.setColor(first, last + 1, Color.yellow);
                    list.setColor(basePtr, finalColor);

                    strings.clear();
                }
            }

            strings.setNotUseful();
            ptrStack.clear();
            ptrStack.setNotUseful();

        }
        catch (InterruptedException err){
            err.printStackTrace();
        }
    }

    private int partition(final int first , final int last, final int basePtr){

        try {

            final T base = list.get(basePtr);
            int left =first , right = last;

            {//initial
                list.setColor(basePtr, twinkleColor);
                list.makeTextEmpty(basePtr);
                Thread.sleep(sleepTime1);
            }

            {//第一次移动，将key尽可能移动到左边
                left = getFirstLarger(first, base, left, basePtr);
                if (left < basePtr) {
                    move(left, basePtr);
                }
                queue.clear();
            }

            while (left!=right){//循环直至左右指针碰撞
                {//----------------------------------------------------
                    right = getFirstLess(last, base, left, right);
                    if (left < right) {
                        move(right, left);
                        left++;
                    }
                    queue.clear();
                }//-----------------------------------------------------
                if(left==right)break;
                {//-----------------------------------------------------
                    left = getFirstLarger(first, base, left, right);
                    if (left < right) {
                        move(left, right);
                        right--;
                    }
                    queue.clear();
                }//-----------------------------------------------------
            }//end while

            {//clear
                list.set(left, base);
                queue.clear();
                queue.setNotUseful();
            }
            return left;
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        return -1;
    }

    private int getFirstLess(final int last,final T base,final int left, int right) throws InterruptedException {
        queue.add("Search the one who");
        queue.add("less than");
        queue.add(base.toString());
        Thread.sleep(sleepTime2);
        if(right<last)list.setColor(right+1,runningColor);
        while (list.get(right).compareTo(base)>=0 && left<right ){
            list.setColor(right, twinkleColor);
            Thread.sleep(sleepTime1);
            list.setColor(right,runningColor);
            right--;//<----
        }
        return right;
    }

    private int getFirstLarger(final int first,final T base, int left, final int right) throws InterruptedException {
        queue.add("Search the one who");
        queue.add("larger than");
        queue.add(base.toString());
        Thread.sleep(sleepTime2);
        if(left>first)list.setColor(left-1,runningColor);
        while (list.get(left).compareTo(base)<=0 && left<right ){
            list.setColor(left, twinkleColor);
            Thread.sleep(sleepTime1);
            list.setColor(left,runningColor);
            left++;//<---
        }
        return left;
    }

    private void move(final int from,final int to) throws InterruptedException {
        queue.add("succeed!");
        list.twinkle(from, twinkleColor,2);
        Thread.sleep(sleepTime2);
        list.setColor(to, twinkleColor);
        list.setColor(from, twinkleColor);
        list.set( to , list.get(from));   ///   <<<--------------------------- 重点是这个，把from元素 移动到 to位置
        list.makeTextEmpty(from);          //   <<<---  可有可无
        Thread.sleep(sleepTime2);
    }

    public static void main(String[] args){
        MyList<Integer> integers = new MyList<>();
        for(int i=0;i<25;++i){
            integers.add(new Random(System.nanoTime()).nextInt()%100 +100);
        }
        new QuickSort<>(integers);
    }
}
