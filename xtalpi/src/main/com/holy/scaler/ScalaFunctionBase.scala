package com.holy.scaler

import org.junit.Test

/**
 * 编程范式：
 *    1-面向对象：  依赖对象关系解决问题，封装，继承，多态，实现，重写
 *    2-函数式编程：直接解决问题，将解决方案封装成一个函数。关注 入参，出餐
 *
 * 函数式编程：
 *    1- 声明函数
 *        1.1- java 中不能独立声明一个函数，必须依附于某个类，必须通过某个或者对象进行引用，
 *              所以在java 中不强调函数的概念，而是方法。
 *        1.2- scala 中函数是可以随时声明的，并且可以作为一个变量存在。
 *    2- 调用函数
 */

object ScalaFunctionBase{
    def main(args: Array[String]): Unit = {
        // NOTE: scala 的语法结构允许在任何 code block 中声明其它 code block, 也就是在语法中声明语法。
        // NOTE: java 中在 一个方法中可以声明一个局部内部类的语法规则与 scala 有些相似 。
        // NOTE: 所谓 函数是不直接定义在类型中的方法，scala 中方法可以重载而函数不可以。
        // NOTE: scala 中没有 throws 关键字，所以在函数中如果有其它的异常抛出也不需要跟着抛出或者捕获，由编译器处理

        def test1(): Unit = println("声明函数时加 （）")  // 0参函数
        def test2: Unit = println("声明函数没加 （）")    // 无参函数

        // scala 可以通过类型推断来简化函数的声明，比如返回类型的推断
        // NOTE  但是当函数签名的返回类型显示声明为 Unit 时，return 不起作用，始终返回的是 Unit
        def test3(): Unit ={return "lu"}  // Returning Unit from a method with Unit result type

        def test4(): String ={"lu"}       // 省略 return
                                          // NOTE 默认将函数体最后一行表达式的值返回，如果返回类型不是显示的声明为 Unit

        def test5(): String = "lu"        // 函数体只有一行代码可省略 {}
        def test6(): Unit = "lu"
        def test7 {print("dasdasd")}      // 明确告诉函数没有返回值, 可以省略 =，但此时无法省略 {}

        test1() // 可省略 ()
        test2   // 函数声明时不接收参数，所以调用时不能加（），这和变量声明是一样的，访问和变量应该一致，称为 统一访问。
        println(test3(), test4(), test5(), test6(), test7)

        def test8(str: String*): Unit ={print(str.getClass, str)}
        test8("a", "b", "c")        // 变长参数
        test8()

        // java 中有方法重载，可通过方法重载实现 “默认参数的功能”， 但是默认参数是被定义在方法体中的。
        // scala和python的参数匹配规则基本一致，也拥有默认参数，但是在scala中，默认参数可以在位置参数之前。
        // 参数默认都是按参数列表顺序匹配的，但是当全部使用关键字参数时，在 python和scala中顺序是可以乱序的。
        def test9(name: String="lu", age: Int): Unit ={println(s"name:$name age:$age")}
        test9("liu", 18)  // 默认参数，关键字参数
        test9(age=18, name="dasd")
    }
}

class ScalaFunctionBase {
    /**
     * scala 是一个完全面向对象的编程语言，同时也完全是函数式编程语言，
     * 这意味着 多范式编程语言 scala 中所有的元素都是对象同时也都是函数。
     *
     * 函数可以作为 ：变量 函数参数 函数的返回值
     */
    @Test
    def valFunction(): Unit ={
        // NOTE 在python中 只有 () 可以触发 __callable__ 对象的执行，在 python中函数也是对象
        // NOTE 但是在 scala 中，无参函数是可以省略 () 触发执行的，因此在 scala 中使用 return
        // NOTE 是无法返回一个函数的，返回的是 函数的执行结果。为此 scala 中 增加了特殊符号 _
        // NOTE 来表示 函数的不执行，从而返回一个函数

        def f0(): Unit ={println("called function wirh zero args")}
        def f00(arg: String): Unit = {println(s"called function wirh args: $arg")}
        def f1() = {f0(); f00 _}
        f1()("a")     // NOTE 显然此时 调用无参函数时是无法省略 () 的，
        val f3 = f1   // 执行函数
        val f4 = f1 _ // 函数赋值
        f3("b")
        f4()("c")

        def fout1(a: Int) ={
            def fin1(b: Int): Int ={
                val multi = a * b
                println(s"a * b= $multi")
                multi
            }
            fin1 _
        }
        val fin1 = fout1(1)
        fin1(5)
        fout1(1)(3)

        // NOTE 函数柯里化
        def curryFunction(i: Int)(j: Int) {println(s"i * j= ${i * j}")}
        // curryFunction(4)   // 无法调用
        curryFunction(4)(5)   // 函数柯里化必须一步调用到最内层函数
    }

    @Test
    def funcEnclosing(): Unit ={
        // NOTE 复杂的概念 函数闭包 详解链接 https://www.zhihu.com/question/34210214
        def fout(a: Int) = {
            def fin(b: Int): Int ={a*b}
            fin _
        }
    }

    @Test
    def argFunction(): Unit ={
        // NOTE 参数函数的 签名

        def func1(f:(Int)=> Int): Int ={10 * f(1)}

        def argFunction(a : Int): Int ={
            a + 20
        }
        println(func1((a: Int) => a + 5))   // 匿名函数
        println(func1(argFunction))
    }

    @Test
    def anonymousFunction(): Unit ={
        println("--------------------------------------------------------------------------------------")
        val a = "args"
        (a)->{print(a)}                   // THINK 匿名函数的声明就是引用 ?????
        (a: Int) => {a + 3}               // THINK 到底哪个是匿名函数 ？？？？ 为什么到这里就不往下执行了 ？？
        println("--------------------------------------------------------------------------------------")
    }
}
