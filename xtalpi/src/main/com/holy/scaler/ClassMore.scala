package com.holy.scaler

object ClassMore {

    def main(args: Array[String]): Unit = {
        val cm: ClassMore = new ClassMore()
        val son: cm.Dog = new cm.Dog("lulu", 18)
        println(son.name, son.gender)  // son.age
    }

    // 使用了case关键字的类定义就是就是样例类(case classes)，样例类是种特殊的类，经过优化以用于模式匹配.
    case class Person(name: String, age: Int)
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
            case _ => "any"                  // default
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
     *          虽然参数都是 val 的，但是在声明类参数时加 val 会将类参数提升为类实例属性。
     *
     */
    class Animal(animalClass: String){
        println("父类主构造方法代码块 ", s"class: $animalClass")
        // NOTE 重载主构造函数，IDE 无法检测出来错误，只有编译时才会报错 ambiguous reference to overloaded definition
        // def this(animalClass: String){this(animalClass); println("父类辅助构造方法代码")}
    }

    class Dog(var name: String, age: Int, val gender: String = "male") extends Animal(name){    // 可从主构造方法的参数列表传入参数到父类主构造方法,也可以传递一个常量
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
}
