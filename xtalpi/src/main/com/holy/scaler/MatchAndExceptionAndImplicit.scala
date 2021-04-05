package com.holy.scaler

import java.io.{FileNotFoundException, FileReader, IOException}

import org.junit.Test

/**
 * _ 的作用与意义：
 *      模式匹配 代表 default 的含义
 *      让虚拟机给定义的变量赋予初始值
 *      导包时候的全部导入，等价于java中的通配符 ×
 *      各类起别名，隐藏类
 *      当参数只被引用一次的时候，代替方法的参数
 *      让方法以方法返回，而不是执行方法
 *
 *      但是存在含义模糊，无法推断出类型的时候，比如 二维数组的flatmap，
 *      编译器无法推断出 _ 代替的是函数的参数还是函数本身
 *      从而默认会当成函数来对待
 */

class MatchAndExceptionAndImplicit{
    @Test
    def _Test(): Unit ={
        // val func = Array.ofDim[Int](2, 3).flatMap(_)  //NOTE win下无法通过编译
        val func = Array.ofDim[Int](2, 3).flatten
        println(func.mkString(","))
    }

    @Test
    def testBreak(): Unit ={
        // 控制抽象 将一段 代码块传递给函数
        import scala.util.control.Breaks
        Breaks.breakable({
            for (i <- 1  to 5){
                if (i % 2 == 0) Breaks.break
            }
        })
        Breaks.breakable{
            for (i <- 1  to 5){
                if (i % 2 == 1) Breaks.break
            }
        }

        def codeBlockArgFunc(op: => Unit): Unit ={ // 省略了参数列表 ()
            println(op)
            println("控制抽象内部")
        }
        codeBlockArgFunc({println("参数")})
        codeBlockArgFunc{println("参数")}

        def codeBlockArgFunc2(op: => Int): Unit ={ // 省略了参数列表 ()
            println(op)
            println("控制抽象内部")
        }
        codeBlockArgFunc2({3})                     // 代码块的最后一行就是该代码块的值
    }

    @Test
    def partialFunctionTest(): Unit ={
        // 偏函数应用场景 只对满足条件的数据做处理，对不满足条件的不做处理
        /**
         * 1) 使用构建特质的实现类(使用的方式是 PartialFunction 的匿名子类)
         * 2) PartialFunction 是个特质(看源码)
         * 3) 构建偏函数时，参数形式 [Any, Int]是泛型，第一个表示参数类型，第二个表示返回参数
         * 4) 当使用偏函数时，会遍历集合的所有元素，编译器执行流程时先执行 isDefinedAt()如果为 true ,
         * 就会执行 apply, 构建一个新的 Int 对象返回
         * 5) 执行 isDefinedAt() 为 false 就过滤掉这个元素，即不构建新的 Int 对象.
         * 6) map 函数不支持偏函数，因为 map 底层的机制就是所有循环遍历，无法过滤处理原来集合的元素
         * 7) collect 函数支持偏函数
         */

        val list = List(1, 2, 3, 4, 1.2, 2.4, 1.9f, "hello")
        //定义一个偏函数
        //1. PartialFunction[Any,Int] 表示偏函数接收的参数类型是 Any,返回类型是 Int
        //2. isDefinedAt(x: Any) 如果返回 true ,就会去调用 apply 构建对象实例,如果是 false,过滤
        //3. apply 构造器 ,对传入的值 + 1,并返回（新的集合）
        val partialFun = new PartialFunction[Any,Int] {
            override def isDefinedAt(x: Any): Boolean = {
                println("x=" + x)
                    x.isInstanceOf[Int]
            }
            override def apply(v1: Any): Int = {
                println("v1=" + v1)
                v1.asInstanceOf[Int] + 1
            }
        }
        //使用偏函数
        //说明：如果是使用偏函数，则不能使用 map,应该使用 collect
        //说明一下偏函数的执行流程
        //1. 遍历 list 所有元素
        //2. 然后调用 val element = if(partialFun-isDefinedAt(list 单个元素)) {partialFun-apply(list 单个元素)}
        //3. 每得到一个 element,放入到新的集合，最后返回
        val list1 = list.collect(partialFun)
        println("list1" + list1)

        //可以将前面的案例的偏函数简写
        def partialFun2: PartialFunction[Any,Int] = {
            //简写成 case 语句，不需要无条件匹配 case 表达式是有值的
            // TODO 注意 match...case... 语句始终是有返回值的，如果您无法匹配会报错
            //      而 {} 语句块中独立的 case 在无法匹配时会过滤掉当前匹配元素
            case i:Int => i + 1
            case j:Double => (j * 2).toInt
        }
        val list2 = list.collect(partialFun2)
        println("list2=" + list2)

        //第二种简写形式
        val list3 = list.collect{
            case i:Int => i + 1
            case j:Double => (j * 2).toInt
            case k:Float => (k * 3).toInt
        }
        println("list3=" + list3)

        // 1 match {case 2 => 2} match error
        // NOTE map sorWith 等函数均可使用 case 过滤掉不需要的元素
        println(list.map({ case i:Int => i}))

    }

