package com.company.expression;

import java.util.HashMap;

public abstract class Expression { //这是作业

    final static char fenGe=' ';

    static HashMap<Character,Integer> left=new HashMap<>(),right=new HashMap<>();
    static {
        left.put('+',3);
        left.put('-',3);
        left.put('~',3);
        left.put('*',5);
        left.put('/',5);
        left.put('%',5);
        left.put('(',1);
        left.put(')',8);
        left.put('!',6);//单目是右结合的
        left.put('#',0);
        //left.put('',);
        right.put('+',2);
        right.put('-',2);
        right.put('~',3);
        right.put('*',4);
        right.put('/',4);
        right.put('%',4);
        right.put('(',8);
        right.put('!',7);
        right.put(')',1);
        right.put('#',0);
        //right.put('',);
    }



    String data;
    int ptr;
    public void setData(String data){this.data=data+"#";}//补#了

    Expression(){}
    Expression(String data){setData(data);}

    public String toString() {
        return data.substring(0,data.length()-1);
    }


    double nextDouble(){ //拒绝科学记数法
        int start=ptr;
        while(  Character.isDigit( data.charAt(ptr)   ) || data.charAt(ptr)=='.'  ){   ptr++;   }
        if(  Character.isLetter( data.charAt(ptr)  ) ){throw new ArithmeticException("这怎么算啊"); }
        return Double.parseDouble(  data.substring(start,ptr)  );
    }

    String nextWord(){
        int start=ptr;
        while (  Character.isLetter(data.charAt(ptr))  ){   ptr++;    }
        if(  Character.isDigit( data.charAt(ptr)   ) || data.charAt(ptr)=='.'  ){throw new ArithmeticException("这是什么变量");}
        return data.substring(start,ptr);
    }


    static boolean isDanMuFu(char ch){
        return ch=='~' || ch=='!' ;
    }

    static double calculate(double num,char ch){
        switch (ch){
            case '!' : return Math.abs(num-0) < 1e-7   ? 1 : 0 ;
            case '-' : return -num;
            case '~' : return -num;
            default  : throw new ArithmeticException("暂时不会算这个符号:"+ch);
        }
    }
    static double calculate(double Y,char c,double X){
        switch (c){
            case '%' : return X%Y;
            case '*' : return X*Y;
            case '/' : return X/Y;
            case '+' : return X+Y;
            case '-' : return X-Y;
            default  : throw new ArithmeticException("暂时不会算这个符号:"+c);
        }
    }
}