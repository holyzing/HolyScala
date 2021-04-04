package com.holy.scaler

import org.junit.Test

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer

/**
 * 在 Scala 中，字符串的类型实际上是 Java String，它本身没有 String 类。
 * 在 Scala 中，String 是一个不可变的对象，所以该对象不可被修改。
 * 这就意味着你如果修改字符串就会产生一个新的字符串对象。
 * 然而String 底层是一个数组，通过反射是可以修改的
 *
 * scala 变量的类型在编译的时候就已经确定了，即使定义时不指定变量类型，也会在编译期推倒出变量类型，
 * python 的变量则完完全全是由 变量引用的值决定的。这也造成了两种语言 垃圾回收机制的完全不同。
 *
 * 在ubuntu 中使用 idea ，fctix类型的输入法不能跟随光标。
 *
 * JAVA 中 length 是 数组的属性 length() 是字符串的方法, size() 是集合的方法
 * Replace .size with .length on arrays and strings
 * scala 中 immutable 使用 length，而 mutable 使用 size
 */

class Collection {

    @Test
    def arrayBase(): Unit ={
        printf("浮点数：%f 整形：%d 字符串：%s", 1.0, 1, "1")
        // println(String.format("浮点数：%f 整形：%d 字符串：%s", 2.0f, 2, "2"))

        // Scala 语言中提供的数组是用来存储固定大小的同类型元素.
        val multiDimArr = Array.ofDim[Int](3,2)
        val twoDimArr = Array.ofDim[Int](1,6)
        println(twoDimArr.length)
        for (i <- multiDimArr){
            for (j <- i){
                // oneDimArr.appended(j) // 运行时错误 ？？？？
                print(j)
            }
        }

        val rangeArr1 = Array.range(1,10)
        val rangeArr2 = Array.range(2, 5, 2)
        val concatArr = Array.concat(rangeArr1, rangeArr2)
        println(concatArr.indices)

        val typeNone = None      // object None extend object Option
        val typeSome = Some      // object Some extend object Option
        val typeOption = Option  // object Option
        val keyNull = null       // keyword
        println(typeNone, typeSome, typeOption, keyNull)

        val capitals = Map("France"->"Paris", "Japan"->"Tokyo", "China"->"Beijing")
        println(capitals get "France", capitals.get("Japan"))
        println(capitals("France"), capitals.getOrElse("Americn", "wanisha"))

        def showCapital(x: Option[String]) = x match {
            case Some(s) => s  // 乱七八糟的返回模式 ？ scala 糟糕的语法
            case None => "?"
        }

        for (c <- capitals.get("Americn")){
            println(c.length)
        }

        println(showCapital(capitals.get("not exist")))

        val optionSome1: Some[Int] = Some(5)
        val optionNone1: Option[Nothing] = None     // does not take parameters
        val optionSome2: Option[Int] = Option(5)
        val optionNone2: Option[Unit] = Option()

        println(optionNone1, optionNone2.get, optionSome1.get, optionSome2.get)
    }

    @Test
    def arrayMore(): Unit = {
        val arr = new Array[String](3)
        arr(1) = "a"

        val arr2 = Array("a")
        // println(arr2.length, arr2.mkString(", "), arr2.+("a"), arr2 + "b")
        for (item <- arr2) {println(item)}
        arr2.foreach((str: String) => {println(str)})
        arr2.foreach(str=> {println(str)})
        arr2.foreach({println(_)})
        arr2.foreach(println(_))
        arr2.foreach(println)

        // JAVACODE: final String strs[] = new String[5]; strs 引用的地址不可改变
        arr2.update(0, "b")
        val arr3: Array[String] = arr2 :+ "c"
        arr3.foreach(println)
        ("a" :+ arr2).foreach(println)
        val ar: ArrayBuffer[String] = ArrayBuffer("1", "2", "3")

        ar += "4"
        ar.insert(1, "4")
        ar.remove(2)
        ar.remove(1, 2)
        arr2.toBuffer
        ar.toArray


        // 与 java 之间互转
        import scala.collection.JavaConverters.{bufferAsJavaList, asScalaBuffer}

        val javaList = bufferAsJavaList(ar)
        println(javaList)
        println(asScalaBuffer(javaList))
    }