    @Test
    def caseClassTest(): Unit ={
        abstract class Amount
        case object NoAmount extends Amount //样例类
        case class Dollar(value: Double) extends Amount //样例类
        case class Currency(value: Double, unit: String) extends Amount //样例类
        //类型(对象) =序列化(serializable)==>字符串(1.你可以保存到文件中【freeze】2.反序列化,2 网络传输)
        /*
        1) 样例类仍然是类
        2) 样例类用 case 关键字进行声明。
        3) 样例类是为模式匹配而优化的类
        4) 构造器中的每一个参数都成为 val——除非它被显式地声明为 var（不建议这样做）
        5) 在样例类对应的伴生对象中提供 apply 方法让你不用 new 关键字就能构造出相应的对象
        6) 提供 unapply 方法让模式匹配可以工作
        7) 将自动生成 toString、equals、hashCode 和 copy 方法(有点类似模板类，直接给生成，供程序员使用)
        8) 除上述外，样例类和其他类完全一样。你可以添加方法和字段，扩展它们
        */

        for (amt <- Array(Dollar(1000.0), Currency(1000.0, "RMB"), NoAmount)) {
            val result = amt match {
                case Dollar(v) => "$" + v
                case Currency(v, u) => v + " " + u
                case NoAmount => ""
            }
            println(amt + ": " + result)
        }

        val amt = Currency(3000.0, "RMB")  // 默认提供 apply 方法 可以省略 new
        val amt2 = amt.copy() // 克隆,创建的对象和 amt 的属性一样
        println("amt2.value" + amt2.value + " amt2.unit= " + amt2.unit)
        val amt3 = amt.copy(value = 8000.0)
        println("amt3.value" + amt3.value + " amt3.unit= " + amt3.unit)
        val amt4 = amt.copy(unit = "美元")
        println("amt4.value" + amt4.value + " amt4.unit= " + amt4.unit)

        // 什么是中置表达式？1 + 2，这就是一个中置表达式。如果 unapply 方法产出一个元组，你可以在
        // case 语句中使用中置表示法。比如可以匹配一个 List 序列
        List(1, 3, 5, 9) match { //修改并测试
            //1.两个元素间::叫中置表达式,至少 first，second 两个匹配才行.
            //2.first 匹配第一个 second 匹配第二个, rest 匹配剩余部分(5,9)
            case first :: second :: rest => println(first + " " + second + " " + rest.length + " " + rest)
            case _ => println("匹配不到...")
        }

        // 匹配嵌套结构
        abstract class Item
        case class Book(description: String, price: Double) extends Item
        case class Food(description: String, price: Double) extends Item
        case class Bundle(description: String, discount: Double, item: Item*) extends Item
        // 最佳实践案例-商品捆绑打折出售
        // 现在有一些商品，请使用 Scala 设计相关的样例类，完成商品可以捆绑打折出售。要求
        // 1) 商品捆绑可以是单个商品，也可以是多个商品
        // 2) 打折时按照折扣 xx 元进行设计.
        // 3) 能够统计出所有捆绑商品打折后的最终价格
        val sale = Bundle("书籍", 10, Book("漫画", 40), Bundle("文学作品", 20, Book("《阳关》", 80), Book("《围城》", 30)))
        //知识点 1 - 使用 case 语句，得到 "漫画"
        val res = sale match {
            //如果我们进行对象匹配时，不想接受某些值，则使用_ 忽略即可，_* 表示所有
            case Bundle(_, _, Book(desc, _), _*) => desc
        }
        println("res=" + res)
        //知识点 2-通过@表示法将嵌套的值绑定到变量。_*绑定剩余 Item 到 rest
        val res2 = sale match {
            case Bundle(_, _, art @ Book(_, _), rest @ _*) => (art, rest)
        }
        println("res2=" + res2)
        //知识点 3-不使用_*绑定剩余 Item 到 rest
        val res3 = sale match {
            case Bundle(_, _, art3 @ Book(_, _), rest3) => (art3, rest3)
        }
        println("res3=" + res3)
        def price(it:Item): Double = {
            it match {
                case Book(_,p) => p
                case Bundle(_,disc,its @ _*) => its.map(price).sum - disc
            }
        }
        println("price=" + price(sale)) // 120

        /**
         * 密封类：
         *     1-- 如果想让case类的所有子类必须都在声明case类的源码文件中定义，可以将 case 类
         *         声明为 sealed，即为隐匿类，也就说不能再其它文件中定义其子类
         */
    }

