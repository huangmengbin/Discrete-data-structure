package com.company.Relations.specialRelation;
import java.util.*;

public class PowerSet extends SpecialRelation<HashSet<Object>> {
    public PowerSet (Object...objects){
        this(Arrays.asList(objects));
    }
    public PowerSet (Collection collection){
        if(!(collection instanceof Set)){
            this.collection=calculate(new HashSet<Object>(collection));
        }
        else {
            this.collection = calculate((Set) collection);
        }
        this.relation = (a,b)->b.containsAll(a);    //  ????
    }
    private static ArrayList<HashSet<Object>> calculate(Set set){
        Object[]objects = set.toArray();
        ArrayList<HashSet<Object>> result = new ArrayList<>();
        final int number = objects.length;
        ArrayList<String> strings = B.calculate(number);
        for(String string : strings){
            HashSet<Object> hashSet =  new HashSet<Object>(){
                @Override
                public String toString() {
                    Iterator<Object> it = iterator();
                    if (!it.hasNext()) return " Ø ";
                    StringBuilder sb = new StringBuilder().append("{ ").append(it.next());
                    while (it.hasNext()) sb.append(", ").append(it.next());
                    return sb.append(" }").toString();
                }
            };
            for(int ptr = 0; ptr<number; ptr++){
                if(string.charAt(ptr)=='1'){
                    hashSet.add(objects[ptr]);
                }
            }
            result.add(hashSet);
        }
        return result;
    }
}
