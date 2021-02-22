package com.holy.scaler

import java.util.Date
import scala.annotation.tailrec


/**
 * 当一个方法无需输入参数时，我们称这个方法为“0参方法“, 定义一个0参方法时，你可以加一对空括号，也可以省略掉括号，直接写方法体。
 * 如果你在定义0参方法时加了括号，则在调用时可以加括号或者省略括号；
 * 但当你在定义0参方法时没加括号，则在调用时不能加括号，只能使用方法名；
 *
 * 当函数参数只有一个参数的时候 则可以将实例引用函数时的 . 和 () 替换为 两个空格, 而复合函数则可以直接去掉空格
 *
 * 前缀操作符  op obj      被解释称 obj.op
 * 中缀操作符  obj op obj2 被解释称 obj1.op(obj2)
 * 后缀操作符  obj op      被解释称 obj.op
 */

object FunctionMore{
    // scala 的标识符中不能包含中文字符
    // scala没有静态的修饰符，但object下的成员都是 "静态的" ,若同一包下有同名的class,则视其为 object 的 "伴生类"。

    private var age = 0

    def getAge: Int = {this.age+=1; age}
    def setAge(age: Int): Int ={this.age = age; this.age}

    def m1(x: Int): Int = x + 3
    def m2(x: Int): Int = {x + 3}

    // => 转换符号
    // =  后边是真正的参数列表定义和函数体， 其前边的则是显式的参数类型声明和返回类型声明
    //    方法必须有参数列表，而函数可以没有参数列表

    val f1: () => Int = () => 10
    val f2: ((Int, String) => Int) => Int = fun => fun(5, "a")

    def methodWithFunctionParam(fun:(Int, String) => Int): Int = {fun(10, "a")}

    {
        // 方法转为函数的方式
        // 1- 方法名 后 跟一个 < _>， 显式的告诉编译器将方法转为函数
        // 2- 当一个函数或者方法的参数是一个函数时, 传入一个方法，编译器会自动将方法本身转为函数,
        // 3- 显式定义一个函数，将方法赋予该函数变量
        val functionConvertFromMethod = methodWithFunctionParam _
        val functionFromMethod:Int = getAge
        println(functionConvertFromMethod.getClass, functionFromMethod.getClass)
        // println(methodWithFunctionParam, getAge.getClass) 方法是没有 getClass 函数的
    }
}


class FunctionMore{
    /**
     * 函数的调用
     *    传值调用（call-by-value）：先计算参数表达式的值，再应用到函数内部,
     *                            在进入函数内部前，传值调用方式就已经将参数表达式的值计算完毕。
     *    传名调用（call-by-name） ：将未计算的参数表达式直接应用到函数内部，在函数内部进行参数表达式的值计算。
     *                            每次使用传名调用时，解释器都会计算一次表达式的值。
     *
     * 函数传参：按照参数列表顺序传参，按照参数名以赋值表达式传参
     *
     * 可变参数，不定长参数： 和 java 的变长参数一致
     *
     * 递归函数：scala 中会使用大量的递归函数，来体现 函数式编程的奥妙
     *
     * 默认参数值：scala 的参数传递和 python有很大的相识之处，可以说几乎完全相同。
     *           也是分为 位置参数（在前） 和关键字参数（在后）。
     *           如果默认参数是一个容器，则任然会出现 ”缓存的问题“。
     *
     * 高阶函数：可以使用其他函数作为参数，或者使用函数作为输出结果的函数
     *
     * 内嵌函数：就是函数中的函数
     *
     * 匿名函数： scala.FunctionX apply
     *
     * 偏应用函数： 偏应用函数是一种表达式，你不需要提供函数需要的所有参数，只需要提供部分，或不提供所需参数
     *
     * 函数柯里化： 将原来接受两个参数的函数变成新的接受一个参数的函数的过程。新的函数返回一个以原有第二个参数为参数的函数
     *
     * 闭包： 同 python 一样， 一个函数使用其所在 scope 中的变量（引用到函数外面定义的变量）会行成闭包，
     *       定义这个函数的过程是将这个自由变量捕获而构成一个封闭的函数。
     *       与python 不同的是python 需要使用 nonlocal 在函数内部的第一行代码 声明引用的外部变量，才会改变外部变量的值
     */


    var instance: FunctionMore = _

    def apply(f: Int => String, x: Int): String = {
        if (instance == null) instance = new FunctionMore()
        f(x)

        @tailrec  // 递归函数好的注释
        def fact(i: Int, accumulator: Int): Int = {
            if (i <= 1) {
                accumulator
            } else {
                fact(i - 1, i * accumulator)
            }
        }

        "[" + fact(x, 1) + "]"
    }

    def log(date: Date, message: String): Unit ={
        println(date + "----" + message)
    }
    // 偏应用函数, 注意 : 两边都有空格
    val logWithBoundDate: String => Unit = log(new Date(), _ : String)

    // 函数柯里化，看起来和函数 偏应用化有一样的功效
    def curryingLog(date: Date)(message: String): Unit = {
        println(date + "----" + message)
    }
    def log(date: Date): String => String = (message: String)=>{date + "----" + message}

    // scala 闭包中可以修改 外部变量的值，不需要去做什么声明，因为要想在函数内部在声明一个同名函数变量需要使用 关键字 var
    // 但是当引用了外部变量之后在定义一个同名变量，编译器则会报错  Wrong forward reference
    var outer_var:Int = 5
    val encloseFuncChangeOuterVar: Int => Int = (x:Int) => {
        outer_var = x * 5
        outer_var
    }


    // --------------------------------------------------------------------
    def anonymous: Int => Int = new Function1[Int, Int] {
        //TODO scala.FunctionX 是什么 鬼？ apply 这个函数名 为什么又那么特殊 ??
        def apply(x:Int):Int = x+1
    }

    def enlongArgs(enlongArgs:String *): Unit = { // 可变长参数 ×args， **args
        for (arg <- enlongArgs){
            println(arg)
        }
    }

    def time(): Long = {
        println("time", "语法太自由")
        val nt: Long = System.nanoTime()
        nt
    }
    def delayed(func:() => Long) {  // 没有指定返回类型， 没有 = 到函数体"
        println("delayed", "函数参数:", func, "函数执行:", func())
        // Unused expression without side effects, 因为返回类型是一个空值
    }

    def delayed2(func:() => String, age:Int): ()=>String ={ // 返回一个函数, 参数是 常量 也就是说 是用 val 修饰的。
        println(age)
        func
    }

    def layout[A](x: A): String = {"["+ x.toString + "]"}
}
