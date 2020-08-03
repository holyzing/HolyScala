package com.holy.scala

import scala.util.control.Breaks

object Clause {
    def main(args: Array[String]): Unit = {
        /**
         * 算术运算符    +     -   *   /   %
         * 关系运算符    ==    !=  >   <   >=  <=
         * 逻辑运算符    &&    ||    !
         * 位运算符      &     |     ^    ~  <<  >>  >>>
         * 赋值运算符    =     +=    -=   *=   /=    %=    <<=    >>=    &=  ^=  |=
         *
         * () [] ,
         *
         * Java是指令式风格，Scala是函数式风格。
         * 在Scala中，应该尽量使用循环，即是应用函数的方式来处理。
         * Scala并没有提供break和continue语句来退出循环，那么如果我们又确实要怎么办呢，有如下几个选项：
         *   1. 使用Boolean型的控制变量。
         *   2. 使用嵌套函数，从函数当中return
         *   3. 使用Breaks对象中的break方法(这里的控制权转移是通过抛出和捕获异常完成的，尽量避免使用这套机制)
         *   4. 递归函数重写循环
         */
        // if： scala 的if 语句和java的if语句完全一样

        // while
        import util.control.Breaks.{break, breakable}
        breakable {
            while (3 > 2){
                println("outer while start")
                do {
                    println("inner while break before")
                    // break()  scala 中 函数带 （）和 不带（）的区别 是 callable
                    break
                    println("inner while break after")
                } while (3>2)
                println("outer while break before")
                break
                println("inner while break after")
            }
        }

        // TODO 符号函数

        println("--------------------------------------------")
        for (i <- 1 to 3){  // 首尾包含
            for (j <- 1 until 2){  // 不包含 10
                println(i * j)
            }
        }
        println("--------------------------------------------")
        for (i <- 1 to 3; j <- 1 until 3; if i == j; if i > 1){
            println(i * j)
        }
        println("--------------------------------------------")  // idea 类型检查这么强大 ？
        var retVal = for { a <- List(1, 2, 3); if a != 3; if a < 2 } yield a
        println(retVal, retVal.getClass, retVal.getClass())
        for (i <- retVal){
            println(i)
        }

        // scala  的语法还是挺灵活的
        val scala_break = new Breaks
        scala_break.breakable {
            scala_break.break()
        }
    }
}
