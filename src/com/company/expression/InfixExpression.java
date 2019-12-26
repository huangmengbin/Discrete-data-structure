package com.company.expression;

import com.company.MyComponent.MyFrame;
import com.company.PrintableCollection.MyList;
import com.company.PrintableCollection.MyQueue;
import com.company.PrintableCollection.MyStack;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Stack;

public class InfixExpression extends Expression {

    public InfixExpression(){}
    public InfixExpression(String data){super(data);}

    public double toValue(){
        ptr=0;
        MyStack<Double> numbers=new MyStack<>();
        MyStack<Character> chars=new MyStack<>();
        numbers.setSleepTime(100);
        chars.setSleepTime(100);
        chars.push('#');

        MyList<Character> myList = new MyList<>();
        for(Character c:data.toCharArray()) {
            myList.add(c);
        }
        MyFrame.setCurrentState(MyFrame.PULSE);
        int beforePtr=0;
        while( ! chars.isEmpty()){
            MyFrame.enablePulse();

            char ch = data.charAt(ptr);

            if (Character.isLetter(ch)){
                throw new ArithmeticException("字母怎么求值");
            }
            else if(Character.isDigit(ch)){
                numbers.push(  nextDouble() );
            }
            else if(left.get(chars.peek()) < right.get(ch)){
                if(ch=='-'&&(ptr==0||data.charAt(ptr-1)=='(')){////
                    chars.push('~');
                }
                else {
                    chars.push(ch);//正常情况
                }
                ptr++;
            }
            else if(left.get(chars.peek()) > right.get(ch)){
                if(isDanMuFu(chars.peek())){
                    numbers.push(calculate(numbers.pop(),chars.pop()));///单目
                }
                else {
                    numbers.push(calculate(numbers.pop(), chars.pop(), numbers.pop()));
                }
                //ptr不要自增
            }
            else if(left.get(chars.peek()) .equals( right.get(ch) ) ){   //==  特指 ()匹配 或者 末尾#遇上栈底#
                chars.pop();
                ptr++;
            }
            else {
                throw new ArithmeticException();
            }
            myList.setColor(beforePtr , beforePtr=ptr, Color.GRAY);//让已经访问的换一种颜色
            if(ptr<myList.size()) myList.setColor( ptr , Color.CYAN);

        }
        return numbers.pop();
    }

    public PostfixExpression toPostfix(){
        ptr=0;
        StringBuilder result = new StringBuilder();
        Stack<Character> chars=new Stack<>();
        chars.push('#');

        while( ! chars.isEmpty()){

            char ch = data.charAt(ptr);

            if(ch == ' '){
                ptr++;
            }
            else if(Character.isLetter(ch)){///////////////////////////////////////
                result.append(  nextWord()  );
                result.append(fenGe);
            }
            else if(Character.isDigit(ch)){
                DecimalFormat df = new DecimalFormat("#.###############");
                result.append(   df.format(nextDouble())  );    //拒绝科学记数法
                result.append(fenGe);
            }
            else if(left.get(chars.peek()) < right.get(ch)){
                if(ch=='-'&&(ptr==0||data.charAt(ptr-1)=='(')){////
                    ch = '~' ;
                }
                chars.push(ch);
                ptr++;
            }
            else if(left.get(chars.peek()) > right.get(ch)){
                result.append(   chars.pop()  );
                //ptr不要自增
            }
            else if(left.get(chars.peek()) .equals( right.get(ch) ) ){   //==  特指 ()匹配 或者 末尾#遇上栈底#
                chars.pop();
                ptr++;
            }
            else throw new ArithmeticException();
        }

        return new PostfixExpression(result.toString());
    }


}
