package com.holy.scaler

import com.holy.ClassBase
import com.holy.scaler.innerPackage.InnferPackageObject
import org.junit.Test

import scala.annotation.meta.{beanGetter, beanSetter}
import scala.beans.BeanProperty

object ClassMember {
    def main(args: Array[String]): Unit = {
        val cm = new ClassMember          // 省略 ()
        println(cm.username)              // 调用 getter 方法 底层签名为 username
        cm.username = "lulu"              // 调用 setter 方法 底层签名为 username_$eq
        cm.username_=("lului")            // 像 +-*/ 作为标识符被翻译为 plus 等, = 被翻译为 $eq
        println(cm.age)                   // NOTE 伴生对象是可以访问伴生类的私有成员, 但是在外部是不能直接访问的.
        // NOTE 当方法只有一个参数时可以省略 . 和 ()
        // NOTE 并用 空格 依次连接 “调用者 被调用者 参数” 类似的还有 to until等方法的使用
        println(cm.+("xxx"), cm + "xxx")  // 和java 中的连字符相似，但比 java 中的强大，操作符重载

        val cm2 = ClassMember             // 单例的伴生类的实例
        val range1 = Range(1, 5)          // apply
        val range2 = Range(1, 5, 2)       // 方法重载
        println(cm2, range1, range2)

        val cm3 = ClassMember("lulu")     // NOTE 注意区分 apply 和 构造方法的区别, 构造方法必须使用 new 来调用

    }

    def apply(): ClassMember = new ClassMember()           //伴生对象创建伴生类的实例

    def apply(s: String): ClassMember = new ClassMember()  //伴生对象创建伴生类的实例
}

package p1 {
    package p2 {
        class UserP2 {
            var username = "zhangsan"
            private var password = "123123"         // 当前类UserP2能访问
            protected var email = "@xx.com"         // 子类能访问
            // private[p2] var address = "xxxxx"    // 当前包p2能访问
            //private[p3] var address = "xxxxx"     // 当前包p3能访问
        }
    }

    package p3 {
        import com.holy.scaler.p1.p2.UserP2
        class EmpP3 {
            def test(): Unit = {
                val user = new UserP2
                // user.username
                // user.address
            }
        }
    }
}

abstract class FatherClass{
    // 抽象方法不需要使用 abstract 声明, 抽象类不能实例化
    def abstractMethod()

    // 存在抽象的属性，则类必须是抽象类
    // 抽象属性在编译为字节码的时候不会生成类属性，只是生成了一个抽象的 gettter 方法
    var gender: String

    var lover: String = ""

    val email:String = ""

    def commonMethod(): Unit ={
        println("抽象类的普通方法")
    }
}


// private // 私有化构造器，但是对于主 object 不是私有的
class ClassMember(name: String) extends FatherClass {
    override var gender: String = _  // 必须重写父类抽象的属性, 且override 可以省略，反编译后子类生成属性并重写getter方法。
    // override var lover = ""       // 覆写父类普通属性，则必须得注解重写。但是var在编译时不会报错，运行时会报错
                                     // variable lover cannot override a mutable variable
    // NOTE 如果重写var 属性，由于动态绑定，以及 “统一访问原则”，（即当直接调用一个属性时，会默认调用他的get方法，
    // NOTE 当给一个属性赋值是，会调用他的set方法），通过子类实例访问父类中调用了父类同名属性的方法，而始终访问的是子类属性，
    // NOTE 这样父类的属性就失去了意义，所以不能重写 var 只能重写 val，因为 val 是不可变的，如果子类重写了这个 final 属性，
    // NOTE 那么在业务角度上是理解的，因为 final属性声明之后无法修改，子类重写只是”需要自己定义而已“。

    var username: String = _         // 底层编译为 private, 提供公有的 getter 和 setter 方法
    private var age: Int = _         // 底层编译为 private, 提供私有的 getter 和 setter 方法
    private[this] var age2: Int = _  // TODO 只能被 this 访问 ??????????????????????????????????????
    // val email: String = _             // val 不能默认初始化，因为它是不可变的
    override val email: String = "lu"    // “类中”声明底层编译增加 final 修饰符, 只提供了getter方法 email

    // NOTE salca 所谓的 getter 和 setter 是不符合 java 大多数框架（El表达式，SSM）中 基于反射
    // NOTE 操作 Bean的 getXxx 和 setXxx 方法复规范的，为了和契合 它们提供了注解生成 getset方法

    @beanGetter var bean1: String = _
    @beanSetter var bean2: String = _
    @BeanProperty var bean3: String = _

    println("NOTE 主构造方法体语句直接在类体中编写, 构造方法体中定义的成员都是类成员", name, age, age2)

    override def abstractMethod(): Unit = {  // override 可以省略
        println("实现抽象父类的抽象方法!")
    }

    override def commonMethod(): Unit = {
        // override 不可以省略, java 中 override 注解可以省略
        // super.commonMethod()
        println("重写抽象父类的普通方法")

    }

    def this(){
        this("lu")
        println("主构造方法的调用必须在第一行")
    }

