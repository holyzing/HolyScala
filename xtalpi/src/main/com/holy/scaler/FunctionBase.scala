package com.holy.scaler

import java.util.Date

import org.junit.Test

import scala.annotation.tailrec

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

 * 当一个方法无需输入参数时，我们称这个方法为“0参方法“, 定义一个0参方法时，你可以加一对空括号，也可以省略掉括号，直接写方法体。
 * 如果你在定义0参方法时加了括号，则在调用时可以加括号或者省略括号；
 * 但当你在定义0参方法时没加括号，则在调用时不能加括号，只能使用方法名；
 *
 * 当函数参数只有一个参数的时候 则可以将实例引用函数时的 . 和 () 替换为 两个空格, 而复合函数则可以直接去掉空格
 *
 * 前缀操作符  op obj      被解释称 obj.op
 * 中缀操作符  obj op obj2 被解释称 obj1.op(obj2)
 * 后缀操作符  obj op      被解释称 obj.op
 *
 * 方法是函数在面向对象里的称呼， 方法转为函数的方式
 * 1- 方法名 后 跟一个 < _>， 显式的告诉编译器将方法转为函数
 * 2- 当一个函数或者方法的参数是一个函数时, 传入一个方法，编译器会自动将方法本身转为函数,
 * 3- 显式定义一个函数，将方法赋予该函数变量
 *
 * 函数的调用
 *   传值调用（call-by-value）：先计算参数表达式的值，再应用到函数内部,
 *                           在进入函数内部前，传值调用方式就已经将参数表达式的值计算完毕。
 * 传名调用（call-by-name） ：将未计算的参数表达式直接应用到函数内部，在函数内部进行参数表达式的值计算。
 *                          每次使用传名调用时，解释器都会计算一次表达式的值。
 *
 * 函数传参：按照参数列表顺序传参，按照参数名以赋值表达式传参
 *
 * 可变参数，不定长参数： 和 java 的变长参数一致
 *
 * 递归函数：scala 中会使用大量的递归函数，来体现 函数式编程的奥妙
 *
 * 默认参数值：scala 的参数传递和 python有很大的相识之处，可以说几乎完全相同。
 *          也是分为 位置参数（在前） 和关键字参数（在后）。
 *          如果默认参数是一个容器，则任然会出现 ”缓存的问题“。
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
 *      定义这个函数的过程是将这个自由变量捕获而构成一个封闭的函数。
 *      与python 不同的是python 需要使用 nonlocal 在函数内部的第一行代码 声明引用的外部变量，才会改变外部变量的值
 *
 * 将返回值类型为 Unit 的函数称之为过程 procedure，过程在声明时可以省略 =
 * def f = "a" 可以说明 “=” 在 scala 中存在的意义
 */

