package com.holy.scaler


/**
 * Scala Trait(特征) 相当于 Java 的接口，但是比接口的功能强大，可以定义属性(java8也可以)以及方法的实现。
 * 一般情况下Scala的类只能够继承单一父类，但是同 java 的 interface 一样可以继承多个Trait，也就是实现了多继承。
 *
 *  Scala Trait(特征)更像 Java 的抽象类
 *
 * 特征也可以有构造器，由字段的初始化和其他特征体中的语句构成。这些语句在任何混入该特征的对象在构造时都会被执行。
 *
 * 构造器的执行顺序：
 *     调用超类的构造器；
 *     特征构造器在超类构造器之后、类构造器之前执行；
 *     特征由左到右被构造；
 *     每个特征当中，父特征先被构造；
 *     如果多个特征共有一个父特征，父特征不会被重复构造
 *     所有特征被构造完毕，子类被构造。
 * 构造器的顺序是类的线性化的反向。线性化是描述某个类型的所有超类型的一种技术规格。
 */

trait ScalaTrait {

}

object ScalaTrait{

    trait TopTrait{
        println("特质 TopTrait")
        val age: Int = 18
        var name: String = _
        private var gender: String = _
        def setGender(gender: String): Unit = {
            this.gender = gender
        }
        def printName(): Unit ={
            println("TopTrait", name)
        }
    }

    trait Yell extends TopTrait {
        println("特质：Yell")

    }

    trait Move extends TopTrait {
        println("特质：move")

    }

    class Animal {

    }

    /**
     * 1- 当既有父类又有特质的时候，需要 extends（继承） 父类 with（混入） 特质
     * 2- 特质和 java 中的接口一样都是抽象的，不能被实例化，需要被实现。
     * 3- java接口中的方法都是抽象的，后续版本可以有默认实现，java的接口中也可以声明属性，
     *    但是不能被更改。
     * 4- java 的接口体是不能混入表达式语句的，而scala 特质像其类体一样可以混入表达式语句
     * 5- 特质中可以定义普通的var 和 普通方法，被混入的类在权限允许下是可以直接引用的
     */
    class Dog extends Animal with Yell with Move {  // TODO 内部静态类 ？？？
        println("底层类 Dog")
    }

    def main(args: Array[String]): Unit = {
        val dog = new Dog()
        println(dog.name, dog.age)
        dog.printName()
        dog.setGender("female")
    }

}