    @Test
    def matchTest(): Unit ={
        /**
         * java 中 的switch 语句支持的数据类型 有 byte(向上) char(ASCII) int string(hashcode equals)
         * 以上四种类型均可以的原因是均可以转为 int 类型
         * 必须使用 break 跳出case 不然会依次顺序执行，所谓 穿透。default不一定就要放在最后，但是会在最后被执行
         *
         * scala 的 match 表达式通过以代码编写的先后次序尝试每个模式来完成计算，只要发现有一个匹配的case，
         * 剩下的case则不会继续匹配，所谓不会穿透。如果最终无法匹配，则抛出 MatchError，但是java不会抛出异常
         * scala 中 case _ 表示无论如何都匹配，和 default 还是有区别，所以它的位置是不能随便调整的。
         */
        val x: Any = 2
        val res = x match {
            case 0 | "" => false
            case 2 | 4 | 6 | 8 | 10 => println("偶数")
            case x if x == 2 || x == 3 => println("two's company, three's a crowd")
        }
        println(res)

        def anyMatch(x: Any): Any = x match {
            case 1 => "one"
            case "two" => 2
            case y: Int => s"scala.Int $y"   // 高级匹配
            case _ => "any"                  // default
        }
        println(anyMatch())                  // 参数列表为 Any 的时候 可以不传 ？？？

        // -----------------------------------------------------------------------------------------
        // 使用了case关键字的类定义就是就是样例类(case classes)，样例类是种特殊的类，经过优化以用于模式匹配.
        case class Person(name: String, age: Int)

        val alice = Person("Alice", 25)
        val bob = Person("Bob", 32)
        val charlie = Person("Charlie", 32)

        for (person <- List(alice, bob, charlie)) {
            person match {
                case Person("Alice", 25) => println("Hi Alice!")
                case Person("Bob", 32)   => println("Hi Bob!")
                case Person(name, age)   => println("Age: " + age + " year, name: " + name + "?")
            }
        }

        object Square {
            //1. unapply 方法是对象提取器
            //2. 接收 z:Double 类型
            //3. 返回类型是 Option[Double]
            //4. 返回的值是 Some(math.sqrt(z)) 返回 z 的开平方的值，并放入到 Some(x)
            def unapply(z: Double): Option[Double] = {Some(math.sqrt(z))}  // None
            def apply(z: Double): Double = z * z
        }

        val number: Double = Square(5.0)// 36.0 //
        number match {
            //说明 case Square(n) 的运行的机制
            //1. 当匹配到 case Square(n)
            //2. 调用 Square 的 unapply(z: Double),z 的值就是 number
            //3. 如果对象提取器 unapply(z: Double) 返回的是 Some(6) ,则表示匹配成功，同时
            // 将 6 赋给 Square(n) 的 n
            //4. 果对象提取器 unapply(z: Double) 返回的是 None ,则表示匹配不成功
            case Square(n) => println("匹配成功 n=" + n)
            case _ => println("nothing matched")
        }
        // -----------------------------------------------------------------------------------------

        val (i, j) = (1, 2)             // 变量也必须以元组的形式给出
        val (q, r) = BigInt(10) /% 3    // q = BigInt(10) / 3 r = BigInt(10) % 3
        println(i * j, q + r)

        val arr = Array(1, 7, 2, 9)
        val Array(first, second, _*) = arr // 提出 arr 的前两个元素
        println(first, second)

        val ll = List(("a", 1), ("c", 2))
        for ((l, f) <- ll){println(l, f)}
        val mm = Map(("a", 1), ("c", 2))
        for ((k, v) <- mm){println(k, v)}

        // -----------------------------------------------------------------------------------------

        for (ch <- "+-3!") { //是对"+-3!" 遍历
            var sign = 0
            var digit = 0
            ch match {
                case '+' => sign = 1
                case '-' => sign = -1
                // 说明..
                // 如果 case 后有 条件守卫即 if ,那么这时的 _ 不是表示默认匹配
                // 表示忽略 传入 的 ch
                case _ if ch.toString.equals("3") => digit = 3
                case _ if ch > 1110 || ch < 120 => println("ch > 10")
                case c => println(c)  // 匹配成功后传递被匹配变量
                case _ => sign = 2
            }
        }

        val a = 8
        //说明 obj 实例的类型 根据 a 的值来返回
        val obj = if (a == 1) 1
        else if (a == 2) "2"
        else if (a == 3) BigInt(3)
        else if (a == 4) Map("aa" -> 1)
        else if (a == 5) Map(1 -> "aa")
        else if (a == 6) Array(1, 2, 3)
        else if (a == 7) Array("aa", 1)
        else if (a == 8) Array("aa")

        val result = obj match {
            case a: Int => a
            case b: Map[String, Int] => s"$b Map[String, Int]"
            case c: Map[Int, String] => s"$c Map[Int, String]"  // 底层是泛型，模式匹配不起作用
            case d: Array[String] => d                          // 数组中的泛型在底层是一个类型
            case e: Array[Int] => s"${e.mkString(",")}对象是一个数字数组"
            case f: BigInt => println(Int.MaxValue, f.getClass)
            case _ => "啥也不是"
        }
        println(result)

        val arrs = Array(Array(0), Array(1, 0), Array(0, 1, 0), Array(1, 1, 0), Array(1, 1, 0, 1))
        for (arr <- arrs) {
            val result = arr match {
                case Array(0) => "0"                    // 匹配只有一个 0 元素的数组
                case Array(x, y) => x + "=" + y         // 匹配长度为2的数组，一次传递给变量 x y
                case Array(0, _*) => "以 0 开头的数组"   // 表示以0 开头的数组
                case _ => "什么集合都不是"               // 无条件直接匹配
            }
            println("result = " + result)
        }

        for (list <- Array(List(0), List(1, 0), List(88), List(0, 0, 0), List(1, 0, 0))) {
            val result = list match {
                case 0 :: Nil => "0"              // 匹配只有一个元素0的列表
                case x :: y :: Nil => x + " " + y // 匹配只有两个元素的列表，且将元素依次赋值给变量 x, y
                case 0 :: tail => tail.size       // 匹配以0开头的元素，是元素的函数，被调用
                case x :: Nil => x                // 匹配只有一个元素的列表
                case _ => "something else"        // 无条件直接匹配
            }
            println(result)
        }

        for (pair <- Array((0, 1), (1, 0), (10, 30), (1, 1), (1, 0, 2))) {
            val result = pair match {
                case (0, _) => "0 ..." // 表示 匹配 Tuple2 第一个元素为 0，第二个任意
                case (y, 0) => y       // 表示 匹配 Tuple2 第一个元素任意，且将第一个元素传递给变量 y，第二个元素是0
                case (x, y) => (y, x)  // 表示 匹配 Tuple2 将两个元素依次赋值给 x, y
                case _ => "other"      // 无条件直接拿匹配
            }
            println(result)
        }
    }

