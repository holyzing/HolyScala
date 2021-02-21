package com.holy.scaler

import org.junit.Test


// public final class Object
object Operator {
    /**
     * Java 是一个面向对象的语言，但不是完全的，因为它还存在 基本类型 static null 等非对象引用。
     * 为了支持 java 中的静态访问，scala 提供了一个 object 关键字来告诉编译器 在编译 object 修饰的结构生成如下两个类
     *
     * 最终虚构类 （以$结尾）：
     *     1- 虚构类的公有的静态实例 public static Test$ MODULE$;
     *     2- 自身定义的成员变量与成员常量以及成员方法，成员变量和成员常量均是private，
     *        成员方法是 public, 但是成员常量用 final 修饰
     *        private final String val1;
     *        private String var1;
     *     3- 为成员变量和常量生成同名无参方法，并返回成员变量和常量
     *     4- 为成员变量 提供类似 var1_$eq 的setter 方法，下划线前市变量名，下划线后是 $eq
     *     5- 局部变量和常量均被编译为普通的成员变量
     *     6- 生成私有的无参构造函数，函数体中对成员变量和常量进行了初始化：
     *         MODULE$ = this;
     *         this.val1 = "伴生对象常量";
     *         this.var1 = "伴生对象变量";
     *     7- 将main方法编译为普通的main方法 public void main(String[] args)
     *     8- 生成一个静态代码块，静态代码块中调用 无参构造，new Test$()
     *
     *  伴生类：
     *     1- 自身定义的成员变量与成员常量以及成员方法，成员变量和成员常量均是private，
     *        成员方法是 public, 但是成员常量用 final 修饰
     *        private final String val1;
     *        private String var1;
     *     2- 为成员变量和常量生成同名无参方法，并返回成员变量和常量
     *     3- 为成员变量 提供类似 var1_$eq 的setter 方法，下划线前市变量名，下划线后是 $eq
     *     4- 编译器自动为 伴生对象中生成的 同名方法（getter）和 *_$eq (setter) 方法 以及普通成员方法，
     *        在伴生类中都会为其生成一个相同签名的静态方法。函数体中通过单例对象调用 伴生对象中相同签名的普通方法。
     *        当然也包括 main 方法。
     *
     *  1- 由于scala 编译器会为伴生类和伴生对象中的成员变量和常量生成同名方法，
     *     所以在 scala中 成员变量和常量与方法不能同名，而在java中是可以的。
     *  2- 伴生对象和伴生类中不能同时定义 main 方法。
     *  3- 如果伴生对象中和伴生类中定义了同名的成员，则不会为其生成对应的静态方法。
     *  4- 伴生对象不包含任何伴生类的信息。
     *  5- 伴生对象天生就是单例对象。
     *  6- TODO 为什么在伴生类中定义构造函数（无参构造与带参构造），
     *     TODO 就不能直接通过 伴生对象引用其成员，构造函数 与 apply 函数之间的关系？
     *     TODO 伴生类中的构造函数 与 伴生对象的关系？
     *     TODO 伴生对象中定义的构造函数是怎么样的 ？
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
        val b: Byte = 1
        println(b)
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
        println(s"a=$a b=$b sum=${a + b}")
        a = a ^ b   // crc 网络数据校验中用到很多抑或操作
        b = a ^ b   // 而在实际计算中则会用到很多 & 运算
        a = a ^ b
        println(s"a=$a b=$b")
    }
    //NOTE scala 中没有 三目运算符,因为在java 中包含三目运算符的表达式可能有值也可能没值,容易产生歧义
    //NOTE 三目运算符完全可以用 if ... else ... 来实现
}