    def this(name: String, age: Int){
        this(name)
        println(s"name:$name age:$age")
    }

    def this(age: Int){
        this("lu", age)
        println("构造方法的调用必须在第一行")
    }

    @Test
    def extendTest(): Unit ={
        // TODO 子类继承了父类的所有成员 (除了构造器), 但是 其 private 修饰的成员不能在" 子类" 中直接访问
        // TODO 子类不能重写父类的私有方法,子类重写父类的非私有方法时,方法访问权限必须大于父类中的访问权限.
        // THINK 在子类被创建的时候，首先会在内存中创建一个父类对象，然后在父类的外部加上子类独有的属性和方法，
        // THINK 两者共同组成了子类的一个对象, 在创建子类对象时，首先要调用父类的构造器.
        // THINK 如果二者出现同名属性的的时候 可通过 this 和 super 进行区分调用，默认this可以省略。
        // THINK 子类不能直接调用父类的 protected 的方法，需要子类重写之后，才能调用。

        val fatherRef = new ClassMember()
        val sonRef = new ClassMember()
        fatherRef.commonMethod()
        sonRef.commonMethod()
        /**
         * Java方法调用过程中，Jvm是如何知道调用的是哪个类的方法？Jvm又是如何处理？
         *
         * 静态绑定: 在编译阶段就已经在类的常量池中记录了 JVM要调用的方法具体在内存的什么位置上,
         *         这种在编译阶段就能够确定调用哪个方法的方式，我们叫做静态绑定机制
         *
         * Java中只有private、static和final修饰的方法以及构造方法是静态绑定。
         *      a、private方法的特点是不能被继承，也就是不存在调用其子类的对象，只能调用对象自身，因此private方法和定义该方法的类绑定在一起。
         *      b、static方法又称类方法，类方法属于类文件。它不依赖对象而存在，在调用的时候就已经知道是哪个类的，所以是类方法是属于静态绑定。
         *      c、final方法：final方法可以被继承，但是不能被重写，所以也就是说final方法是属于静态绑定的，因为调用的方法是一样的。
         *      总结：如果一个方法不可被继承或者继承后不可被覆盖，那么这个方法就采用的静态绑定。
         *
         * 动态绑定: 在程序运行过程中，通过动态创建的对象的方法表来定位方法的方式，我们叫做 动态绑定机制 。
         *          1-成员方法执行的过程中，JVM 会将执行的方法与调用对象的实际内存进行绑定
         *          2-成员属性没有动态绑定机制，"在哪使用就在哪寻址"，也就是说属性没有重写的概念。
         *          3-scala的语法结构上与java的上述问题表现不一致
         */

        val nf = new FatherClass {
            override def abstractMethod(): Unit = ???

            override var gender: String = _
        }
    }

    @Test
    def constructTest(): Unit ={
        // java1： 当并没有给定构造函数时，java 虚拟机会默认提供一个公共的无参构造, 各重载的构造函数之间没有强关联.
        // java2： 构造函数中调用其它构造函数时 使用 关键字 this(args list) 调用
        // java3： java 中无论是构造函数还非静态成员函数，都注入了 this 关键字，来实现互相调用。
        // java4:  jvm 为 java中每个构造函数的第一行增加了 super(),来实现父类的初始化。

        // scala1：scala中构造函数分两大类 主构造方法 和 辅构造方法，
        //         一定得提供主构造方法。而且辅助构造方法的调用链必须调用且只能调用一次主构造方法。
        //         （PS）守护线程为用户线程服务，守护线程不能独立用户线程运行
        // scala2：scala 是完全面向函数编程的语言，所以类也是函数，通过简化如下函数的定义，
        //         def Test(): String = {}，并将关键字def换为class，就可以简化为一个最简单的类。
        //         1- 无明确返回类型 2-可以类型推断 3-无参
        // scala3: 类所代表的函数就是类的构造函数, 显然,默认是无参构造, 而类体{} 就是构造方法体,而该构造方法就是该类的主构造方法.
        // scala4: 在主构造方法的方法体(构造体)中使用关键字 this 生命的构造方法就是辅助构造方法,
        //         辅构造方法的参数列表不能与主构造方法相同,因为重复定义了.
        // scala5: 辅构造方法的定义需要遵循被调用的辅构造方法先声明的原则.
    }

