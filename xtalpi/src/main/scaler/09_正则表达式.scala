package scaler

import scala.util.matching.Regex

object RegTest {
    def main(args: Array[String]) {
        val pattern = "Scala".r()              // 构造 Regex 对象
        val pattrer2 = new Regex("(S|s)cala")  // 管道(|)来设置不同的模式, 首字母可以是大写 S 或小写 s
        val str = "Scala is Scalable and cool"
        val str2 = "Scala is scalable and cool"
        println(pattern findFirstIn str, (pattern findAllIn str2).mkString(","))   // 使用逗号 , 连接返回结果
        println(pattern replaceFirstIn(str, "Java"), pattern replaceAllIn(str2, "Java"))

        // Scala 的正则表达式继承了 Java 的语法规则，Java 则大部分使用了 Perl 语言的规则
    }
}
