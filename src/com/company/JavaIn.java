package com.company;

import java.util.Scanner;

class JavaIn {
    private static Scanner scanner = new Scanner(System.in);

    static int nextInt(){
        return scanner.nextInt();
    }
    static String next(){       //会被空格打断
        return scanner.next();
    }
    static String nextLine(){
        return scanner.nextLine();
    }
    static double nextDouble(){
        return scanner.nextDouble();
    }
    static float nextFloat(){
        return scanner.nextFloat();
    }
    static long nextLong(){
        return scanner.nextLong();
    }
    static byte nextByte(){
        return scanner.nextByte();
    }
    static String getStrings(int number,String string){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<number;i++)stringBuilder.append(string);
        return stringBuilder.toString();
    }
}