    @Test
    def seqTest(): Unit ={
        // JAVA: LinkedList ArrayList
        // 不可变 List （大小不可变）
        val l: List[Int] = List(1,2,3,4)
        // 索引，访问
        println(l(1), l.head, l.tail, l.last, l.mkString(","))
        // 单个元素扩展
        println((l :+ 5).mkString(","), "\n", (5 +: l).mkString(","), "\n", l.+:(0).mkString(","))
        println(l.::(5).mkString(","), "\n", (-1 :: 0 :: l).mkString(","))
        // 多个元素扩展
        println(l ++ List(4, 5, 6),  "\n", l ++ Array(4, 5, 6))
        println(0 :: l ::: List(4, 5, 6))
        // 空集合扩充
        println(Nil, List(), -1 :: 0 ::Nil, List(1, 2) ::: Nil)

        // 更新 返回新列表
        println(l.updated(2, 0) == l)
        // TODO 索引位无法赋值 ??? 大小不可变 索引位的值也不可变？？？
        //      error: value update is not a member of List[Int]
        // l(0) = -1
        // 移除数据 从前往后 drop
        println(l.drop(2).mkString(","))

        println("-------------------------------------------------------------------")
        // 可变集合
        val lb: ListBuffer[Int] = ListBuffer(2, 3, 4, 5)
        println(lb.head, lb.init, lb.tail, lb.last)
        println(lb.head, lb.init(2), lb.tail(2), lb.last)

        // Scala 不可变列表 （immutable recursive data）类似于数组，它们所有元素的类型都相同，长度固定
        // 但是它们也有所不同：列表是不可变的，值一旦被定义了就不能改变，其次列表 具有递归的结构（也就是链接表结构）而数组不是。。

        // 二维列表
        val dim = (1 :: (0 :: (0 :: Nil))) ::  // ::: 等价 list.concat
            (0 :: (1 :: (0 :: Nil))) ::
            (0 :: (0 :: (1 :: Nil))) :: Nil

        val squares = List.tabulate(6)(f=n => n * n) // for (i <- 0 until(6)) { fun(i) }
        val mul = List.tabulate(4, 5)(_ * _)
        println(dim, squares, mul)

        val list6 = List.fill(3)("strEle")  // 重读 0
        val list7 = List.fill(3)(2)
        println(list6, "\n", list7)


    }

    @Test
    def queueTest(): Unit ={
        val q: mutable.Queue[Int] = mutable.Queue(1, 2, 3, 4)
        q.enqueue(5)
        print(q.dequeue())

        /**
         * Kafka 双端队列
         * kafka 能保证一个分区内的有序性，但不能保证多个分区之间数据的有序性
         * 生产者发送数据可能存在失败问题，失败后为了保证数据顺序，只能在返回原队列位置，
         * 所谓双端就是头尾均可取数据，插入数据
         *
         * 阻塞式队列
         * 从队列中取数据的时候队列里没数据，形成阻塞，直到队列被插入数据
         * 往队列中插数据的时候队列里是满的，形成阻塞，直到队列被取走数据
         *
         */
    }

    @Test
    def setTest(): Unit ={
        /**
         * 虽然可变Set和不可变Set都有添加或删除元素的操作，但是有一个非常大的差别。对不可变Set进行操作，会产生一个新的set，
         * 原来的set并没有改变，这与List一样。 而对可变Set进行操作，改变的是该Set本身，与ListBuffer类似
         *
         *  无序不可重复，重复元素不会替换原来的元素而是不做操作
         * 引申：数据库的存储也是无序的
         * JAVA: HashSet TreeSet
         */
        // 无序不可重复, 不可变集合
        val s = Set(0, 1, 2, 3, 6, 1)
        println(s + 4, s - 3, s - 5)

        // 可变集合
        val ms: mutable.Set[Int] = mutable.Set(0, 1, 2, 3, 4, 5, 6, 1)
        println(ms.getClass)

        // 并集：++
        // 交集：&    intersect
        // 差集：&~              // 还有一个对称差集 ？？？
        // + -
        // 子集： subSetOf
    }

    @Test
    def mapTest(): Unit ={
        // Map：也叫哈希表（Hash tables）,键不可重复，重复键映射的值会替换原来的值。
        //      并在存在重复键的时候返回旧值，否则返回 null
        // JAVA: HashMap TreeMap HashTable

        // 不可变 Map
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

        val m: Map[String, Int] = Map("a" -> 1, "b" -> 2)
        println((m + ("c" -> 3)).mkString(","), m.mapValues(_ + 2), m.values, m.keys)

        // 可变map
        val mm: mutable.Map[String, Int] = mutable.Map("a" -> 1, "b" -> 2)
        mm.remove("a")
        mm - "b"  // 返回一个新的map
        println(mm.mkString(","))
        mm -= "b"
        mm + ("a" -> 5)     // 返回一个新map
        println(mm.mkString(","))
        mm("a") = 6
        println(mm.mkString(","))
        mm.update("a", 7)
        mm.updated("a", 8)  // 返回一个新map
        println(mm.mkString(","))

        // map 为了防止空指针异常 提供了特殊的类型 Option，存在两个子类 Some 和 None
        val option: Option[Int] = mm.get("a")
        println(option, option.get, option.getOrElse(8))
        // mm.get("f").get java.util.NoSuchElementException: None.get
    }

