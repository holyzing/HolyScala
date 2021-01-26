package com.holy.javer;

/**
 * 最终类是不可以被继承，但是可以被实例化
 * 抽象类是可以被普通类和抽象类继承，且不能被实例化， 抽象类继承抽象类可以不重写抽象类方法
 *
 */

abstract class AbstractClass2{
    public void test(){
        test2();
    }

    public abstract void test2();
}

abstract class AbstractClass3 extends AbstractClass2 {
    public void test(){
        test3();
    }
    public abstract void test3();
}

public final class FinalTest extends AbstractClass3{

    public static void main(String[] args) {
        new FinalTest();
    }


    @Override
    public void test2() {

    }

    @Override
    public void test3() {

    }
}
