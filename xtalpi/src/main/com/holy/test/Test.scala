package com.holy.test

object  Test{

    val val1 = "伴生对象常量"
    var var1 = "伴生对象变量"
    var objectVar = "object 独有的变量"

//    def Test(): Unit ={
//
//    }

    def main(args: Array[String]): Unit = {
        var areaVar = "局部变量"
        val areaVal = "局部常量"
        areaVar = "局部变量"
        println(s"伴生对象的main方法 $areaVal, $areaVar")
        args.foreach(println)

        val t = new Test()
        t.method()
        println(t.val1, t.var1)
        Test.objectMethod()
    }

    def objectMethod(): Unit ={
        println("伴生对象独有的方法")
    }

    def method (): Unit ={
        println("伴生对象的 method")
    }

}

class  Test{
    val val1 = "伴生类常量"
    var var1 = "伴生类变量"
    var classVar = "class 独有的变量"

//    def Test(): Unit ={
//
//    }
//
//    def Test(s: String): Unit ={
//        this.var1 = s
//    }

    def classMain(args: Array[String]): Unit = {
        println("伴生类的main方法")
        args.foreach(println)
    }

    def classMethod(): Unit ={
        Test.objectMethod()
    }

    def method(): Unit ={
        println("伴生类的 method")
    }
}

