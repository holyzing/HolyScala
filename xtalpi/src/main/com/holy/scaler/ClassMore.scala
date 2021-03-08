package com.holy.scaler

object ClassMore {

    case class Person(name: String, age: Int)

    def main(args: Array[String]): Unit = {
        val cm: ClassMore = new ClassMore()
        val son: cm.Dog = new cm.Dog("lulu")
        println(son.name)
    }

    def matchTest(): Unit ={
        val x: Any = 2
        val res = x match {
            case 0 | "" => false
            case 2 | 4 | 6 | 8 | 10 => println("偶数")
            case x if x == 2 || x == 3 => println("two's company, three's a crowd")
        }
        println(res)

        def anyMatch(x: Any): Any = x match {
            // match 表达式通过以代码编写的先后次序尝试每个模式来完成计算，只要发现有一个匹配的case，剩下的case不会继续匹配
            case 1 => "one"
            case "two" => 2
            case y: Int => s"scala.Int $y"   // 高级匹配
            case _ => "any"              // default
        }
        println(anyMatch())  // 参数列表为 Any 的时候 可以不传 ？？？

        val alice = Person("Alice", 25)
        val bob = Person("Bob", 32)
        val charlie = Person("Charlie", 32)

        for (person <- List(alice, bob, charlie)) {
            person match {
                case Person("Alice", 25) => println("Hi Alice!")
                case Person("Bob", 32)   => println("Hi Bob!")
                case Person(name, age)   => println("Age: " + age + " year, name: " + name + "?")
            }
        }
    }
}

class ClassMore {
    /**
     * java-1: java 的子类构造函数默认在第一行都会执行 super(args) 以实现调用父类构造函数从而实现父类对象的构造。
     * scala-1: 伴生对象中定义的类是内部静态类 ？？？？
     * scala-2: 类的主构造方法的参数列表在类的整个类体内都可以使用，但是脱离类体之外是不能访问的, 比如类的实例去访问。
     * scala-3: 为了让作用域如此之广的类参数能够被“保留”从而让类实例调用，scala 允许在声明参数时，
     *          加上var 或者 val 关键字，将其提升为类属性，甚至加访问权限修饰符限制其访问权限。
     *
     */
    class Animal(animalClass: String){
        println("父类主构造方法代码块 ", s"class: $animalClass")
        // NOTE 重载主构造函数，IDE 无法检测出来错误，只有编译时才会报错 ambiguous reference to overloaded definition
        // def this(animalClass: String){this(animalClass); println("父类辅助构造方法代码")}
    }

    class Dog(var name: String) extends Animal(name){    // 可从主构造方法的参数列表传入参数到父类主构造方法,也可以传递一个常量
        println("子类主构造方法代码块 ", s"name: $name")
        private[this] var age: Int = _                   // TODO 这种修饰的作用是？

        def this(age: Int)={
            this("lulu")
            println("辅助构造方法调用主构造方法 ", s"age: $age")
        }

        def this(name: String, age: Int)={
            this(name)
            this.age = age
            println("辅助构造方法调用主构造方法 ", s"name: $name age: $age")
        }
    }
}


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

// 使用了case关键字的类定义就是就是样例类(case classes)，样例类是种特殊的类，经过优化以用于模式匹配.

/**
 * 构造器的每个参数都成为val，除非显式被声明为var，但是并不推荐这么做；
 * 在伴生对象中提供了apply方法，所以可以不使用new关键字就可构建对象；
 * 提供unapply方法使模式匹配可以工作；
 * 生成toString、equals、hashCode和copy方法，除非显示给出这些方法的定义。
 */
