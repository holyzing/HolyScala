package com.holy.javer;

import java.util.ArrayList;
import java.util.Arrays;

public class MyCollections {
    public static void main(String[] args) {
        String javaStr = "this is a java string";
        String[] strArr = new String[5];
        String[] strArr2 = new String[]{"a", "b"};
        ArrayList<String> alStr = new ArrayList<>();
        alStr.add("java");
        System.out.println(javaStr.length());
        System.out.println(strArr.length);
        System.out.println(strArr[0]);
        System.out.println(strArr2);
        System.out.println(strArr2.toString());
        System.out.println(Arrays.toString(strArr2));
        System.out.println(alStr.size());
        System.out.println(alStr.get(0));

    }
}
