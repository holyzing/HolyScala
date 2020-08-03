package com.holy.scala

import org.apache.spark.scheduler.SparkListenerTaskGettingResult

/**
 * 当一个方法无需输入参数时，我们称这个方法为“0参方法“, 定义一个0参方法时，你可以加一对空括号，也可以省略掉括号，直接写方法体。
 *
 * 如果你在定义0参方法时加了括号，则在调用时可以加括号或者省略括号；
 * 但当你在定义0参方法时没加括号，则在调用时不能加括号，只能使用方法名；
 */

object Function { // scala 的标识符中不能包含中文字符
    /**
     * scala没有静态的修饰符，但object下的成员都是静态的 ,若同一包下有同名的class,会将其作为 object 的 "伴生类"。
     */
    def main(args: Array[String]): Unit = {
        // println(_)
        println("dasdasdas", 3+5)
        // THINK 哪一版本前 当函数只有一个参数的时候可以不加 (), 比如  (println “123”) (1.+2) ？？？
        methodWithFunctionParam(m1)
        println(getAge)  // getAge() 会报错
    }

    private var age = 0
    def setAge(age: Int) ={
        this.age = age
        this.age
    }
    def getAge: Int = {this.age+=1; age}

    def m1(x: Int): Int = x + 3

    def m2(x: Int) = {x+3}

    // => 转换符号
    var f1: String => String = (x: String) => x + "hello world"
    var f2 = (x: Int) => {x - 3}
    val f3: Int => Int = (y: Int) => y + 3
    val f4 = (y: Int) => y - 3

    val f5: (Int => Int) => Int = (fun:(Int) => Int) => fun(5)

    val f6 = () => 10 // 函数必须得有参数列表
    val f7: () => Int = () => 10
    // "="  后边是真正的参数列表定义和函数体， 其前边的则是显式的参数类型声明和返回类型声明

    def methodWithFunctionParam(fun:(Int) => Int): Int ={
        // 指定返回 Unit，则最后无论指定返回啥 也返回 Unit， 指定返回Int，则返回需要类型匹配

        // 方法名意味着方法调用，函数名只是代表函数自身：
        // 函数是一个赋值给一个变量（或者常量）的匿名方法（带或者不带参数列表）
        // scala 允许无参方法调用时省略（）括号, 而 function 可以作为最终的表达式出现
        val l = List(1,2,3, 4)
        println(l.map(fun))
        return fun(10)

        // 前缀操作符  op obj 被解释称 obj.op
        // 中缀操作符  obj1 op obj2 被解释称 obj1.op(obj2)
        // 后缀操作符  obj op 被解释称 obj.op
    }

    {
        println(f1)
        println(f2.getClass)
        println(f3(2))
        println(f4.getClass())
        println(f5(f4))
        println(methodWithFunctionParam(f3))

        // 方法转为函数的方式
        // 1- 方法名 后 跟一个 < _>， 显式的告诉编译器将方法转为函数
        val functionConvertFromMethod = methodWithFunctionParam _
        // 2- 当一个函数或者方法的参数是一个函数时, 传入一个方法，编译器会自动将方法本身转为函数,
        // 3- 显式定义一个函数，将方法赋予该函数变量
        val functionFromMethod:Int = getAge
        methodWithFunctionParam(x => setAge(x))  // 多此一举

        // 函数必须有参数列表，而方法可以没有参数列表
    }

    // object 是非抽象的，静态的，
    // java 中外部类不能是静态的，只有内部类才可以是静态的，内部静态类作为外部类的成员可以被直接访问。

    println("静态代码块，在加载的时候会执行！")
}


class Function{
    var age1 = Function.age     // age 是私有的，但是在伴生类中是可以直接访问的, C++ 中的友元类
    // def abstractMethod1: Int // 抽象方法， 则class 必为 抽象的，且必须声明为抽象的

    // def abstractMethod2(x: Int): String

    def functionParma(fun: (Int) => Int): Int = {fun(1)}

    def functionParma2(fun:Int=> Int): Int = fun(10)
}


object Method{
    var instance: Method = null
    def apply(f:Int=>String, x:Int): String ={ // 高阶函数
        println("apply 方法")
        if (instance==null) instance= new Method
        f(x)
        def fact(i:Int, accumulator: Int): Int = {
            if (i<=1){
                accumulator
            }else{
                fact(i-1, i*accumulator)
            }
        }
        "["+fact(x, 1) + "]"
    }

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

     import java.util.Date
     val today: Date = new Date()
     def log(date: Date, message: String): Unit ={
        println(date + "----" + message)
     }
     // 偏应用函数, 注意 : 两边都有空格
     val logWithBoundDate: String => Unit = log(today, _ : String)

