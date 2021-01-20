package com.holy.scaler.innerPackage

// public final class ScalaSign
object ScalaSign {
    /**
     * 將 该文件内的 scala 语法编译在 ScalaSign$ 的字节码中，并通过静态成员的引用，层层引用直到 java 语法。
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
     */
    def main(args: Array[String]): Unit = {
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
    }
}

class ScalaSign{

}
