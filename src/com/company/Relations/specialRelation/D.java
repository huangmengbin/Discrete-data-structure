package com.company.Relations.specialRelation;

import java.util.*;

public class D extends SpecialRelation<Long> {
    public D(long number){
        if(number<=0)throw new ArithmeticException();
        collection=calculate(number);
        collection.add( 1L );
        this.relation = (a,b) -> b % a == 0 ;
    }
    private static HashSet<Long> calculate(long longNum){
        System.out.print(longNum);
        HashMap<Long, Integer> primeFactorToExponentMap = new HashMap<Long, Integer>(){
            @Override
            public String toString() {
                Iterator<Entry<Long,Integer>> i = entrySet().iterator();
                if (! i.hasNext())
                    return "{}";

                StringBuilder sb = new StringBuilder();
                sb.append("{\n");
                for (;;) {
                    Entry<Long,Integer> e = i.next();
                    Long key = e.getKey();
                    Integer value = e.getValue();
                    sb.append(" prime factor:").append(key).append(" \t -->\texponent:" ).append(value).append("\n");
                    if (! i.hasNext())
                        return sb.append('}').toString();
                }
            }
        }; //素因子 -> 指数 , 懒得自己维护了，反正规模也不大......


        {
            long i = 2L;
            while (i <= longNum) {
                if (longNum % i == 0) {
                    longNum /= i;
                    primeFactorToExponentMap.merge(i, 1, (a, b) -> a + b);
                }
                else {
                    ++i;
                }
            }//end while
        }


        ArrayList<ArrayList<Long>> atoms = new ArrayList<>();
        for(long factor:primeFactorToExponentMap.keySet()){
            ArrayList<Long> arrayList = new ArrayList<>();
            long tmp ;
            arrayList.add( tmp = 1L);
            for(int i=0 ; i<primeFactorToExponentMap.get(factor) ; i++){
                tmp *= factor;
                arrayList.add(tmp);
            }
            atoms.add(arrayList);
        }
        HashSet<Long> result = helper(atoms,new ArrayList<>(),new HashSet<>());
        System.out.println(" 的因子个数： "+result.size());
        System.out.println(primeFactorToExponentMap);
        return result;

    }

    private static HashSet<Long> helper(ArrayList<ArrayList<Long>>atoms ,  ArrayList<Integer> choose , HashSet<Long>result){
        if(choose.size()==atoms.size()){
            Long tmp=1L;
            for(int i = 0 ; i<atoms.size() ; ++i){
                tmp *= ( atoms.get(i).get( choose.get(i) ) );
            }
            result.add(tmp);
        }
        else if(choose.size()<atoms.size()) {
            for(int i=0;i< atoms.get(choose.size()).size() ; i++ ){
                choose.add(i);
                helper(atoms, choose, result);
                choose.remove(choose.size()-1);
            }
        }
        else
            throw new ArrayIndexOutOfBoundsException();

        return result;
    }
}

