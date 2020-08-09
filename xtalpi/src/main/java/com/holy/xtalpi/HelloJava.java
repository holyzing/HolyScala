package com.holy.xtalpi;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class HelloJava {
    public static void signature() {
        List<Integer> l = new ArrayList<Integer>();
        System.out.println("我是一个java程序！");
        System.out.println(l);
        System.out.println(0x11);
        System.out.println(11);
        System.out.println(011);
        System.out.println(0b11);
    }

    @Test
    public void test(){
        System.out.println("Java Test");
    }

    public static void main(String[] args) {
        signature();
    }
}
