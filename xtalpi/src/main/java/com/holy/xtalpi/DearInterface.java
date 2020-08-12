package com.holy.xtalpi;

public interface DearInterface {
    // 接口中定义的方法默认是 public 且 abstract
    // java 一个源文件中 只能有一个 与源文件名相同的公共类 / 接口
    void sayHello();
    void seeYou();

    String str = "";
}

interface SeeJava {
    void loveJava();
}

abstract class AbstractClass{
    public static void main(String[] args) {
        System.out.println("抽象类不能被实例化，除非匿名实现");
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
