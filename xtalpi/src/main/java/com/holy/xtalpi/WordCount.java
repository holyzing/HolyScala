package com.holy.xtalpi;

import org.apache.spark.SparkConf;

import java.util.Iterator;
import java.util.Properties;

public class WordCount {
    public static void main(String[] args) {
        System.setProperty("spark.master", "local");
        Properties pops = System.getProperties();
        for (Iterator it = pops.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            String value = (String) pops.get(key);
            System.out.println(key + ":" + value);
            break;
        };
        int a = 5;
        // -> :: >> <<  >>>
        String[] strArray = new String[] {"aaa", "bbb"};
        for (String el: strArray){
            System.out.println(el);
        }
        test(2, "s", "c");
    }

    public static void test(int a,  String... b) {
        System.out.println(b);
        Integer d = new Integer(2);
        WordCount wc = new WordCount();
        // boolean c = wc instanceof String;
        System.out.println(b);
    }

    public static <T> void test2(){
        // T t = new T();  // Type parameter 'T' cannot be instantiated directly
        // return t;
        return;
    }
}
