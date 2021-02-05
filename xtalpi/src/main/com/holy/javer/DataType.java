package com.holy.javer;

public class DataType {
    public static void main(String[] args) {
        test(new Byte("10"));
        // char 类型是16位无符号整数
        byte b = 10;
        short s = 20;
        int i = b + s;
        // b = b + s;       // 运算符指令不够,将所有int 下的数据类型之间的 + 操作提升为 int 类型的操作
        String _ = "_";     // java 9 已经不支持 _单独使用下划线做标识符了
        String _1 = "_1";
        System.out.println(_ + _1);
    }
    public static void test(byte b) {
        System.out.println("bbbb");
    }
    public static void test(short s) {
        System.out.println("ssss");
    }
    public static void test(char c) {
        System.out.println("cccc");
    }
    public static void test(int i) {
        System.out.println("iiii");
    }
}
