package com.holy

import org.junit.Test

object ClassBase {
    def main(args: Array[String]): Unit = {
        new ClassBase()
    }

    def test(): Unit ={
        println("伴生对象方法：test")
    }
}

package scaler {

    package object scalar{
        val name: String = "包对象"
        def say(): Unit ={
            println("包对象下的方法：say")
        }
    }

    package innerPackage{
        object InnferPackageObject{
            def main(args: Array[String]): Unit = {
                val cb = new ClassBase()
                println(cb.age)
            }

            def test(): Unit ={
                println("包中包的伴生对象 innerPackage 中的方法：test")
            }
        }
    }

}

class ClassBase {
    // val gender: String = _ Unbound placeholder parameter
    var age: Int = _ // 变量初始化
    var name: String = _

    @Test
    def packageDeclare(): Unit = {
        // 1- 默认声明方式 和 java 一样。源码文件开头声明。
        // 2- 在源码文件 中可以多次声明包，多次声明后源码文件中的类所在的包
        //    为从上到下直至类期间 所有 package 声明的 包的拼接。
        // 3- 源码中包的声明路径可以不与源码所在文件结构的路径保持一致。
        // 4- scala 中所有的语法结构可以互相嵌套
        // 5- 当 package 声明时携带了 包体 ，那么包体内的类和对象在包体的包中
        // 6- scala 中 子包引用父包中的类不需要导包。
        // 7- package 下只能声明类，但是这又违背了 scala中的语法嵌套，
        //    所以 scala 又提供了 包对象的概念。
        println("伴生类中的方法：packageDeclare")
    }

    def handler(): Unit = {
        // scala 中不支持 java 语法，但是支持调用 java 对象和方法，所以说使用 java的 <> 进行泛型，是错误的
        import java.util.{HashMap => JavaHashMap}
        val jhm: JavaHashMap[String, String] = new JavaHashMap[String, String]()
        jhm.put("k1", "v1")
    }
}


/**
 * Scala是一种纯面向对象的语言，每个值都是对象。对象的数据类型以及行为由类和特质描述。
 * 类抽象机制的扩展有两种途径：一种途径是子类继承，另一种途径是灵活的混入机制。这两种途径能避免多重继承的种种问题。
 *
 * Scala也是一种函数式语言，其函数也能当成值来使用。Scala提供了轻量级的语法用以定义匿名函数，
 * 支持高阶函数，允许嵌套多层函数，并支持柯里化。
 *
 * Scala的case class及其内置的模式匹配相当于函数式编程语言中常用的代数类型。
 * 更进一步，程序员可以利用Scala的模式匹配，编写类似正则表达式的代码处理XML数据。
 *
 * Scala具备类型系统，通过编译时检查，保证代码的安全性和一致性。类型系统具体支持以下特性：
 *
 * 泛型类
 * 协变和逆变
 * 标注
 * 类型参数的上下限约束
 * 把类别和抽象类型作为对象成员
 * 复合类型
 * 引用自己时显式指定类型
 * 视图
 * 多态方法
 * 扩展性
 *
 * Scala的设计秉承一项事实，即在实践中，某个领域特定的应用程序开发往往需要特定于该领域的语言扩展。
 * Scala提供了许多独特的语言机制，可以以库的形式轻易无缝添加新的语言结构：
 *
 * 任何方法可用作前缀或后缀操作符, 可以根据预期类型自动构造闭包。
 *
 * 并发性
 * Scala使用Actor作为其并发模型，Actor是类似线程的实体，通过邮箱发收消息。
 * Actor可以复用线程，因此可以在程序中可以使用数百万个Actor,而线程只能创建数千个。
 * 在2.10之后的版本中，使用Akka作为其默认Actor实现。
 *
 * 符号"$"
 * 在 Scala 中也看作为字母。然而以"$"开头的标识符为保留的 Scala 编译器产生的标志符使用，
 * 应用程序应该避免使用"$"开始的标识符，以免造成冲突。也应该避免使用以下划线结尾的标志符以避免冲突。
 *
 * 符号标志符:
 * + ++ ::: < ?>
 * *:-> $colon$minus$greater 【scala 的内部名称】
 *
 * import的效果从开始延伸到语句块的结束
 * 默认情况下，Scala 总会引入 java.lang._ 、 scala._ 和 Predef._
 */








