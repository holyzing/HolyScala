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


class ClassMember {
    var username: String = _    // 底层编译为 private, 提供公有的 getter 和 setter 方法
    private var age: Int = _    // 底层编译为 private, 提供私有的 getter 和 setter 方法

    // val email: String = _    // 不能默认初始化
    val email: String = "lu"    // “类中”声明底层编译增加 final 修饰符, 只提供了getter方法 email

    // NOTE salca 所谓的 getter 和 setter 是不符合 java 大多数框架（El表达式，SSM）中 基于反射
    // NOTE 操作 Bean的 getXxx 和 setXxx 方法复规范的，为了和契合 它们提供了注解生成 getset方法

    @beanGetter var bean1: String = _
    @beanSetter var bean2: String = _
    @BeanProperty var bean3: String = _

    @Test
    def constructTest(): Unit ={
        // java1： 当并没有给定构造函数时，java 虚拟机会默认提供一个公共的无参构造
        // java2： 构造函数中调用其它构造函数时 使用 关键字 this(args list) 调用
        // java3： java 中无论是构造函数还非静态成员函数，都注入了 this 关键字，来实现互相调用。
        // java4:  jvm 为 java中每个构造函数的第一行增加了 super(),来实现父类的初始化。

        // scala1：scala中构造函数分两大类 主构造方法 和 辅构造方法，
        //         一定得提供主构造方法。而且辅助构造方法中必须调用主构造方法。
        //         （PS）守护线程为用户线程服务，守护线程不能独立用户线程运行
        // scala2：scala 是完全面向函数编程的语言，所以类也是函数，通过简化如下函数的定义，
        //          def Test(): String = {}，并将关键字def换为class，就可以简化为一个最简单的类。
        //

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
    }

    @Test
    def methodTest(): Unit ={
        // 所谓方法就是 定义在类中的函数, 但是在调用方式上与函数有一定的区别
        // 因为作用域不同，方法必须通过类的实例进行调用，
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
}
