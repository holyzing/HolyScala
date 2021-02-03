package com.holy.scaler

import org.junit.Test

/**
 * 1 - java 的变量是可以先声明，只要在引用之前 “赋值”， 也就是所谓的初始化也是可以的，
 *     但是 scala 是必须在声明的时候就初始化。变量在栈中，如果不初始化会指向 随机的地址。
 * 2- java 中的常量是用 final 修饰的变量，但是 scala中用 val 声明的引用属于常量，在 scala 的编译器
 *    下是不能被修改的，但是当被编译成字节码后则是 java 中普通的变量，所以从语法角度来讲 val 声明的是
 *    scala 中的常量。
 */
object ValVar {
    def main(args: Array[String]): Unit = {
        /**
         * Byte	      8位有符号补码整数。数值区间为 -128 到 127
         * Short	  16位有符号补码整数。数值区间为 -32768 到 32767
         * Int	      32位有符号补码整数。数值区间为 -2147483648 到 2147483647
         * Long	      64位有符号补码整数。数值区间为 -9223372036854775808 到 9223372036854775807
         * Float	  32 位, IEEE 754 标准的单精度浮点数
         * Double	  64 位 IEEE 754 标准的双精度浮点数
         * Char	      16位无符号Unicode字符, 区间值为 U+0000 到 U+FFFF
         * String	  字符序列
         * Boolean	  true或false
         * Unit	      表示无值，和其他语言中void等同。用作不返回任何结果的方法的结果类型。Unit只有一个实例值，写成()。
         * Null	      null 或空引用
         * Nothing	  Nothing类型在Scala的类层级的最底端；它是任何其他类型的子类型。
         * Any	      Any是所有其他类的超类
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
         * Char      'a'， 'b'，'\n'
         * String    "a", "\n", "haha"
         * Null      null                 Scala.Null和scala.Nothing是用统一的方式处理Scala面向对象类型系统的某些"边界情况"的特殊类型。
         * Null 是每个引用类（继承自AnyRef的类）的子类。Null不兼容值类型
         * Nothing
         * Unit      ()
         * Any                            scala 中所有 “其他类” 的超类
         * AnyRef                         scala 中所有 “引用类” 的基类
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
    @Test
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
        print(d, e, f, g)
        val multiLineString =
            """
              |
              |
              |
              |
             """
        println(multiLineString, "\b", "\t", "\n", "\f", "\r", "\"", "\'", "\\")
        // scla 和 java 一样不能重复定义一个变量，这和 python 的 引用指向是有一定区别的
    }
}
