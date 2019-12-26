package com.company.PrintableCollection;

import java.awt.*;
import java.util.*;

public class HeapHomeWork {
    private static MyHeap<Integer> heap = new MyHeap<>(Integer::compareTo,300);
    public static void main(String[] homework) {
        //_1A();
        //_1B();
        //_2A();
        //_2B();
        //_3_1();
        //_3_2();
        //_3_3();
        //_3_4();

        _4();
    }

    private static void _1A(){
        MyList<Integer> integers = new MyList<>();
        integers.addAll(Arrays.asList(10, 12, 1, 14, 6, 5, 8, 15, 3, 9, 7, 4,   11, 13,  2));
        heap.clear();
        int i=0;
        for (Integer integer : integers) {
            integers.setColor(i++,i, Color.lightGray);
            heap.enqueue(integer);
        }
    }
    private static void _1B() {
        heap.clear();
        ArrayList<Integer> integers = new MyList<>();
        integers.addAll(Arrays.asList(10, 12, 1, 14, 6, 5, 8, 15, 3, 9, 7, 4,   11, 13,2));
        heap.create(integers);
    }
    private static void _2A(){
        int oldTime = heap.getSleepTime();
        heap.setSleepTime(5);
        _1A();
        heap.setSleepTime(oldTime);
        heap.poll();
        heap.poll();
        heap.poll();
    }
    private static void _2B(){
        int oldTime = heap.getSleepTime();
        heap.setSleepTime(5);
        _1B();
        heap.setSleepTime(oldTime);
        heap.poll();
        heap.poll();
        heap.poll();
    }
    private static void _3_1(){
        heap.setComparator((a,b)->b-a);
        heap.clear();
        ArrayList<Integer> integers = new MyList<>();
        integers.addAll(Arrays.asList( 100, 86, 48, 73, 35, 39, 42, 57, 66, 21 ));
        heap.create(integers);
    }
    private static void _3_2(){
        ArrayList<Integer> integers = new MyList<>();
        integers.addAll(Arrays.asList( 12, 70, 33, 65, 24, 56, 48, 92, 86, 33 ));
        heap.clear();
        heap.create(integers);
    }
    private static void _3_3(){
        heap.setComparator((a,b)->b-a);
        ArrayList<Integer> integers = new MyList<>();
        integers.addAll(Arrays.asList( 103, 97, 56, 38, 66, 23, 42, 12, 30, 52, 06, 20));
        heap.clear();
        heap.create(integers);
    }
    private static void _3_4(){
        ArrayList<Integer> integers = new MyList<>();
        integers.addAll(Arrays.asList( 05, 56, 20, 23, 40, 38, 29, 61, 35, 76, 28, 100 ));
        heap.clear();
        heap.create(integers);
    }
    private static void _4(){
        final char tag = '*';
        MyHeap<String>heap = new MyHeap<>(Comparator.comparingInt((a ->Integer.parseInt(a.substring(0,
                a.indexOf(tag)<0 ? a.length() : a.indexOf(tag)  )))) ,  20);
        ArrayList<String> strings = new MyList<>();
        strings.addAll(Arrays.asList( "12","2","16","30","28","10","16*","20","6","18"));
        System.out.println(strings+"\n");
        heap.create(strings);
        MyQueue<String> queue = new MyQueue<>();
        while (!heap.isEmpty()){
            queue.add(heap.peek());
            System.out.println(heap+heap.peek());
            heap.poll();
        }
    }

}
