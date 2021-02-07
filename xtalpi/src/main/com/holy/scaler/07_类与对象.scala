package com.holy.scaler

/**
 * scala 中没有 static 关键字，所以没有静态方法供虚拟机直接调用，为了添加 static 特性，提供了 object 关键字定义一个单例对象。
 * object 中的 apply 方法 ？？？？？？？？？？？？？
 *
 *  类是对象的抽象，而对象是类的具体实例。类是抽象的，不占用内存，而对象是具体的，占用存储空间。
 *  类是用于创建对象的蓝图，它是一个定义包括在特定类型的对象中的方法和变量的软件模板。
 *
 *  Scala中的类不声明为public，一个Scala源文件中可以有多个类。
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
 * scala 中无法在 class 中声明 成员为 static，所以
 * class 在未实例化之前，其中任何成员是不能被直接访问的， 也就是说 class 中的 main 方法是不能直接被调用的。
 *
 * java 中外部类不能是静态的，只有内部类才可以是静态的，内部静态类作为外部类的成员可以被直接访问。
 *
 * 声明一个未用priavate修饰的字段 var age，scala编译器会字段帮我们生产一个私有字段和2个公有方法get和set,
 * 这和C#的简易属性类似； 若使用了private修饰，则它的方法也将会是私有的。这就是所谓的统一访问原则。
 *
 * object 的成员在被虚拟机加载的时候会被初始化
 */



class Geometry {

}


class Point(xc: Int, yc: Int) extends Geometry {
    var x: Int = xc
    var y: Int = yc

    def move(dx: Int, dy: Int) {
        x = x + dx
        y = y + dy
        println ("x 的坐标点: " + x);
        println ("y 的坐标点: " + y);
    }
}


class Location private( val xc: Int,  val yc: Int,  val zc :Int) extends Point(xc, yc){ // 私有构造
    var z: Int = zc

    def move(dx: Int, dy: Int, dz: Int) {
        x = x + dx
        y = y + dy
        z = z + dz
        println ("x 的坐标点 : " + x);
        println ("y 的坐标点 : " + y);
        println ("z 的坐标点 : " + z);
    }

    // 重写一个函数 可以改变其返回类型
    override def toString: String ={
        // TODO return String.format("location is : (%s, %s, %s)", this.xc, this.yc, this.zc)
        return ""
    }
}

// 伴生对象，与类名字相同，可以访问类的私有属性和方法
object Location{
    private val locations: Map[String, Location] = Map(
        "center" -> new Location(xc = 0, yc=0, zc=0),
        "one" -> new Location(xc = 1, yc=1, zc=1),
        "two" -> new Location(xc = 2, yc=2, zc=2)
    )

    def apply(loca:String): Location = {
        if(locations.contains(loca)) locations(loca) else null
    }

    def getMarker(loca:String): Location = {
        if(locations.contains(loca)) locations(loca) else null
    }

    def main(args: Array[String]): Unit = {
        val loc: Location = new Location(1,2, 3)
        println(Location("center"), loc)
        loc.move(1,2,3)
    }
}

class Method private{  // 私有化无参构造，隐藏构造器，但是对于主 object 不是私有的
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
}
