package com.holy.scaler

import java.io.{FileNotFoundException, FileReader, IOException}

import org.junit.Test

// Scala 抛出异常的方法和 Java一样，使用 throw 方法，例如，抛出一个新的参数异常：

/**
 * 异常捕捉的机制与其他语言中一样，如果有异常发生，catch字句是按次序捕捉的。
 * 因此，在catch字句中，越具体的异常越要靠前，越普遍的异常越靠后。
 * 如果抛出的异常不在catch字句中，该异常则无法处理，会被升级到调用者处。
 */

object MatchAndExceptionAndImplicit {
    def main(args: Array[String]): Unit = {
        try {
            val f = new FileReader("input.txt")
            f.close()
        } catch {
            case ex: FileNotFoundException =>
                println("Missing file exception", ex)
            case ex: IOException =>
                println("IO Exception", ex)
            case ex:Throwable => // Execption extends Throwable
                println("Not predicted exception！", ex)
        } finally {
            println("Exiting finally...")
        }
        /**
         * java 中 try catch 中的异常是有大小范围 的，而异常的捕获是从上到下执行，
         * 所以得要求先捕获范围小的异常，在捕获范围大的异常。
         *
         * 而在 scala 中通过 模式匹配进行的异常捕获，是从上到下进行依次捕获，直到捕获为止，
         * 不考虑捕获异常的顺序，相当于 java 中 的 switch case break
         * 但是在实际编程中，显然应该是越具体的异常越靠前越好
         *
         * scala 中没有 checked 异常，都是在运行时进行捕获处理
         *
         * 模式匹配下的 case 可以省略 { }
         *
         */

        def testNothing(): Nothing ={
            throw new Exception("Wrong")
            // throw 表达式的类型是 Nothing 它是所有类型的子类型，所以 throw 表达式可以用在需要类型的地方。
        }

        // testNothing()

        try{
            testNothing()
        } catch {
            case e: Exception =>
                e.printStackTrace()
            case nfe: NumberFormatException =>
                nfe.fillInStackTrace()
        }

        @throws(classOf[NumberFormatException])
        def numberFormatToStr(numberStr: String): Int ={
            numberStr.toInt
        }

        try{
            numberFormatToStr("hhh")
        } catch {
            case nfe: NumberFormatException =>
                println("错误的数字字符串")
                nfe.printStackTrace()
        }

        numberFormatToStr("abc")

        /**
         * scala 提供了 throws 类（注解）来在定义一个函数时，声明一个函数在执行的过程中可能抛出的异常，
         */
    }
}

class MatchAndExceptionAndImplicit{
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

    // ---------------------------------------------------------------------------------------
    // scala 默认情况下支持的隐式转换
    // 1-- 数值类型的类型提升 各个数值类型中都定义了它们的隐式转换
    // 2-- 多态语法下的类型转换，具体转抽象，子类转父类等
    // 3-- 当前环境下（作用域）不能出现多个相同签名（入参类型和返回类型一样）的隐式转换函数

    @Test
    def implicitTest(): Unit ={
        // 隐式函数 底层是 private final
        implicit def floatToInt(f: Float): Int ={f.toInt}
        val i: Int = 5.0f   // OCP 对功能扩展开放，对修改源码关闭
        println(i.toFloat)

        class Mysql{def select(): Unit ={println("select func")}}
        class Db{def delete(): Unit ={println("delete func")}}
        implicit def transform(mysql: Mysql): Db = {new Db}
        val mysql = new Mysql // 无参构造
        mysql.select()
        mysql.delete()        // THINK 找不到这个方法的时候就会隐式转换 ？？？？

        def sayname(name:String = "张三"): Unit ={println(name)}
        // sayname  // Missing arguments for method sayname(String)
        sayname()

        // implicit val age2: Int = 20  // ambiguous implicit val
        implicit val age: Int = 20
        // 隐式参数
        def sayage(implicit age:Int = 18): Unit ={
            println(age)
        }
        sayage      // 因为隐式参数，所以 () 可以省略
        sayage()
        sayage(19)

        implicit class IO(mysql: Mysql){def drop(): Unit ={mysql.delete(); println("drop func")}}
        mysql.drop()
    }

    @Test
    def regexTest() {
        import scala.util.matching.Regex
        val pattern = "Scala".r()              // 构造 Regex 对象
        val pattrer2 = new Regex("(S|s)cala")  // 管道(|)来设置不同的模式, 首字母可以是大写 S 或小写 s
        val str = "Scala is Scalable and cool"
        val str2 = "Scala is scalable and cool"
        println(pattern findFirstIn str, (pattern findAllIn str2).mkString(","))   // 使用逗号 , 连接返回结果
        println(pattrer2 replaceFirstIn(str, "Java"), pattern replaceAllIn(str2, "Java"))

        // Scala 的正则表达式继承了 Java 的语法规则，Java 则大部分使用了 Perl 语言的规则
    }
}
