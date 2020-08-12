package com.holy.scala

import java.io.{FileNotFoundException, FileReader, IOException}

// Scala 抛出异常的方法和 Java一样，使用 throw 方法，例如，抛出一个新的参数异常：

/**
 * 异常捕捉的机制与其他语言中一样，如果有异常发生，catch字句是按次序捕捉的。
 * 因此，在catch字句中，越具体的异常越要靠前，越普遍的异常越靠后。
 * 如果抛出的异常不在catch字句中，该异常则无法处理，会被升级到调用者处。
 */

object ExceptionTest {
    def main(args: Array[String]): Unit = {
        try {
            val f = new FileReader("input.txt")
        } catch {
            case ex: FileNotFoundException => {
                println("Missing file exception")
            }
            case ex: IOException => {
                println("IO Exception")
            }
            case ex:Throwable => {  // Execption extends Throwable
                println("Not predicted exception！")
            }
        } finally {
            println("Exiting finally...")
        }
    }
}
