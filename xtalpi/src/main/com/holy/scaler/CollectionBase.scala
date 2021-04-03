package com.holy.scaler

import org.junit.Test

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * java 集合
 * String
 * Array
 * List: 有序可重复
 *       LinkedList ArrayList
 * Set：无序不可重复，重复元素不会替换原来的元素而是不做操作
 *      引申：数据库的存储也是无序的
 *      HashSet TreeSet
 * Map：键不可重复，重复键映射的值会替换原来的值。并在存在重复键的时候返回旧值，否则返回 null
 *      HashMap TreeMap HashTable
 *
 * muteable immutable
 */

class CollectionBase {
    @Test
    def seqTest(): Unit ={
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
        println(Nil, List(), -1 :: 0 ::Nil)

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
    }

    @Test
    def queueTest(): Unit ={
        val q: mutable.Queue[Int] = mutable.Queue(1, 2, 3, 4)
        q.enqueue(5)
        print(q.dequeue())
    }

    @Test
    def setTest(): Unit ={
        // 无序不可重复, 不可变集合
        val s = Set(0, 1, 2, 3, 6, 1)
        println(s + 4, s - 3, s - 5)
        // 集合的操作 并集 交集 差集 对称差集

        // 可变集合
        val ms: mutable.Set[Int] = mutable.Set(0, 1, 2, 3, 4, 5, 6, 1)
        println(ms.getClass)
    }

    @Test
    def mapTest(): Unit ={
        // 不可变 Map
        val m: Map[String, Int] = Map("a" -> 1, "b" -> 2)
        println((m + ("c" -> 3)).mkString(","))

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
        // 将无关的数据当成一组数据
        var t: (Int, String, Char) = Tuple3(1, "a", 'f')
        t = (1, "a", 'f')
        println(t._1, t._2, t._3)

        // 两个元组称为 对偶 （与键值对映射）
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
        println(f2(1, "a", 'c'))

        // NOTE scala 中元祖的最大长度是22 也就是 Tuple22,
        //      一样的函数的最大参数个数也是22，即 Function22类型，他们是不同长度参数函数的实际类型
        //      还有一些类型 Product,
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
        println(s.flatMap(x =>{x.toCharArray}))

        // 过滤
        println(c.filter(_ % 2 == 1))

        // 拉链 (python 中也有)
        println(List(1, 2, 3).zip(Array(1, 2, 3)))
        println(List(1, 2, 3).zip(Array(1, 2, 3)).unzip)

        // 联合
        println(List(1, 2, 3).union(Array(1, 2, 3)))

        // 相交
        println(List(1, 2, 3).intersect(Array(1, 2, 4)))

        // 相减  // 对称相减
        println(List(1, 2, 3).diff(Array(1, 2, 4)))

        // 降维
        // 折叠
    }

}