    @Test
    def tupleTest(): Unit ={
        // 与不可变列表一样，元组也是不可变的，但与列表不同的是元组可以包含不同类型的元素。
        // 将无关的数据当成一组数据
        var t: (Int, String, Char) = Tuple3(1, "a", 'f')
        t = (1, "a", 'f')
        println(t._1, t._2, t._3)

        // 两个元组称为 对偶 （与键值对映射） 对于二元组 则会提供 swap 函数
        val tm = Map(("a", 1), ("b", 2), ("c", 3))
        tm.foreach(t2=>{println(t2)})
        tm.foreach((t2:(String, Int))=>{println(t2._1 + "=" + t2._2)})
        // tm.foreach((t2:Tuple2[String, Int])=>{println(t2)})

        // -----------------------------------------------------------------------

        // val f = new Function3[Int, String, Char, Boolean]() {
        //    override def apply(v1: Int, v2: String, v3: Char): Boolean = {true}
        // }

        val f2 = new ((Int, String, Char) => Boolean)() {
            override def apply(v1: Int, v2: String, v3: Char): Boolean = {true}
        }
        println(f2(1, "a", 'c'), (1, 2).swap)

        // NOTE scala 中元祖的最大长度是22 也就是 Tuple22,
        //      一样的函数的最大参数个数也是22，即 Function22类型，他们是不同长度参数函数的实际类型
        //      还有一些类型 Product,
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
    def collectionFunction(): Unit ={
        // 集合的函数式编程
        val c = ListBuffer[Int](3, 1, 2, 6, 7, 2, 6)
        // val r = new Random()
        c.append(5)

        println("max = " + c.max)
        println("min = " +  c.min)
        println("sum = " + c.sum)
        println("product = " + c.product)
        println("reverse :" + c.reverse)
        println("sorted :" + c.sorted)
        println("sortedWithOrdering :" + c.sorted(Ordering.Int))  // NOTE 没什么用 ？？

        // 迭代
        for(e <- c) {e / 2}
        for(e <- c.iterator){e % 2}

        // Top N
        println(c.take(2).mkString(","))

        // 升序排序
        println(c.sortBy(_ % 2).mkString(","))
        val s: List[String] = List("11", "a1", "01", "b2")
        println(s.sortWith((l, r)=>{l < r}))
        println(s.sortWith((l, r)=>{l.substring(1) > r(0).toString}))

        // 分组聚合 (聚合后是个Map 如果要排序则转为 list排序)
        c.groupBy(_ % 2).toList.foreach(t=>{println(t._1 + ": " + t._2.mkString("[", ",", "]"))})

        // 映射 wordcount
        println(c.map((_, 1)).groupBy(_._1).map(t=>{(t._1, t._2.size)}).mkString(","))

        // 扁平化
        // val l: List[Any] = List(1, List(2, 3), 4, List(5))
        // val list: List[Int] = l.flatMap(any => {
        //     if (any.isInstanceOf[List[Int]]) {
        //         any.asInstanceOf[List[Int]]
        //     } else {
        //         List(any.asInstanceOf[Int])
        //     }
        // })
        println(s.flatMap(x =>{x.toCharArray}))

        // 过滤
        println(c.filter(_ % 2 == 1))

        // 拉链 (python 中也有)

        val list123 = List(1, 2, 3)
        println(list123.zip(Array(1, 2, 3)))
        println(list123.zip(Array(1, 2, 3)).unzip)

        // 联合
        println(list123.union(Array(1, 2, 3)))

        // 相交
        println(list123.intersect(Array(1, 2, 4)))

        // 相减  // 对称相减
        println(list123.diff(Array(1, 2, 4)))

        // 化解 归约 降维
        println(list123.reduceLeft(_ - _))  // 左元素 减去 右元素
        println(list123.reduceRight(_ - _)) // 翻转列表 右元素 减去 左元素
        println(list123.reduce(_ - _))

        // 折叠 扫描（集合的元素往额外的一个元素上折叠）
        println(list123.fold(10)(_ - _))
        println(list123.foldLeft(10)(_ - _))
        println(list123.foldRight(10)(_ - _))
        println(list123.scanLeft(10)(_ - _))
        println(list123.scanRight(10)(_ - _))

        // python update 操作
        val map1 = mutable.Map(("a", 1), ("b", 2), ("c", 3))
        val map2 = mutable.Map("a" -> 3, "c" -> 2, "d" -> 1)
        val map3 = map1.foldLeft(map2)((map, t) => {
            val v = map.getOrElse(t._1, 0)
            map(t._1) = v + t._2
            println(map == map2)
            map
        })
        println(map3, map3 == map2)
    }

    def scalaIO(): Unit ={
        import scala.io.Source
        val source = Source.fromFile("")
        source.getLines().toList
        source.close()
    }

    /**
     * 线程安全： JVC ThreadPool Executor
     * 低版本会存在一些 以 Synchronized开头的集合
     * 低版本这些集合都不可用了
     *
     * java 的线程不是原生线程
     *
     * TODO java 的线程和 python 的线程有何区别 ？？？？？
     *
     * 多核并行集合 （单核并发）
     */
    @Test
    def parallelTest(): Unit ={
        println((0 to 10).map(_ => Thread.currentThread().getName))
        println((0 to 10).par.map(_ => Thread.currentThread().getName))
    }
}
