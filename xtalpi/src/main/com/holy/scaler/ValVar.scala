package com.holy.scaler

import com.holy.test
import org.junit.Test

/**
 * 1 - java 的变量是可以先声明，只要在引用之前 “赋值”， 也就是所谓的初始化也是可以的，
 *     但是 scala 是必须在声明的时候就初始化。变量在栈中，如果不初始化会指向 随机的地址。
 * 2- java 中的常量是用 final 修饰的变量，在 scala 的语法层面，用 val 声明的引用属于常量，
 *    在 scala 的编译器下是不能被修改的。
 *    但是处于不同作用域下的常量在经过scala编译之后，形成的字节码的限定修饰是不一样的，在一个
 *    伴生类的成员中声明的常量经过编译后是被加了 final修饰符的，在方法体等局部中声明的常量则和
 *    普通的局部变量没什么区别。
 *  3- java 中用 final修饰的变量，指的是该变量的引用地址不可变，在java中的参数传递是值传递，
 *     String的底层是一个字符数组,是可以改变其内容的。
 *  4- 函数式编程，声明变量后改变一个变量引用的可能性较小，所以在scala 中 val 比 var使用频率更高。
 *  5- scala 中是没有基本类型的，它是完全面向对象的。数字也是一个对象。
 */
object ValVar {
    def main(args: Array[String]): Unit = {
        /**
         * Byte	      8位有符号补码整数。数值区间为 -128 到 127
         * Short	  16位有符号补码整数。数值区间为 -32768 到 32767
         * Int	      32位有符号补码整数。数值区间为 -2147483648 到 2147483647
         * Long	      64位有符号补码整数。数值区间为 -9223372036854775808 到 9223372036854775807
         * Float	  32 位, IEEE 754 标准的单精度浮点数
         * Double	  64 位 IEEE 754 标准的双精度浮点数 字面量默认是 Double 类型
         * Char	      16位无符号Unicode字符, 区间值为 U+0000 到 U+FFFF
         * Boolean	  true或false
         * String	  字符序列
         * Unit	      表示无值，和其他语言中void等同。用作不返回任何结果的方法的结果类型。Unit只有一个实例值，写成()。
         * Null	      null 或空引用
         * Nothing	  Nothing类型在Scala的类层级的最底端；它是任何其他类型的子类型。
         * Any	      Any是所有其他类的超类
         * AnyVal     AnyVal类是Java中所有基本类型中scala中字面值类型的超类
         * AnyRef	  AnyRef类是Scala里所有引用类(reference class)的基类
         *
         * scala 中没有 java 的原生类型，和 python 一样万事万物皆对象。
         *
         * 字面量：
         * Int       1, 2, 3
         * Long      1L，2l, 0xFFFFFF     scala 中进制的字面量表示 只有 16 进制和 10进制
         * Float     1.2f，1.0e10
         * Double    1.2，1.2e-10
         * Boolean   true，false
         * Char      'a'， 'b'，'\n'       16位无符号的Unicode 编码
         * String    "a", "\n", "haha"
         *
         * Unit      唯一实例 ()
         * Null      唯一实例 null
         *           是每个引用类（继承自AnyRef的类）的子类。Null不兼容值类型
         *           Scala.Null和scala.Nothing是用统一的方式处理Scala面向对象类型系统的某些"边界情况"的特殊类型。
         *           Scala.Null和scala.Nothing是用统一的方式处理Scala面向对象类型系统的某些"边界情况"的特殊类型。
         *           是每个引用类（继承自AnyRef的类）的子类。Null不兼容值类型
         *
         * Nothing
         * Any       scala 中所有 “其他类” 的超类
         * AnyRef    scala 中所有 “引用类” 的基类
         *
         * "Symbol"  'x，Symbol(“x”)      符号字面量，不能以数字开头，被映射成 预定义类 scala.Symbol 的实例。‘<标识符>
         *
         * 变量是一种使用方便的占位符，用于引用计算机内存地址，变量创建后会占用一定的内存空间。
         * 基于变量的数据类型，操作系统会进行内存分配并且决定什么将被储存在保留内存中。
         * 因此，通过给变量分配不同的数据类型，你可以在这些变量中存储整数，小数或者字母。
         * 如果在没有指明数据类型的情况下声明变量或常量必须要给出其初始值，否则将会报错。
         *
         * 字面值常量
         * 声明式常量
         *
         * shift + alt + insert : 区块选择的开关
         */
    }
}


class ValVar {
    @test.Test
    def baseTest(): Unit = {
        val a: Double = 1.2e-10
        val b: Float = 1.2f
        val c: Double = .1
        println(a, b, c, c.getClass, 0x11)
        // Binary and Octal literals syntax has been disabled since Scala 2.11
        val d: Boolean = true
        val e = 'x
        // Symbol literals are deprecated in Scala 2.13. Use Symbol("x") instead.
        val f = Symbol("x")
        val g: Null = null
        val s: String = g       // 引用类型的隐式转换
        val dd: Double = 2f     // 值类型的隐式转换
        print(d, e, f, g)
        println(d, e, f, g, s, dd )
        val multiLineString =
            """
              |
              |
              |
              |
             """
        println(multiLineString, "\b", "\t", "\n", "\f", "\r", "\"", "\'", "\\")
        // scla 和 java 一样不能重复定义一个变量，这和 python 的 引用指向是有一定区别的

        println(10.toDouble, 10f.toDouble, 10d.getClass)
        // 低精度到高精度是可以实现隐式转换的
        // java 中的引用类型的装换是要满足多态特征的，在 scala中则各类型提供了可以识别到的强制类型转换函数。
    }

    def testNothing: Nothing = {
        throw new Exception("没有一个正常的返回值")
    }

    @test.Test
    def identifier(): Unit ={
        // scala 支持各种特殊符号作为标识符，编译为字节码后，由符号对应的单词标识，
        //       但是当特殊符号（运算符）与其它一般字符连接使用时则编译器会报错，因为特殊符号可能会被当做操作符编译
        val ++ = 1
        val -- = 2
        val ** = 3
        val \\ = 4
        val / = 5
        val +-*/ = 6
        // val _ = 7         // 在scala中下划线不能单独作为标识符，但是在java中是可以的
        // val 变量 = 8       // unicode 字符都可以作为标识符，和python中一样
        val `private` = 9    // 使用飘号可以将scala中的保留字作为标识符
        val Int = 10
        val int = 11         // 反编译后可能不准确，依旧是 int
        println(++, --, **, \\, /, +-*/, `private`, Int, int)

        for (i <- 1 to 2) {
            println(i)
        }

        for (i <- 1 until 2) {
            println(i, null.getClass, ().getClass)
        }

        // scala 特有的关键字： object trait with sealed implict match yield def val var type lazy override
        // scala 中没有的关键字： throws static public void implement friendly switch
        // 共有的：package import object class protected private final abstract extend null
        //        new this super try catch finally throw if else do while case for true false return
    }
}
