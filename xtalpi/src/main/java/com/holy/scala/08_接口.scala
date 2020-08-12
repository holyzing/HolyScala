package com.holy.scala

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

trait Equal {
    def isEqual(x: Any): Boolean
    def isNotEqual(x: Any): Boolean = !isEqual(x)
}

class Point2(xc: Int, yc: Int) extends Equal {
    var x: Int = xc
    var y: Int = yc

    def isEqual(obj: Any): Boolean = {
        obj.isInstanceOf[Point] && obj.asInstanceOf[Point].x == x
    }
    // match 表达式通过以代码编写的先后次序尝试每个模式来完成计算，只要发现有一个匹配的case，剩下的case不会继续匹配
    val matchValue: String = 1 match {
        case 1 => "1"
        case 2 => "2"
    }

    def matchTest(x: Any): Any = x match {
        case 1 => "one"
        case "two" => 2
        case y: Int => "scala.Int"   // 高级匹配
        case _ => "any"              // default
    }
}

object Point2{

    // 使用了case关键字的类定义就是就是样例类(case classes)，样例类是种特殊的类，经过优化以用于模式匹配.
    case class Person(name: String, age: Int)

    def main(args: Array[String]): Unit = {
        val p:Point2 = new Point2(xc = 1, yc = 2)
        println(p.matchValue, p.matchTest(2))

        val alice = new Person("Alice", 25)
        val bob = new Person("Bob", 32)
        val charlie = new Person("Charlie", 32)

        for (person <- List(alice, bob, charlie)) {
            person match {
                case Person("Alice", 25) => println("Hi Alice!")
                case Person("Bob", 32)   => println("Hi Bob!")
                case Person(name, age)   => println("Age: " + age + " year, name: " + name + "?")
            }
        }
        val x: Any = 2
        val res = x match {
            case 0 | "" => false
            case 2 | 4 | 6 | 8 | 10 => println("偶数")
            case x if x == 2 || x == 3 => println("two's company, three's a crowd")
        }
        println(res)

        /**
         * 构造器的每个参数都成为val，除非显式被声明为var，但是并不推荐这么做；
         * 在伴生对象中提供了apply方法，所以可以不使用new关键字就可构建对象；
         * 提供unapply方法使模式匹配可以工作；
         * 生成toString、equals、hashCode和copy方法，除非显示给出这些方法的定义。
         */
    }
}
