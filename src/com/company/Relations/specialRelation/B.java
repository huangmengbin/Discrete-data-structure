package com.company.Relations.specialRelation;

import java.util.ArrayList;

public class B extends SpecialRelation<String> {
    public B(Integer number){
        collection=calculate(number);
        this.relation = (a,b) -> {
            for(int i=0;i<a.length();i++){
                if(a.charAt(i)=='1'&b.charAt(i)=='0'){
                    return false;
                }
            }
            return true;
        };
    }
    static ArrayList<String> calculate(Integer number){
        int powerN = (int) Math.pow(2,number);
        ArrayList<String> res = new ArrayList<>();
        for(int i=0;i<powerN;i++){
            String string = Integer.toBinaryString(i);
            while(string.length()<number)string="0"+string;
            res.add(string);
        }

        return res;
    }
}
