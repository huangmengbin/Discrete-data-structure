package com.company.Sorting;

import com.company.PrintableCollection.MyHeap;
import com.company.PrintableCollection.MyQueue;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class HeapSort {

    public static void main(String[]args){
        MyHeap<Integer> heap = new MyHeap<>( (a, b) -> (b-a) , 50);

        ArrayList<Integer> integers = new ArrayList<>(Arrays.asList(217,2,5,67,666,67666,0,125,10,80,30,4));
        heap.create(integers);

        MyQueue<Integer> queue = new MyQueue<>();
        while (!heap.isEmpty()){
            queue.add(heap.peek());
            heap.poll();
        }
    }
}