     def curryingLog(date: Date)(message: String) = {
         // 函数柯里化，看起来和函数 偏应用化有一样的功效
         println(date + "----" + message)
     }
     def log(date: Date)=(message: String)=>{
         val re = date + "----" + message
         println(re)
         re
     }

     var outer_var:Int = 5

     val encloseFuncChangeOuterVar: Int => Int = (x:Int) => {

         outer_var = x * 5
         // scala 闭包中可以修改 外部变量的值，不需要去做什么声明，因为要想在函数内部在声明一个同名函数变量需要使用 关键字 var
         // 但是当引用了外部变量之后在定义一个同名变量，编译器则会报错  Wrong forward reference
         outer_var
         /// var outer_var = 6
     }

     // --------------------------------------------------------------------
     def anonymous = new Function1[Int, Int] {
         //TODO scala.FunctionX 是什么 鬼？ apply 这个函数名 为什么又那么特殊 ??
         def apply(x:Int):Int = x+1
     }

     def enlongArgs(enlongArgs:String *): Unit = { // 可变长参数 ×args， **args
        for (arg <- enlongArgs){
            println(arg)
        }
     }

     def time(): Long = {
         println("time", "语法太自由");
         val nt: Long = System.nanoTime()
         nt
     }
     def delayed(func:() => Long) {  // 没有指定返回类型， 没有 = 到函数体"
         println("delayed", "函数参数:", func, "函数执行:", func());
         func                        // Unused expression without side effects, 因为返回类型是一个空值
     }

    def delayed2(func:() => String, age:Int): ()=>String ={ // 返回一个函数, 参数是 常量 也就是说 是用 val 修饰的。
        println(age)
        func
    }

    def layout[A](x: A): String = {"["+ x.toString + "]"}

    def main(args: Array[String]): Unit = {
        val ins1 = Method(layout, 5)        // 直接调用无参构造函数 这就是 apply 函数吗？
        val ins2 = new Method()    // 调用无参构造并创建一个对象
        val ins3 = new Method()
        println(ins2.hashCode() == ins3.hashCode())
        val ins4 = new Method("自定义带参构造")
        println(ins1, ins2, ins4)
        println("-------------------------------------")
        println("无参方法执行", time)
        println("-------------------------------------")
        println(delayed(time))
        println("-------------------------------------")
        delayed2(age = 10, func = ()=>"haha")  // 函数参数按参数列表顺序赋值，或者 按 键赋值
        println("-------------------------------------")
        logWithBoundDate("hahha")
        println("------------------闭包测试-------------------")
        println(outer_var, encloseFuncChangeOuterVar(5), outer_var)
    }
}


class Method private{  // 私有化无参构造，隐藏构造器，但是对于主 object 不是私有的
    /**
     * scala 中无法在 class 中声明 成员为 static，所以
     * class 在未实例化之前，其中任何成员是不能被直接访问的， 也就是说 class 中的 main 方法是不能直接被调用的。
     *
     * 声明一个未用priavate修饰的字段 var age，scala编译器会字段帮我们生产一个私有字段和2个公有方法get和set,
     * 这和C#的简易属性类似； 若使用了private修饰，则它的方法也将会是私有的。这就是所谓的统一访问原则。
     */

    private[this] var age = 18    // 只能被 this 访问，编译器不会为起创建 getAge 和 setAge 方法
    private var name: String = _  // 预留变量，_ 是为了初始化。编译器自动为起创建 getName 和 setName 方法
    println("默认构造，在类Method中是一个无参构造")

    // def this(){this}  无参构造不能被被显式 定义，如果定义了，则在runtime 的时候 会出现 ambiguous 的错误，
    // 因为在scala 中 类体就是一个构造体，类不带参数的时候，就是无参构造。类带参数的时候，就是默认的构造函数，
    // 每一个类体内的其它构造函数都要显式调用与类参数列表一样的构造函数。

    def this(name: String) {
        this()                    // 同java 一样必须 在有参构造第一行 显式调用
        this.name = name
        println("带参构造")
    }

    def sayHello(){println("Helo Word!")}

    def getAge: Int = this.age

    def setAge(age: Int): Unit ={
        this.age = age
    }

    /**
     * 伴生类 中 定义 main 方法，会覆盖 object 的 main 方法，执行 object 中的main 方法 会出现 main 未定义的错误
     */
     // def main(args: Array[String]): Unit = {
     //   println("这个main 方法 是不能被 Method 调用的")
     // }
}
