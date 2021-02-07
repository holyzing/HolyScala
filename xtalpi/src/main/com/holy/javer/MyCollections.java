package com.holy.javer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class MyCollections {
    public static void main(String[] args) {
        String javaStr = "this is a java string";
        String[] strArr = new String[5];
        strArr[0] = "lulu";
        String[] strArr2 = new String[]{"a", "b"};
        ArrayList<String> alStr = new ArrayList<>();
        alStr.add("java");
        System.out.println(javaStr.length());
        System.out.println(strArr.length);
        System.out.println(strArr[0]);
        System.out.println(Arrays.toString(strArr2));
        System.out.println(Arrays.toString(strArr2));
        System.out.println(Arrays.toString(strArr2));
        System.out.println(alStr.size());
        System.out.println(alStr.get(0));
    }

    @Test
    public void testEqual() {
        String s1 = new String("s");
        String s2 = "s";
        String s3 = "abc".substring(0, 1);
        if (s1 == "s") {
            System.out.println("s1 == \"s\"");
        } else {
            if (s1 == s2) {
                System.out.println("s1 == s2");
            }else {
                System.out.println("s1 != s2 != 'a'");
            }
        }
        System.out.println("a".hashCode() + "--" + s1.hashCode() + "--" + s2.hashCode() + "--" + s3.hashCode());
    }
}
