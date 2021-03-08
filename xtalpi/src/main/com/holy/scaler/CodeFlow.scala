package com.holy.scaler

import com.holy.test
import com.holy.test.JavaTest
import org.junit.Test

import scala.util.control.Breaks

object CodeFlow {
    /**
     * 顺序控制中变量的引用需要是合法的的前向引用.
     */
}

class CodeFlow {
    /**
     * Java是指令式风格，Scala是函数式风格。
     * 在Scala中，应该尽量使用循环，即是应用函数的方式来处理。
     * Scala并没有提供break和continue语句来退出循环，那么如果我们又确实要怎么办呢，有如下几个选项：
     *      1. 使用Boolean型的控制变量。
     *      2. 使用嵌套函数，从函数当中return
     *      3. 使用Breaks对象中的break方法(这里的控制权转移是通过抛出和捕获异常完成的，尽量避免使用这套机制)
     *      4. 递归函数重写循环
     */

    def whileTest(): Unit = {

        val con = 5

        val whileValue: Unit = while(con % 2 == 0){
            println(con)
        }
        println(whileValue)
        // NOTE 因为 while 循环体内会不可避免的使用外部变量进行计算以及控制循环, 所以在 scala 中是不推荐使用的.
        // NOTE java 中有 label 和 break可以使得在内层循环中直接跳出到外层循环, 但是scala作为完全面向对象的语言是不支持的.
        // NOTE scala 的特性也导致其 没有 continue 这样的关键字 实现结束当前循环的功能, 而只能通过 if else 实现循环的控制.

        import scala.util.control.Breaks.{break, breakable}
        breakable {
            while (3 > 2) {
                println("outer while start")
                do {
                    println("inner while break before")
                    // break()  scala 中 函数带 （）和 不带（）的区别 是 callable
                    break
                    println("inner while break after")
                } while (3 > 2)
                println("outer while break before")
                break
                println("inner while break after")
            }
        }
    }

    @JavaTest
    def forTest(): Unit = {
        /**
         * java 中的循环方式有两种: for(int i=1; i < 10; i++) ; for(item: iterableCollection)
         * 普通for 循环的三个表达式 均可以不写, 可以再循环体内做对应操作
         */

        // for 推导式 表达式 生成式
        for (i <- 1 to 3; j <- 1 until 2) {
            println(s"i * j = ${i * j}")
        }

        // NOTE python 中 基于 range 的序列循环是有 steps 参数的,
        // NOTE 而java 中的普通for 循环是通过第三表达式来控制步进的
        // NOTE 当然也可以像 Java 一样 在scala中通过 "循环守卫" 控制步长

        val l: Range = 0.until(1)
        for (i <- l){
            println(i.+(3))
        }
        for (i <- Range(1, 18, 2)){
            val j = (18 - i) / 2
            println(" " * j + "*" * i + " " * j)
        }

        for (i <- Range(1, 18, 2); j = (18 - i) / 2){
            println(" " * j + "*" * i + " " * j)
        }

        for {i <- Range(1, 18, 2)  // 灵活的语法格式 for 表达式签名列表可以使用 {} 组织
             j = (18 - i) / 2 } {
            println(" " * j + "*" * i + " " * j)
        }

        // NOTE 一般的 for 循环表达式的值都是 Unit 的实例 ()
        val forValue: Unit = for(i <- 1 to 3) {println(i)}
        val yieldValues: Seq[Int] = for(i <- 1 to 10) yield i * 2  // 序列生成器
        println(forValue, yieldValues)

        val retVal = for {a <- List(1, 2, 3); if a != 3; j = a * 2 if a < 2} yield j
        println(retVal, retVal.getClass)

        // NOTE scala 使用循环守卫控制 for 循环的流转,没有 java 中 的 continue 方便
        // NOTE scala 中是没有 break 关键字控制 一个循环直接结束.但是完全面向对象的scala
        // NOTE 提供了对象 Breaks 来 中断循环

        Breaks.breakable {
            for (i <- 1 to 10; if i > 5){
                if (i == 8) {
                    Breaks.break()      // object Breaks extends Breaks
                }
                println(i)
            }
        }

        import scala.util.control.Breaks._
        breakable {
            for (i <- 1 to 10; if i > 5){
                if (i == 8) {
                    break
                }
                println(i)
            }
        }

        println("--------- for ending ---------")

        // NOTE java 中的静态导入 import static com.Test.*
    }

    @JavaTest
    def ifTest(): Unit = {
        /**
         * NOTE scala 中 if 语句的 判定表达式的值类型必须是 Boolean 类型,而 python 则不是.
         * 对象的比较一般用 equals 比较, 用 ==, 在字符串的比较过程中 由于字符串常量池的存在,
         * 而 == 比较的是 hashcode,所以无法确切的比较 两个字符串的内容是否相同
         */
        val s1 = new String("s")

        if (s1 == "s") {
            print("s1 == \"s\"")
        } else {
            val s2 = "s"
            if (s1 == s2) {
                print("s1 == s2")
            }
        }

        // NOTE scala 中的任何表达式 都是有值的, 而 if else 也算是表达式, 它的值是其代码体内最后一条被执行的表达式的值.
        // NOTE 由于 if 语句的执行路径是不确定的, 所以其返回结果也是不确定的, 如果各个分支的返回类型不一致,则 最终的返回类型是 Any
        val r: Unit = if (s1 == "s") print("单行 body")
        val r2 = if (true) 3

        val value: AnyVal = if (s1.equals("a")) 1
        print(r, r2, value)
        // NOTE 一般嵌套分支不要超过三层
    }

    @JavaTest
    def matchCase(): Unit ={
        /**
         * 可以代替 Java 中 的 switch, java 的Switch 存在一个 穿透问题, 其实就是忘记写 break,被依次执行.
         */

    }
}
