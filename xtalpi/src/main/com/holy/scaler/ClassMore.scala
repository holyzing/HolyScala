package com.holy.scaler

import scala.beans.BeanProperty


object ClassMore {

    def main(args: Array[String]): Unit = {
        val cm: ClassMore = new ClassMore()
        val son: cm.Dog = new cm.Dog("lulu", 18)
        println(son.name, son.gender)  // son.age
    }

    object AppTest extends App{
        println("App is a trait !")
    }

    object EnumerTest extends Enumeration{
        // 没有太大的意义,枚举的完吗 ???
        var RED: Value = Value(1, "啊")
        var YELLO: Value = Value(2, "a")
    }
}

class ClassMore private{        // 私有的无参主构造方法, 伴生对象可以调用

    @BeanProperty var gender: String = _

    private def this (name: String){
        this()  // () 可以省略
        println(name)
    }

    def this(name: String, age: Int = 18){
        this(name)
        println(age)
    }

    /**
     * java-1: java 的子类构造函数默认在第一行都会执行 super(args) 以实现调用父类构造函数从而实现父类对象的构造。
     * scala-1: 伴生对象中定义的类是内部静态类 ？？？？
     * scala-2: 类的主构造方法的参数列表在类的整个类体内都可以使用，但是脱离类体之外是不能访问的, 比如类的实例去访问。
     * scala-3: 为了让作用域如此之广的类参数能够被“保留”从而让类实例调用，scala 允许在声明参数时，
     *          加上var 或者 val 关键字，将其提升为类属性，甚至加访问权限修饰符限制其访问权限。
     *          虽然参数都是 val 的，但是在声明类参数时加 val 会将类参数提升为类实例属性,通过类实例访问,
     *          但是如果不加 var 和 val 则类实例是无法访问的.
     *
     */
    class Animal(animalClass: String){
        println("父类主构造方法代码块 ", s"class: $animalClass")
        // NOTE 重载主构造函数，IDE 无法检测出来错误，只有编译时才会报错 ambiguous reference to overloaded definition
        // def this(animalClass: String){this(animalClass); println("父类辅助构造方法代码")}
    }

    // 可从主构造方法的参数列表传入参数到父类主构造方法,也可以传递一个常量
    class Dog(var name: String, age: Int, val gender: String = "male") extends Animal(name){
        println("子类主构造方法代码块 ", s"name: $name")
        private[this] var defaultAge: Int = _                   // TODO 这种修饰的作用是？
        if (age != 0){
            defaultAge = age
        }

        name = "defaultName"
        // NOTE age = 0 ERROR: Reassignment to val

        def this(age: Int)={
            this("lulu", age)
            println("辅助构造方法调用主构造方法 ", s"age: $age")
        }
    }

    // -----------------------------------------------------------------------------------------------------------------


    def classRemain(): Unit ={
        val v = classOf[Dog]   // java 中获取一个类的 Meta 信息 Object.class object.getClass()
        println(v.getInterfaces.length)
        type Xxx = Dog         // NOTE 给类其别名, 给包其别名

        // 比较对象是否相等 (HashTable)
        // == 默认比较对象地址值的 HashCode
        // THINK 重写 equals 和 hashcode  (重写 hashcode 的意义 ?????????)

        val xxx = new Xxx(12)
        xxx.isInstanceOf[Dog]   // 类型判断
        xxx.asInstanceOf[Dog]   // 强制转换

        val `class` = ""
        println(`class`.charAt('A'))
    }

    // google 三大篇  (GFS, BIgTable, )
}
