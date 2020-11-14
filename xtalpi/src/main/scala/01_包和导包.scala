/*×
Scala 特性
  面向对象特性
    Scala是一种纯面向对象的语言，每个值都是对象。对象的数据类型以及行为由类和特质描述。
    类抽象机制的扩展有两种途径：一种途径是子类继承，另一种途径是灵活的混入机制。这两种途径能避免多重继承的种种问题。

  函数式编程
    Scala也是一种函数式语言，其函数也能当成值来使用。Scala提供了轻量级的语法用以定义匿名函数，支持高阶函数，允许嵌套多层函数，并支持柯里化。
    Scala的case class及其内置的模式匹配相当于函数式编程语言中常用的代数类型。
    更进一步，程序员可以利用Scala的模式匹配，编写类似正则表达式的代码处理XML数据。

  静态类型
    Scala具备类型系统，通过编译时检查，保证代码的安全性和一致性。类型系统具体支持以下特性：

    泛型类
    协变和逆变
    标注
    类型参数的上下限约束
    把类别和抽象类型作为对象成员
    复合类型
    引用自己时显式指定类型
    视图
    多态方法
    扩展性

  Scala的设计秉承一项事实，即在实践中，某个领域特定的应用程序开发往往需要特定于该领域的语言扩展。
  Scala提供了许多独特的语言机制，可以以库的形式轻易无缝添加新的语言结构：

  任何方法可用作前缀或后缀操作符
  可以根据预期类型自动构造闭包。

  并发性
    Scala使用Actor作为其并发模型，Actor是类似线程的实体，通过邮箱发收消息。
    Actor可以复用线程，因此可以在程序中可以使用数百万个Actor,而线程只能创建数千个。
    在2.10之后的版本中，使用Akka作为其默认Actor实现。

  符号"$"在 Scala 中也看作为字母。然而以"$"开头的标识符为保留的 Scala 编译器产生的标志符使用，
  应用程序应该避免使用"$"开始的标识符，以免造成冲突。也应该避免使用以下划线结尾的标志符以避免冲突。

  符号标志符: + ++ ::: < ?>
            :-> $colon$minus$greater 【scala 的内部名称】
* */

// import的效果从开始延伸到语句块的结束
// 默认情况下，Scala 总会引入 java.lang._ 、 scala._ 和 Predef._

package com.holy.scala

import java.lang._
import java.awt.{Window, event}

package innerpackage{
  object packageTest{
    def main(args: Array[String]): Unit = {
      println("scala 的 跨包导入！")
    }
  }
}

class JavaClass {

  def val_var(args: Array[String]): Unit = {
    val a, b = 10  // 声明不可变变量
    var c, d = 5   // 声明可变变量
    val content: String = "hello world"
    println(args, content)
    println(a, b, c, d)
  }

  def handler(evt: event.ActionEvent): Unit = {

    import java.util.{HashMap => JavaHashMap}
    // scala 中不支持 java 语法，但是支持调用 java 对象和方法，所以说使用 java的 <> 进行泛型，是错误的
    // JavaHashMap jhm = new JavaHashMap<String, String>() 错误的语法
    val jhm: JavaHashMap[String, String] = new JavaHashMap[String, String]()
    import java.awt.{Window => _, Color, Font}  // _：隐藏成员
    println(Color.BLACK)
    System.out.println(Font.BOLD)
  }

  def main(args: Array[String]): Unit = {

  }
}

object _01_HelloWorld extends JavaClass {
  override def main(args: Array[String]): Unit = {
    val u: Unit = ()     // Unit 类型唯一的值 是 ()
    val u1: Unit = 2
    val u2 = ()
    val s1: String = "给"
    println(u, u1, u2)
    println(u.getClass(), u1.getClass(), u2.getClass())
    println(test1())
  }

  def test1(): Unit = {

  }
}
