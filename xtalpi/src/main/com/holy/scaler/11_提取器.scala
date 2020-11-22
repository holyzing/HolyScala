package com.holy.scaler

/**
 * 提取器是从传递给它的对象中提取出构造该对象的参数。
 * Scala 标准库包含了一些预定义的提取器，我们会大致的了解一下它们。
 * Scala 提取器是一个带有unapply方法的对象。unapply方法算是apply方法的反向操作：
 * unapply接受一个对象，然后从对象中提取值，提取的值通常是用来构造该对象的值。
 */

object ExtractorTest {
    def main(args: Array[String]) {

        println("通过静态构造调用apply 方法: " + ExtractorTest(user = "holy", domain = "@lixue"))
        println("Apply 方法 : " + apply("Zara", "gmail.com"));
        println("Unapply 方法 : " + unapply("Zara@gmail.com"));
        println("Unapply 方法 : " + unapply("Zara Ali"));

        println("---------------------------------------------")

        val x = ExtractorTest(5)
        println(x)

        x match
        {   //TODO unapply 被调用 ?????
            case ExtractorTest(num) => println(x + " 是 " + num + " 的两倍！")
            case _ => println("无法计算")
        }

    }

    // 注入方法 (可选)
    def apply(user: String, domain: String) = {  // 不需要 new 操作就可以 构造对象
        user + "@" + domain
    }

    // 提取方法（必选）
    def unapply(str: String): Option[(String, String)] = {
        /**
         * unapply接受一个对象，然后从对象中提取值，提取的值通常是用来构造该对象的值。
         * 实例中使用 Unapply 方法从对象中提取用户名和邮件地址的后缀。
         */
        val parts = str split "@"
        if (parts.length == 2) {
            Some(parts(0), parts(1))
        } else {
            None
        }
    }

    // 函数重载
    def apply(x: Int) = x*2

    // None null Nothing, Nil
    def unapply(z: Int): Option[Int] = {
         if (z%2==0) Some(z/2) else None
        /**
         * 在实例化一个类的时，可以带上0个或者多个的参数，编译器在实例化的时会调用 apply 方法。可以在类和对象中都定义 apply 方法。
         * unapply 用于提取我们指定查找的值，它与 apply 的操作相反。 当在提取器对象中使用 match 语句时，unapply 将自动执行：
         */

    }
}