    @Test
    def implicitTest(): Unit ={
        // ---------------------------------------------------------------------------------------
        // scala 默认情况下支持的隐式转换
        // 1-- 数值类型的类型提升 各个数值类型中都定义了它们的隐式转换
        // 2-- 多态语法下的类型转换，具体转抽象，子类转父类等
        // 3-- 当前环境下（作用域）不能出现多个相同签名（入参类型和返回类型一样）的隐式转换函数

        // 隐式函数 底层是 private final
        implicit def floatToInt(f: Float): Int ={f.toInt}
        val i: Int = 5.0f   // OCP 对功能扩展开放，对修改源码关闭
        println(i.toFloat)

        class Mysql{def select(): Unit ={println("select func")}}
        class Db{def delete(): Unit ={println("delete func")}}
        implicit def transform(mysql: Mysql): Db = {new Db}
        val mysql = new Mysql // 无参构造
        mysql.select()
        mysql.delete()        // THINK 找不到这个方法的时候就会隐式转换 ？？？？

        def sayname(name:String = "张三"): Unit ={println(name)}
        // sayname  // Missing arguments for method sayname(String)
        sayname()

        // implicit val age2: Int = 20  // ambiguous implicit val
        implicit val age: Int = 20
        // 隐式参数
        def sayage(implicit age:Int = 18): Unit ={
            println(age)
        }
        sayage      // 因为隐式参数，所以 () 可以省略
        sayage()
        sayage(19)

        implicit class IO(mysql: Mysql){def drop(): Unit ={mysql.delete(); println("drop func")}}
        mysql.drop()
    }

