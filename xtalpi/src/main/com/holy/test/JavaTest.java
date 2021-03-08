package com.holy.test;

public class JavaTest {
    public static void main(String[] args) {

    }
}

class Father{
    int i = 10;

    public int getI() {
        return i;
    }

    public int getResult(){
        return i + 10;
    }
}

class Son extends Father{
    int i = 20;

    public int getI() {
        return i;
    }

    public int getResult(){
        return i + 10;
    }
}
