package com.holy.scaler

import java.io.{File, PrintWriter}

import scala.io.{BufferedSource, Source, StdIn}

/**
 * Scala 进行文件写操作，直接用的都是 java中 的 I/O 类 （java.io.File)：
 */

object IOTest {
    def main(args: Array[String]) {
        val writer = new PrintWriter(new File("test.txt" ))
        writer.write("菜鸟教程")
        writer.close()
        print("请输入菜鸟教程官网 : " )
        // Scala2.11 后的版本 Console.readLine 已废弃，使用 scala.io.StdIn.readLine() 方法代替。
        val line = StdIn.readLine()
        println("谢谢，你输入的是: " + line)

        /**
         * 有一个名为 reset 的方法，但是它所做的就是从文件中重新创建源代码。
         * 内部 Source 创建一个 BufferedSource ，它有一个 close 方法。然而，这不是从Source公开。
         * 我希望Source一旦读取了文件的内容就释放文件句柄，但似乎没有做到这一点。
         * 到目前为止，我所见过的最好的解决方法是从根本上将 Source 转换为 BufferedSource 并调用 close 。
         *
         * 为什么Scala不能提供方便的方法，比如Python中的with子句呢？它看起来很有用但不难。
         */
        val src: BufferedSource = Source.fromFile("test.txt" )
        src.foreach(println)
        src.close()
    }
}