    @Test
    def access(): Unit ={
        // Java 可以通过反射绕过 其访问机制访问私有属性
        // protected void finalize() throws Throwable { }
        /**
         * JVM 在首次回收对象的时候会调用 finalize 进行回收前的前置操作, 通过该方法可延迟一次对象回收.
         */
        // protected native Object clone() throws CloneNotSupportedException;
        /**
         * clone 需要复制内存, java 需要调用本地方法(操作系统方法),因此用 那native修饰,调用本地方法
         *
         * 所谓权限就是方法的提供者和使用者之间的关系
         * 任何类的父类都是 Object 指的是类型上的关系, 而对于具体的实例来说他们的父类是不一样的,
         * 不能说两个实例的父类都是 Object 的, 他们拥有不同的父类.
         *
         * class A {
         *      override protected Object clone() throws CloneNotSupportedException {
         *          return super.clone()
         *      }
         * }
         *
         * public class JavaAccess{
         *      public static void main(String[] args) {
         *          A a = new A()
         *          a.clone()
         *      }
         * }
         * clone 提供者: a 而不是 java.lang.Object
         * clone 调用者: JavaAccess 中的main 而不是 a, 而 . 的真正的实际意义就是从属关系.
         *              是 main方法中调用 a 的 clone 方法, 不是 main 中 调用 Object 的clone,
         *              因为 在这里 main 和 Object 没有直接的关系.
         *
         *  为什么 子类实不能直接调用 protectewd clone 方法呢??? 首先说明一下 protected 权限范围:
         *      friendly: 默认也就是包访问权限,顾名思义,包访问权限,也就是必须同一个包中才可以,即使是子类不在同一个包中也不行的
         *      protected: 是默认包访问权限的扩展,默认是出了包的范围,就不行了,但是protected给予子类放行,
         *                 即使是包的外部,当然同一个包中的子类肯定是可以的
         *
         *      protected的含义是指子类可以访问,说的是子类直接访问父类的protected方法
         *      而不是说子类中,可以通过子类的实例或者父类的对象访问父类的protected方法
         *      子类(子类的实例)可以访问,可以在子类访问不是一个概念
         *
         *      你可以访问和在你家访问显然差别很大
         */
        // scala 中也有四种权限
        // public: 默认的访问权限, 不提供该关键字进行声明.
        // protected: 只能是子类访问,不能在同包中访问
        // friendly(package): 不提供关键字,需要采用特殊的语法结构, private[package] 指定的包下可以访问
        // private: 只能在本类中访问

        /**
         * public        ： default
         * private       ： java 允许外部类访问内部类的私有成员，但是 scala在嵌套类情况下，外层类甚至不能访问被嵌套类的私有成员。
         * protected     ： java 中的保护成员，除了定义了该成员的类的子类可以访问，同一个包里的其他类也可以进行访问。
         *               ： scala 只允许保护成员在定义了该成员的的类的子类中被访问。
         * friendly      ： java有 该限定符
         * scope limit   ： Scala中，访问修饰符可以通过使用限定词强调 private|protected[package|class|singleton-instance]
         *                  被修饰 结构 除了对[…]中的类或[…]中的包中的类及它们的伴生对像可见外，对其它所有类都是private。
         */
    }

    @Test
    def methodTest(): Unit ={
        // 所谓方法就是 定义在类中的函数, 但是在调用方式上与函数有一定的区别
        // 因为作用域不同，方法必须通过类的实例进行调用，
        // 方法转为函数的方式: 1-通过 _ 2-作为函数参数
        // NOTE scala 作为一种多范式变成语言,可以糅合多种编码风格来实现功能.
    }

    @Test
    def testPackage(): Unit ={
        ClassBase.test()                    // 包外伴生对象的引用
        val cb = new ClassBase()            // 包外伴生类的引用, 和当前类在同一包下.
        cb.packageDeclare()                 // 包外类实例的引用
        InnferPackageObject.test()          // 包内对象的调用
        scalar.say()                        // 包对象函数的调用

        innerPackage.innerPackage.ssy()     // 内包的包对象如果不用包同名,则需要通过包名调用
        innerPackage.innerPackage2.ssy()    // 子包不需要导入
    }

    // 伴生类 中 定义 main 方法，会覆盖 object 的 main 方法，执行 object 中的main 方法 会出现 main 未定义的错误
}

/**
 *
 *  类是对象的抽象，而对象是类的具体实例。类是抽象的，不占用内存，而对象是具体的，占用存储空间。
 *  类是用于创建对象的蓝图，它是一个定义包括在特定类型的对象中的方法和变量的软件模板。
 *
 *  Scala 的类定义可以有参数，称为类参数，类参数在整个类中都可以访问。
 *
 *   1、重写一个非抽象方法必须使用override修饰符。
 *   2、只有主构造函数才可以往基类的构造函数里写参数。
 *   3、在子类中重写超类的抽象方法时，你不需要使用override关键字。
 *   4、Scala 只允许继承一个父类。

 *   Scala 中使用单例模式时，除了定义的类之外，还要定义一个同名的 object 对象，它和类的区别是，object对象不能带参数。
 *   当单例对象与某个类共享同一个名称时，他被称作是这个类的伴生对象：companion object。
 *   必须在同一个源文件里定义类和它的伴生对象。类被称为是这个单例对象的伴生类：companion class。类和它的伴生对象可以互相访问其私有成员。
 */


/**
 *
 * java 中外部类不能是静态的，只有内部类才可以是静态的，内部静态类作为外部类的成员可以被直接访问。
 *
 * 声明一个未用priavate修饰的字段 var age，scala编译器会字段帮我们生产一个私有字段和2个公有方法get和set,
 * 这和C#的简易属性类似； 若使用了private修饰，则它的方法也将会是私有的。这就是所谓的统一访问原则。
 *
 * object 的成员在被虚拟机加载的时候会被初始化
 */
