package com.holy.javer;

import java.awt.*;

public interface DearInterface {
    // 接口中定义的方法默认是 public 且 abstract
    // java 一个源文件中 只能有一个 与源文件名相同的公共类 / 接口
    void sayHello();
    void seeYou();

    String str = "";
}

interface SeeJava {
    static void testStaticMethodInInterface(){
        System.out.println("静态的就是要被实现的");
    };
    void loveJava();
}

abstract class AbstractClass{
    public static void main(String[] args) {
        System.out.println("抽象类中是可以定义静态方法的");
        System.out.println("抽象类不能被实例化，除非匿名实现");
        SeeJava.testStaticMethodInInterface();
    }

    public static void testAbstractClass() {
        System.out.println("因为我是一个类，所以我就可以定义静态方法，没什么好说的");
    }
}

class Test implements DearInterface, SeeJava{
    public static void main(String[] args) {
        SeeJava sj = new SeeJava() {
            @Override
            public void loveJava() {
                System.out.println("我是一个匿名类！");
            }
        };
        DearInterface di = new Test();
        System.out.println(di.str);
        SeeJava sj2 = new Test();
        di.sayHello();
        di.seeYou();
        sj.loveJava();
        sj2.loveJava();

    }

    @Override
    public void sayHello() {

    }

    @Override
    public void seeYou() {

    }

    @Override
    public void loveJava() {

    }
}
