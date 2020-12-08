package com.holy.javer;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;


import java.util.Iterator;
import java.util.Properties;


class GetLength implements Function<String, Integer> {
    public Integer call(String s) { return s.length(); }
}
class Sum implements Function2<Integer, Integer, Integer> {
    public Integer call(Integer a, Integer b) { return a + b; }
}

public class WordCount {

    public void rddApi(){
        SparkConf conf = new SparkConf().setAppName("javaRDDApi").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lines = sc.textFile("data.txt", 3);
        JavaRDD<Integer> lineLengths = lines.map(new Function<String, Integer>() {
            public Integer call(String s) { return s.length(); }
        });
        int totalLength = lineLengths.reduce(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer a, Integer b) { return a + b; }
        });


        JavaRDD<String> lines2 = sc.textFile("data.txt");
        JavaRDD<Integer> lineLengths2 = lines.map(new GetLength());
        int totalLength2 = lineLengths.reduce(new Sum());

    }

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
