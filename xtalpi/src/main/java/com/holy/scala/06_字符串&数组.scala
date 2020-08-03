package com.holy.scala

/**
 * 在 Scala 中，字符串的类型实际上是 Java String，它本身没有 String 类。
 * 在 Scala 中，String 是一个不可变的对象，所以该对象不可被修改。这就意味着你如果修改字符串就会产生一个新的字符串对象。
 *
 * scala 变量的类型在编译的时候就已经确定了，即使定义时不指定变量类型，也会在编译期推倒出变量类型，这与python的变量是有很大不同的，
 * python 的变量则完完全全是由 变量引用的值决定的。这也造成了两种语言 垃圾回收机制的完全不同。
 */

object MyString{
    def main(args: Array[String]): Unit = {
        // import java.lang.{String, StringBuffer, StringBuilder}
        printf("浮点数：%f 整形：%d 字符串：%s", 1.0, 1, "1")
        println(String.format("浮点数：%f 整形：%d 字符串：%s", 2.0, 2, "2"))

        /**
         * Scala 语言中提供的数组是用来存储固定大小的同类型元素.
         */
        val scalaArr: Array[String] = new Array[String](5)
        // val javaArr = new String[]{} scala 中是无法定义java数组的
        // val applyArray: Array[Int] = Array(1, "a")  // 运行时错误
        val applyArray: Array[Int] = Array(1, 2)
        println(applyArray(1))

        // Any 和 AnyRef 的区别
        // new Class() 和 Class() 的区别
        // apply 函数的作用
        // 在ubuntu 中使用 idea ，fctix类型的输入法不能跟随光标。
    }
}

class MyString{

}
