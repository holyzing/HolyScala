package com.holy.scaler

object ScalaSign {
    def main(args: Array[String]): Unit = {
        val la = Array("hello you","hello me","hello world")
        val bigArr: Array[String] = la.flatMap(_.split(" "))
        val sameArr: Array[Array[String]] = la.map(_.split(" "))

        val l: List[Int] = List(1, 2, 3, 4, 5)
        val c: List[Char] = l.flatMap(_.toString)
        val s: List[Char] = l.flatMap(a => String.valueOf(a))
    }
}
