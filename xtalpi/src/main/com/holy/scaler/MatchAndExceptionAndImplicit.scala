package com.holy.scaler

import java.io.{FileNotFoundException, FileReader, IOException}

import org.junit.Test

/**
 * _ 的作用与意义：
 *      模式匹配 代表 default 的含义
 *      让虚拟机给定义的变量赋予初始值
 *      导包时候的全部导入，等价于java中的通配符 ×
 *      各类起别名，隐藏类
 *      当参数只被引用一次的时候，代替方法的参数
 *      让方法以方法返回，而不是执行方法
 *
 *      但是存在含义模糊，无法推断出类型的时候，比如 二维数组的flatmap，
 *      编译器无法推断出 _ 代替的是函数的参数还是函数本身
 *      从而默认会当成函数来对待
 */

class MatchAndExceptionAndImplicit{
    // 使用了case关键字的类定义就是就是样例类(case classes)，样例类是种特殊的类，经过优化以用于模式匹配.
    case class Person(name: String, age: Int)

    @Test
    def _Test(): Unit ={
        val func = Array.ofDim(2, 3).flatMap(_)
        println(func)
    }

    def matchTest(): Unit ={
        /**
         * java 中 的switch 语句支持的数据类型 有 byte(向上) char(ASCII) int string(hashcode equals)
         * 以上四种类型均可以的原因是均可以转为 int 类型
         * 必须使用 break 跳出case 不然会依次顺序执行，所谓 穿透。default不一定就要放在最后，但是会在最后被执行
         *
         * scala 的 match 表达式通过以代码编写的先后次序尝试每个模式来完成计算，只要发现有一个匹配的case，
         * 剩下的case则不会继续匹配，所谓不会穿透。如果最终无法匹配，则抛出 MatchError，但是java不会抛出异常
         * scala 中 case _ 表示无论如何都匹配，和 default 还是有区别，所以它的位置是不能随便调整的。
         */
        val x: Any = 2
        val res = x match {
            case 0 | "" => false
            case 2 | 4 | 6 | 8 | 10 => println("偶数")
            case x if x == 2 || x == 3 => println("two's company, three's a crowd")
        }
        println(res)

        def anyMatch(x: Any): Any = x match {
            case 1 => "one"
            case "two" => 2
            case y: Int => s"scala.Int $y"   // 高级匹配
            case _ => "any"                  // default
        }
        println(anyMatch())                  // 参数列表为 Any 的时候 可以不传 ？？？

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

        for (ch <- "+-3!") { //是对"+-3!" 遍历
            var sign = 0
            var digit = 0
            ch match {
                case '+' => sign = 1
                case '-' => sign = -1
                // 说明..
                // 如果 case 后有 条件守卫即 if ,那么这时的 _ 不是表示默认匹配
                // 表示忽略 传入 的 ch
                case _ if ch.toString.equals("3") => digit = 3
                case _ if ch > 1110 || ch < 120 => println("ch > 10")
                case c => println(c)  // 匹配成功后传递被匹配变量
                case _ => sign = 2
            }
        }

        val a = 8
        //说明 obj 实例的类型 根据 a 的值来返回
        val obj = if (a == 1) 1
        else if (a == 2) "2"
        else if (a == 3) BigInt(3)
        else if (a == 4) Map("aa" -> 1)
        else if (a == 5) Map(1 -> "aa")
        else if (a == 6) Array(1, 2, 3)
        else if (a == 7) Array("aa", 1)
        else if (a == 8) Array("aa")

        val result = obj match {
            case a: Int => a
            case b: Map[String, Int] => s"${b}对象是一个字符串-数字的 Map 集合"
            case c: Map[Int, String] => s"${c}对象是一个数字-字符串的 Map 集合"  // 【】是泛型约束，模式匹配不起作用
            case d: Array[String] => d            //"对象是一个字符串数组" 数组中[] 是类型约束，编译为字节码则是一个对应类型的数组
            case e: Array[Int] => s"${e.mkString(",")}对象是一个数字数组"
            case f: BigInt => println(Int.MaxValue, f.getClass)
            case _ => "啥也不是"
        }
        println(result)

        val arrs = Array(Array(0), Array(1, 0), Array(0, 1, 0), Array(1, 1, 0), Array(1, 1, 0, 1))
        for (arr <- arrs) {
            val result = arr match {
                case Array(0) => "0"
                case Array(x, y) => x + "=" + y
                case Array(0, _*) => "以 0 开头的数组"
                case _ => "什么集合都不是"
            }
            println("result = " + result)
        }

        for (list <- Array(List(0), List(1, 0), List(88), List(0, 0, 0), List(1, 0, 0))) {
            val result = list match {
                case 0 :: Nil => "0"              // 匹配只有一个元素0的列表
                case x :: y :: Nil => x + " " + y // 匹配只有两个元素的列表，且将元素依次赋值给变量 x, y
                case 0 :: tail => tail.size       // 匹配以0开头的元素，是元素的函数，被调用
                case x :: Nil => x                // 匹配只有一个元素的列表
                case _ => "something else"        // 无条件直接匹配
            }
            println(result)
        }

        for (pair <- Array((0, 1), (1, 0), (10, 30), (1, 1), (1, 0, 2))) {
            val result = pair match {
                case (0, _) => "0 ..." // 表示 匹配 Tuple2 第一个元素为 0，第二个任意
                case (y, 0) => y       // 表示 匹配 Tuple2 第一个元素任意，且将第一个元素传递给变量 y，第二个元素是0
                case (x, y) => (y, x)  // 表示 匹配 Tuple2 将两个元素依次赋值给 x, y
                case _ => "other"      // 无条件直接拿匹配
            }
            println(result)
        }
    }


    @Test
    def implicitTest(): Unit ={
        // ---------------------------------------------------------------------------------------
        // scala 默认情况下支持的隐式转换
        // 1-- 数值类型的类型提升 各个数值类型中都定义了它们的隐式转换
        // 2-- 多态语法下的类型转换，具体转抽象，子类转父类等
        // 3-- 当前环境下（作用域）不能出现多个相同签名（入参类型和返回类型一样）的隐式转换函数

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

    @Test
    def exceptionTest(): Unit ={
        /**
         * Scala 抛出异常的方法和 Java一样，使用 throw 方法，例如，抛出一个新的参数异常：
         * 异常捕捉的机制与其他语言中一样，如果有异常发生，catch字句是按次序捕捉的。
         * 因此，在catch字句中，越具体的异常越要靠前，越普遍的异常越靠后。
         * 如果抛出的异常不在catch字句中，该异常则无法处理，会被升级到调用者处。
         */

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
