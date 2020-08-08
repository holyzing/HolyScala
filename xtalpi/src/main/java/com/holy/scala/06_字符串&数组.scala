package com.holy.scala

import scala.runtime.Nothing$

/**
 * 在 Scala 中，字符串的类型实际上是 Java String，它本身没有 String 类。
 * 在 Scala 中，String 是一个不可变的对象，所以该对象不可被修改。这就意味着你如果修改字符串就会产生一个新的字符串对象。
 *
 * scala 变量的类型在编译的时候就已经确定了，即使定义时不指定变量类型，也会在编译期推倒出变量类型，这与python的变量是有很大不同的，
 * python 的变量则完完全全是由 变量引用的值决定的。这也造成了两种语言 垃圾回收机制的完全不同。
 *
 * 在ubuntu 中使用 idea ，fctix类型的输入法不能跟随光标。
 */

object MyString{
    /**
     * TODO scala 中哪些函数对应 python 中 的 map filter reduce 函数 ？
     * TODO Any 和 AnyRef 的区别
     * TODO new Class() 和 Class() 的区别
     */
    def main(args: Array[String]): Unit = {
        // import java.lang.{String, StringBuffer, StringBuilder}
        printf("浮点数：%f 整形：%d 字符串：%s", 1.0, 1, "1")
        println(String.format("浮点数：%f 整形：%d 字符串：%s", 2.0, 2, "2"))

        /**
         * Scala 语言中提供的数组是用来存储固定大小的同类型元素.
         */
        val scalaArr: Array[String] = new Array[String](5)
        // val javaArr = new String[]{} scala 中是无法定义java数组的, 不支持 java 语法
        // val applyArray: Array[Int] = Array(1, "a")  // 运行时错误
        val applyArray: Array[Int] = Array(1, 4, 2)
        println(applyArray(1))

        var total = 0.0
        for (e <- applyArray){
            total += e
        }
        var max = applyArray(0)
        for (i <- 1 until applyArray.length){ // i <- 1 to applyArray.length - 1
            if (applyArray(i) > max){
                max = applyArray(i)
            }
        }
        println(total, max)

        val multiDimArr = Array.ofDim[Int](3,2)
        val oneDimArr = Array.ofDim[Int](1,6)
        for (i <- multiDimArr){
            for (j <- i){
                // oneDimArr.appended(j) // 运行时错误 ？？？？
                print(j)
            }
        }

        val rangeArr1 = Array.range(1,10)
        val rangeArr2 = Array.range(2, 5, 2)
        val concatArr = Array.concat(rangeArr1, rangeArr2)
        println(concatArr.repr)

        /**
         * 可变集合：
         *      List - 线性方式存储，元素可重复
         *      Set  - 无需存储，元素不可重复 hashcode 不能重复
         *      Map  - 键是一个集合，值是一个列表
         * 不可变集合：Tuple
         * Iterator  Option
         */
        val typeNone = None       // object None extend object Option
        val typeSome = Some      // object Some extend object Option
        val typeOption = Option  // object Option
        val keyNull = null       // keyword

        val capitals = Map("France"->"Paris", "Japan"->"Tokyo", "China"->"Beijing")
        println(capitals get "France", capitals.get("Japan"))
        println((capitals get "France").get, capitals.getOrElse("Americn", "wanisha"))

        def showCapital(x: Option[String]) = x match {
            case Some(s) => s  // 乱七八糟的返回模式 ？ scala 糟糕的语法
            case None => "?"
        }

        for (c <- capitals.get("Americn")){
            println(c.length)
        }

        println(showCapital(capitals.get("not exist")))

        for (i <- scalaArr.indices){  // def indices: Range = 0 until length
            scalaArr(i) = i.toString
        }
        println(scalaArr.repr)
        val mapArr = scalaArr.map(_*2).map(_+2)
        println(mapArr.repr)
        mapArr.foreach(println)

        val optionSome1: Some[Int] = Some(5)
        val optionNone1: Option[Nothing] = None     // does not take parameters
        val optionSome2: Option[Int] = Option(5)
        val optionNone2: Option[Unit] = Option()

        println(optionNone1, optionNone2.get, optionSome1.get, optionSome2.get)

    }

    def listTest(): Unit ={
        val site: List[String] = List("Runoob", "Google", "Baidu")
        val nothingList: List[Nothing] = List()  // keyword Nothing
        val doubleDimList: List[List[Int]] = List(List(1, 2, 3), List(4, 5, 6), List(7, 8, 9))

        // Nil, ::
    }
}

class MyString{

}
