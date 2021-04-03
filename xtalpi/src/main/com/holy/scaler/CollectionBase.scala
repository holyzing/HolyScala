package com.holy.scaler

import org.junit.Test
import scala.collection.mutable.ArrayBuffer

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
 * Seq Set Map Iterable
 *
 * muteable immutable
 */

class CollectionBase {
    @Test
    def arrayMore(): Unit ={
        val arr = new Array[String](3)
        val arr2 = Array("a")
        arr(1) = "a"
        println(arr2.length, arr2.+("a"), arr2 + "b", arr2.mkString(", "))
        for (item <- arr2){println(item)}
        arr2.foreach((str: String)=>{println(str)})
        arr2.foreach((str)=>{println(str)})
        arr2.foreach({println(_)})
        arr2.foreach(println(_))
        arr2.foreach(println)

        // JAVACODE: final String strs[] = new String[5]; strs 引用的地址不可改变
        arr2.update(1, "b")
        val arr3: Array[String] = arr2 :+ ("c")
        arr3.foreach(println)
        ("a" :+ arr2).foreach(println)

        val ar: ArrayBuffer[Int] = ArrayBuffer(1, 2, 3)
        ar += 4
        ar.insert(1, 3)
        val i: Int = ar.remove(2)
        ar.remove(1, 2)
        arr2.toBuffer
        ar.toArray
    }
}
