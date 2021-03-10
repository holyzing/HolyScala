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
 *
 * 多特质混入的时候方法执行顺序
 *     从右到左
 *     特质中的 super 的意义不是父特质,而指的是上一级特质(从右向左)
 *     如果需要特质指向父特质 则需要限定 即 super[parentTrait]
 */

trait ClassTrait {

}

object ClassTrait{

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
        def read()
    }

    trait Yell extends TopTrait {
        override def printName(): Unit ={
            println("特质：Yell")
            super.printName()
        }

        def yell(): Unit ={
            println("吼 ...")
        }
    }

    trait Move extends TopTrait {
        // NOTE 限制特质使用的范围, 被混入类必须是一个异常类
        this: Exception=>
        override def printName(): Unit ={
            println("特质：move")
            super.printName()
            // super[Yell].printName() 运行时错误 Yell does not name a parent class of trait Move
        }
    }

    trait AnimalTrait extends Exception{
        def printTraitName(): Unit ={
            println("AnimalTrait")
            this.getMessage
        }
    }

    class Animal extends AnimalTrait {

    }

    /**
     * 1- 当既有父类又有特质的时候，需要 extends（继承） 父类 with（混入） 特质
     * 2- 特质和 java 中的接口一样都是抽象的，不能被实例化，需要被实现。
     * 3- java接口中的方法都是抽象的，后续版本可以有默认实现，java的接口中也可以声明属性，
     *    但是不能被更改。
     * 4- java 的接口体是不能编写非定义语句的，而scala 特质像其类体一样可以混入非定义语句
     * 5- 特质中可以定义普通的var val 和 普通方法，被混入的类在权限允许下是可以直接引用他们甚至修改,
     *    而java中的接口中定义的属性都是常量不能修改
     * 6- java 的接口实现不会传递,即子类不会实现父类实现的接口,可以(obj.getClass().getInterfaces().length == 0)
     *    scala 如果一个子类继承了一个混入特质的父类 那么 子类也不会混入特质,能访问特质的成员是因为继承了父类.
     * 7- scala 即继承了父类又混入了特质,那么特质是不会混入父类的.
     * 8- java 中的接口也可以继承另外一个接口.
     * 9- scala 也可以通过 with 将 java中的接口当成特质一样使用
     * 10- 特质可以继承类
     *
     * 　　抽象类不能多继承，只能是单继承；
     * 　　抽象类有带参数的构造函数，特质不行（如 trait t（i：Int）{} ，这种声明是错误的）
     */
    // NOTE illegal inheritance, self-type Dog does not conform to Exception
    class Dog extends Animal with Yell with Move {  // TODO 内部静态类 ？？？
        println("底层类 Dog")
        override def read(): Unit = {               // override 可以省略
            println("狗狗会写字 !")
        }
        override def printName(): Unit ={           // override 不可以省略
            println("我是哈巴狗 !")
        }
    }

    def main(args: Array[String]): Unit = {
        val dog = new Dog()
        println(dog.name, dog.age)
        dog.printName()
        dog.setGender("female")
        dog.printTraitName()

        val animal = new Animal() with TopTrait {
            override def read(): Unit = {
                // NOTE 动态混入:
                //      OCP 扩展功能开放,但是不能修改源码
            }
        }
        animal.read()
    }

}


