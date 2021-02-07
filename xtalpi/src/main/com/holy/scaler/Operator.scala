package com.holy.scaler

import org.junit.Test

import scala.util.control.Breaks

// public final class Object
object Operator {
    /**
     * 將 该文件内的 scala 语法编译在 Operate$ 的字节码中，并通过静态成员的引用，层层引用直到 java 语法。
     * Java 是一个面向对象的语言，但不是完全的，因为它还存在 基本类型 static null 等非对象引用。
     * 为了支持 java 中的静态访问，scala 提供了一个 object 关键字来告诉编译器 在编译 object 修饰的结构的时候
     * 编译成两个 类，一个是最终类，它包含object 中定义了被添加了 static 修饰的成员，另一个是普通类，包含object中成员的实现，
     * 最终类通过一个静态成员 Module 引用普通类的实例，从而引用对应普通类中的普通成员，在scala 中被object所修饰的结构称为
     * 伴生对象，但其实它是一个最终类，通过这样的方式，scala中支持了使用类名访问成员的操作。
     *
     * scala 中默认的成员都是 public 的，所以没有public 关键字  但支持了 private 和 protected，
     * scala 中采用特殊对象 Unit 来模拟  java 中 的关键字 void 来表示没有返回
     *
     * scala 中使用 def 定义一个成员方法
     * scala 中方法的签名与方法体之间使用 = 连接，是因为 scala中的方法也是一个对象，方法体就是 new 一个对象，然后赋值给变量去引用。
     * scala 中不强调类型，所以在声明一个类型的时候将类型写在后边，甚至可以不写，但却又是强类型的，所以它编译的时候会进行类型推断
     *
     * scala 编译器在 默认情况下 总会引入 java.lang._ 、 scala._ 和 Predef._ 这也是 可以直接 使用println 等基础函数的原因
     */

    def main(args: Array[String]): Unit = {
        /**
         * 算术运算符    + (正号, 连字符)  - (负号)   *   /   %
         * 关系运算符    ==    !=  >   <   >=  <=
         * 逻辑运算符    &&    ||    !
         * 位运算符      &     |     ~    ^    <<    >>    >>>
         * 赋值运算符    =     +=    -=   *=   %=    <<=    >>=    &=  ^=  |=
         *
         * () [] ,
         *
         * scala 中是没有 ++ 和 -- 操作符的, 和python 是一样的, int a=0 ++a a++ --a a-- 这种操作符容易产生歧义,
         * 赋值运算符的本质 是将 右边的 "计算结果" 赋值给左边,强调的是计算也就是运算,
         * a=a++ 中 a的结果是 0, because The value changed at 'a++' is never used
         * 而实际经过反编译后的赋值运算步骤是存在中间变量的:
         *      a=a++ => temp = a++; temp = a; a++ => temp = 0; a=1; a = temp = 0
         * 但是在字节码层面则不是这样的,而是通过 栈与本地变量表之间实现的 slot
         *
         */

        // TODO 符号函数
    }
}

class Operator{
    def underlineTest(): Unit = {
        val la = Array("hello you","hello me","hello world")
        val bigArr: Array[String] = la.flatMap(_.split(" "))
        val sameArr: Array[Array[String]] = la.map(_.split(" "))
        bigArr.foreach(println)
        sameArr.foreach(println)

        val l: List[Int] = List(12, 22, 33, 44, 55)
        val c: List[Char] = l.flatMap(_.toString)
        val s: List[Char] = l.flatMap(a => String.valueOf(a))
        val ss: List[String] = l.map(_.toString)
        println(l, c, s, ss)

        val words1 = bigArr.map((_, 1))
        val words2 = bigArr.map(x => (x, 1))
        println(words1.length, words2.length)
        println(s"name=${"lulu"}, age=${26}, gender=${'F'}")
        println(f"name=${"lulu"}, age=${26.0}%.2f, gender=${'F'}")
        println(raw"\n, ${2.0}%.2f \t")
    }

    @Test
    def arithmeticOperator(): Unit ={
        val v: Int = 10 / 3         // NOTE 整数除只保留整数 和 浮点数除保留浮点数的精度
        println(v, 10 / 3, 10.0 / 3, 10 / 3.0)
        var b: Byte = 1
        // b = b + 1                // NOTE java 和scala 的编译器都会直接报错
        // b += 1                   // NOTE 运行时赋值运算符左侧类型提升,无法赋值给精度更小的类型,报错. 但是java中会进行精度缩减
    }

    @Test
    def compareOPerator(): Unit ={
        var c = 10
        if (c == 10){
            println("true")         //NOTE 在 JS 中 s=2 的结果是true,这也是函数式编程的特点
        }else{
            println(c=10)           //NOTE scala 的所有表达式都是有值的,包括赋值表达式, 但是java中的赋值表达式是没有值的.
        }
    }

    @Test
    def logicOperator(): Unit ={
        val A = 0
        val B = 1
        val D = true
        val E = false
        println(D && E, D || E, D^E, !D)
        println(A & B, D | E)
    }

    @Test
    def byteOPerate(): Unit ={
        var a = 10
        var b = 20
        println(s"a=${a} b=${b}")
        a = a ^ b   // crc 网络数据校验中用到很多抑或操作
        b = a ^ b   // 而在实际计算中则会用到很多 & 运算
        a = a ^ b
        println(s"a=${a} b=${b}")
    }
    //NOTE scala 中没有 三目运算符,因为在java 中包含三目运算符的表达式可能有值也可能没值,容易产生歧义
    //NOTE 三目运算符完全可以用 if ... else ... 来实现
}
