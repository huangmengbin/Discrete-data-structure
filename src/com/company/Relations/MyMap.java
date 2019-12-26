package com.company.Relations;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class MyMap<T> extends HashMap<T, HashSet<T>> {

    public MyMap(){}
    public MyMap(MyMap<T> myMap) {
        for(T key:myMap.keySet()){
            this.put(key,myMap.get(key));
        }
    }

    public void add(T key , T value){
        if (get(key) == null) {
            HashSet<T> hashSet = new HashSet<>();
            hashSet.add(value);
            this.put(key, hashSet);
        } else {
            this.get(key).add(value);
        }
    }
    public void add(T key , T...values){
        if(values!=null&&values.length!=0)
            this.addAll(key, Arrays.asList(values));

    }
    public void addAll(T key , Collection<T> collection){
        if(collection==null)return;
        if(get(key)==null){
            HashSet<T> hashSet = new HashSet<>(collection);
            this.put(key,hashSet);
        }
        else {
            this.get(key).addAll(collection);
        }
    }

    public void makeTransitive(){       // 应该是O(n^3）
        HashSet<T> hashSet = ketAndValueSet();
        for(T elemment:hashSet){
            for(T before:this.keySet()){
                HashSet<T> beforeSet = this.get(before);
                if(beforeSet!=null && beforeSet.contains(elemment)){
                    this.addAll(before , this.get(elemment));
                }
            }
        }
    }

    public void makeReflexive(){
        HashSet<T> tmpSet = new HashSet<>();
        for(T key : this.keySet()){
            for(T value:get(key)){
                tmpSet.add(key);
                tmpSet.add(value);
            }
        }
        for(T hhh:tmpSet){
            this.add(hhh,hhh);
        }
    }
    public void makeSymmetric(){

    }

    HashSet<T> ketAndValueSet(){
        HashSet<T> set = new HashSet<>();
        for(T key : this.keySet()){
            set.addAll(this.get(key));
            set.add(key);
        }
        return set;
    }

    public static void main(String[]a){//testNode
        MyMap<Integer>myMap=new MyMap<>();
        myMap.add(2,3,5,6,3,4);
        myMap.add(2,4,9);
        System.out.println(myMap.get(2));
        System.out.println(myMap.get(3));
    }
}
