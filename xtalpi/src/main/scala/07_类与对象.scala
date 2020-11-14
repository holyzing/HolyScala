package com.holy.scala

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
    override def toString(): String ={
        return String.format("location is : (%s, %s, %s)", this.xc, this.yc, this.zc)
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