object FunctionBase{
    def main(args: Array[String]): Unit = {
        // NOTE: scala 的语法结构允许在任何 code block 中声明其它 code block, 也就是在语法中声明语法。
        // NOTE: java 中在 一个方法中可以声明一个局部内部类的语法规则与 scala 有些相似 。
        // NOTE: 所谓 函数是不直接定义在类型中的方法，scala 中方法可以重载而函数不可以。
        // NOTE: scala 中没有 throws 关键字，所以在函数中如果有其它的异常抛出,需要使用 try catch 捕获。

        def test1(): Unit = println("声明函数时加 （）")  // 0参函数
        def test2: Unit = println("声明函数没加 （）")    // 无参函数

        // scala 可以通过类型推断来简化函数的声明，比如返回类型的推断
        // NOTE  但是当函数签名的返回类型显示声明为 Unit 时，return 结束函数执行，但是返回值无效，始终为 Unit
        def test3(): Unit ={return "lu"}  // Returning Unit from a method with Unit result type

        def test4(): String ={"lu"}       // 省略 return
                                          // NOTE 默认将函数体最后一行表达式的值返回，如果返回类型不是显示的声明为 Unit

        def test5(): String = "lu"        // 函数体只有一行代码可省略 {}
        def test6(): Unit = System.nanoTime()

        // Unused expression without side effects, 因为返回类型是一个空值
        // 明确告诉函数没有返回值, 可以省略 =，但此时无法省略 {}
        def test7 {print("dasdasd")}

        test1() // 可省略 ()
        test2   // 函数声明时不接收参数，所以调用时不能加（），这和变量声明是一样的，访问和变量应该一致，称为 统一访问。
        println(test3(), test4(), test5(), test6(), test7)

        def test8(strs: String*): Unit ={
            for (arg <- strs){println(arg)}
            print(strs.getClass, strs)
            strs.foreach(println)
        }
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

class FunctionBase {
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

        // NOTE 函数柯里化， 将多个参数的函数转化为一个参数的多个函数
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

        def func(f:(String)=> Unit): Unit ={f("lu")}
        func((s:String)=>{println(s)})
        func((s)=>{println(s)})
        func(s=>println(s))
        func(println(_))
        func(println)

        def f(sum:(Int, Int)=>Int, a: Int=10, b:Int=20): Int = {sum(a, b)}
        println(f((a:Int, b:Int)=> {a + b}))
        println(f((a, b)=> {a + b}))
        println(f((a, b)=> a + b))
        println(f(_+_))  // NOTE 函数体只有一个表达式，且参数列表中的参数在一个表达式内只调用一次
    }

    @Test
    def anonymousFunction(): Unit ={
        def f = 10
        val v = f
        val vf = f _
        println(f, v, vf)

        def f2() = 20
        println(f2, f2(), f2 _, (f2 _)())

        println("--------------------------------------------------------------------------------------")
        val a = "args"
        (a)->{print(a)}                   // THINK 匿名函数的声明就是引用 ?????
        (a: Int) => {a + 3}               // THINK 到底哪个是匿名函数 ？？？？ 为什么到这里就不往下执行了 ？？
        println("--------------------------------------------------------------------------------------")
    }

    /**
     * JVM 堆上对象并不一定能被及时回收，GC 的执行是不确定的，HIbernate 的弃用 就是随着数据量的增加，
     * 不仅是数据库中的数据会转换成一个个的对象，而且会产生很多中间数据，从而占用大量内存导致性能下降。
     * 有时候一个对象在函数执行完成之后就应该被及时回收，从而在近代 JVM 中支持了栈上分配的机制，
     * 当一个变量出栈之后是直接释放内存的，所以不需要回收，但是也伴随着逃逸分析的问题，
     * 当函数内部变量需要被返回去 或者 函数入参了一个外部的引用变量的时候是不能直接被释放的，
     * 因为 它逃逸出 方法了，是不能放在 栈上的。
     *
     * 闭包是 外部函数变量作用域的延伸，延伸到内部函数 闭包
     *
     * TODO 传值调用 和 传名调用
     */

    // 递归函数 无限递归会导致 栈溢出，函数执行 变量的压栈出栈
    // 1- 函数 return 的条件
    // 2- 参数之间的规律
    // 3- NOTE 递归函数必须指定返回值的类型，因为类型推断会陷入死循环，且返回类型不能指定为 Any
    @tailrec
    final def fact(i: Int, accumulator: Int): Int = {if (i <= 1) {accumulator} else {fact(i - 1, i * accumulator)}}
    def !!(i:Int): Int={if (i == 1){i} else{i * !!(i-1)}}

    // 偏应用函数, 注意 : 两边都有空格
    def log(date: Date): String => String = (message: String)=>{date + "----" + message}
    def log(date: Date, message: String): Unit ={println(date + "----" + message)}
    val logWithBoundDate: String => Unit = log(new Date(), _ : String)

    // scala 闭包中可以修改 外部变量的值，不需要去做什么声明，因为要想在函数内部在声明一个同名函数变量需要使用 关键字 var
    // 但是当引用了外部变量之后在定义一个同名变量，编译器则会报错  Wrong forward reference
    var outer_var:Int = 5
    val encloseFuncChangeOuterVar: Int => Int = (x:Int) => {
        outer_var = x * 5  // NOTE 赋值表达式的值 是 Unit
        outer_var
    }

    @Test
    def returnFunctionTest(): Unit ={
        def f = "a"
        def f2() = "a"
        val f3 = () => {"a"}
        def delayed(func:() => String) ={func}
        def delayed2(func:() => String) = {func()}
        def delayed4(func:() => String) = {f3}
        def delayed5(func:() => String) = {f2 _}

        println(delayed(()=>"a"), delayed(f _), delayed(f2), delayed(f2 _), delayed(f3))
        println(delayed2(()=>"a"), delayed2(f _), delayed2(f2), delayed2(f2 _), delayed2(f3))

        // def delayed3(func:() => String) ={func _}
        // _ must follow method; cannot follow () => String  编译器没报错
        // println(delayed3(()=>"a"), delayed3(f _), delayed3(f2), delayed3(f2 _), delayed3(f3))

        // NOTE 当函数直接返回一个参数，该参数是一个函数时，可直接返回该函数名，而不用 _ 进行转换
        // NOTE 只有当当返回一个方法 (对象内的函数，函数也是一个对象)时，才需要用 _ 进行转换
        // THINK 纠结这些有什么用 ？？？？？？？？
    }

    def layout[A](x: A): String = {"["+ x.toString + "]"}

    //TODO scala.FunctionX 是什么 鬼？ apply 这个函数名 为什么又那么特殊 ??
    def anonymous: Int => Int = new Function1[Int, Int] {def apply(x:Int):Int = x+1}

    @Test
    def lazyValTest(): Unit = {
        // 函数的惰性执行 lazy 只能修饰 val， 而不能修饰 var， 因为 var 是可变的

        def sum(a: Int, b: Int): Int ={
            println("执行 sum 函数")
            a + b
        }
        lazy val s = sum(1, 2)
        println("还未引用 lazy val")
        println("即将引用 lazy val", s)
    }



}