    @Test
    def regexTest() {
        import scala.util.matching.Regex
        val pattern = "Scala".r()              // 构造 Regex 对象
        val pattrer2 = new Regex("(S|s)cala")  // 管道(|)来设置不同的模式, 首字母可以是大写 S 或小写 s
        val str = "Scala is Scalable and cool"
        val str2 = "Scala is scalable and cool"
        println(pattern findFirstIn str, (pattern findAllIn str2).mkString(","))   // 使用逗号 , 连接返回结果
        println(pattrer2 replaceFirstIn(str, "Java"), pattern replaceAllIn(str2, "Java"))

        // Scala 的正则表达式继承了 Java 的语法规则，Java 则大部分使用了 Perl 语言的规则
    }

    @Test
    def exceptionTest(): Unit ={
        /**
         * Scala 抛出异常的方法和 Java一样，使用 throw 方法，例如，抛出一个新的参数异常：
         * 异常捕捉的机制与其他语言中一样，如果有异常发生，catch字句是按次序捕捉的。
         * 因此，在catch字句中，越具体的异常越要靠前，越普遍的异常越靠后。
         * 如果抛出的异常不在catch字句中，该异常则无法处理，会被升级到调用者处。
         */

        try {
            val f = new FileReader("input.txt")
            f.close()
        } catch {
            case ex: FileNotFoundException =>
                println("Missing file exception", ex)
            case ex: IOException =>
                println("IO Exception", ex)
            case ex:Throwable => // Execption extends Throwable
                println("Not predicted exception！", ex)
        } finally {
            println("Exiting finally...")
        }
        /**
         * java 中 try catch 中的异常是有大小范围 的，而异常的捕获是从上到下执行，
         * 所以得要求先捕获范围小的异常，在捕获范围大的异常。
         *
         * 而在 scala 中通过 模式匹配进行的异常捕获，是从上到下进行依次捕获，直到捕获为止，
         * 不考虑捕获异常的顺序，相当于 java 中 的 switch case break
         * 但是在实际编程中，显然应该是越具体的异常越靠前越好
         *
         * scala 中没有 checked 异常，都是在运行时进行捕获处理
         *
         * 模式匹配下的 case 可以省略 { }
         *
         */

        def testNothing(): Nothing ={
            throw new Exception("Wrong")
            // throw 表达式的类型是 Nothing 它是所有类型的子类型，所以 throw 表达式可以用在需要类型的地方。
        }

        // testNothing()

        try{
            testNothing()
        } catch {
            case e: Exception =>
                e.printStackTrace()
            case nfe: NumberFormatException =>
                nfe.fillInStackTrace()
        }

        @throws(classOf[NumberFormatException])
        def numberFormatToStr(numberStr: String): Int ={
            numberStr.toInt
        }

        try{
            numberFormatToStr("hhh")
        } catch {
            case nfe: NumberFormatException =>
                println("错误的数字字符串")
                nfe.printStackTrace()
        }

        numberFormatToStr("abc")

        /**
         * scala 提供了 throws 类（注解）来在定义一个函数时，声明一个函数在执行的过程中可能抛出的异常，
         */
    }
}
