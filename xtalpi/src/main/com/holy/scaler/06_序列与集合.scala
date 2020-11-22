package com.holy.scaler

import org.junit.Test


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
        println("main is running !")
    }
}

// ctrl (shift) +/-
class MyString{

    @Test
    def arrayTest(): Unit ={
        // import java.lang.{String, StringBuffer, StringBuilder}
        printf("浮点数：%f 整形：%d 字符串：%s", 1.0, 1, "1")
        // TODO println(String.format("浮点数：%f 整形：%d 字符串：%s", 2.0, 2, "2")) // 版本问题 ??

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
         *      Array
         *      List  - 线性方式存储，元素可重复
         *      Set   - 无需存储，元素不可重复 hashcode 不能重复
         *      Map   - 键是一个集合，值是一个列表
         * 不可变集合：Tuple
         * Iterator  Option
         * Seq
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

    @Test
    def listTest(): Unit ={
        // Scala 列表类似于数组，它们所有元素的类型都相同，长度固定  TODO Tuple ？？   (immutable recursive data)
        // 但是它们也有所不同：列表是不可变的，值一旦被定义了就不能改变，其次列表 具有递归的结构（也就是链接表结构）而数组不是。。
        val site1: List[String] = List("Runoob", "Google", "Baidu")
        val nothingList: List[Nothing] = List()  // keyword Nothing
        val doubleDimList: List[List[Int]] = List(List(1, 2, 3), List(4, 5, 6), List(7, 8, 9))

        val site2 = "Runoob" :: ("Google" :: ("Baidu" :: Nil))
        val nums = 1 :: (2 :: (3 :: (4 :: Nil)))
        var empty = Nil

        // 二维列表
        val dim = (1 :: (0 :: (0 :: Nil))) ::
            (0 :: (1 :: (0 :: Nil))) ::
            (0 :: (0 :: (1 :: Nil))) :: Nil
        println(site2, "\n", site2.length, nums.length, "\n", empty, "\n", dim)
        println(site1.head, empty.isEmpty, nums.tail)

        // ::: List.::: List.concat

        val list1 = "A"::("B"::("C"):: Nil)
        val list2 = List("D", "E", "F")
        val list3 = list1 ::: list2
        val list4 = list1.:::(list2)
        val list5 = List.concat(list1, list2)
        println(list3, "\n", list4, "\n", list5)

        val list6 = List.fill(3)("strEle")  // 重读 0
        val list7 = List.fill(3)(2)
        println(list6, "\n", list7)

        println("--------------------------------")
        val squares = List.tabulate(6)(f=n => n * n) // for (i <- 0 until(6)) { fun(i) }
        val mul = List.tabulate(4, 5)(_ * _)

        var listCon:List[Int] = null
        for (i <- 0 until(4); j <- 0 until(5)){
            if (listCon == null){
                listCon = (i*j) :: empty
                println(listCon.hashCode())
            }
            listCon = (i*j) :: listCon
            // print(newLi.hashCode() == empty.hashCode())
        }

        // +: :: ::: :+
        println(squares, "\n", mul, "\n ", empty, mul.reverse, "\n", listCon, listCon.hashCode())

        // TODO  new 的使用场景 ？ 有时候可省略，有时候必须使用，有时候不能使用

    }

    @Test
    def setTest(): Unit ={
        /**
         * 虽然可变Set和不可变Set都有添加或删除元素的操作，但是有一个非常大的差别。对不可变Set进行操作，会产生一个新的set，
         * 原来的set并没有改变，这与List一样。 而对可变Set进行操作，改变的是该Set本身，与ListBuffer类似
         */
        // 并集：++
        // 交集：&    intersect
        // 差集：&~
        // + -
        // 子集： subSetOf
        println("setTest")
    }

    @Test
    def mapTest(): Unit ={  // 静态的 不能使用 Junit 测试
        /**
         * Map 也叫哈希表（Hash tables）。
         * Map 有两种类型，可变与不可变（import scala.collection.mutable.Map），
         * 区别在于可变对象可以修改它，而不可变对象不可以。默认情况下 Scala 使用不可变 Map。
         * 在 Scala 中 你可以同时使用可变与不可变 Map，不可变的直接使用 Map，可变的使用 mutable.Map。
         */
        var A:Map[Char,Int] = Map()
        println(A.hashCode())
        A += ('A'-> 1)
        println(A.hashCode(), A)
        A += ('B'-> 2)
        println(A.hashCode(), A)
        //  合并 map python中使用 update 会原地更新 dict 而在 scala 中 scala.Map 则是不可变的
        A ++= Map('c'->3)
        A = A.++(Map('d'->4))
        println(A.hashCode(), A, A.contains('e'))

        // A = A.-('c')
        A -= 'c'
        println(A.hashCode(), A, A.contains('e'))


    }

    @Test
    def tupleTest(): Unit ={
        // 与列表一样，元组也是不可变的，但与列表不同的是元组可以包含不同类型的元素。
        val t1 = (1, 3.14, "Fred")
        val t2 = new Tuple3(1, 3.14, "Fred")
        // 元组的实际类型取决于它的元素的类型，比如 (99, "runoob") 是 Tuple2[Int, String]。
        // ('u', 'r', "the", 1, 4, "me") 为 Tuple6[Char, Char, String, Int, Int, String]。
        // 目前 Scala 支持的元组最大长度为 22。对于更大长度可以使用集合，或者扩展元组。
        val doubleTuple = ("front", "back")  // 对于二元组 则会提供 swap 函数
        println(t1, t1._1, t1._2, t1._3, t1.toString(), doubleTuple.swap)
        t2.productIterator.foreach(i=>{println("str:" + i)})
    }

    @Test
    def iteratorTest(): Unit ={
        val it = Iterator(20,40,2,50,69, 90)
        // println(it.max)
        // println(it.min)  // UnsupportedOperationException: empty.min
        println(it.size)
        while (it.hasNext){
            println(it.next(), it.length)
        }
        // TODO 将迭代器指向的所有元素拷贝至缓冲区 Buffer。
    }

    @Test
    def seqTest(): Unit ={
        println("seq Test")
    }

}
