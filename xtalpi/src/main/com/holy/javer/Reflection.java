package com.holy.javer;

import java.lang.reflect.Field;

public class Reflection {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        // 反射可以绕过 java 的一些 语法机制，反射+泛型可以实现类型参数化
        String s = " a, b ";
        Field sc = s.getClass().getDeclaredField("value");
        sc.setAccessible(true);
        char[] o = (char[])sc.get(s);
        o[2] = 4; // ascII char 和 int 可以互转
        o[3] = 'c';
        System.out.println(s + "   " + o.length);
    }
}
