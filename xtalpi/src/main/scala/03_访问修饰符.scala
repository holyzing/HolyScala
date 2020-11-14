package com.holy.scala

object accessDecorator {
    /**
     * public        ： default
     * private       ： java 允许外部类访问内部类的私有成员，但是 scala在嵌套类情况下，外层类甚至不能访问被嵌套类的私有成员。
     * protected     ： java 中的保护成员，除了定义了该成员的类的子类可以访问，同一个包里的其他类也可以进行访问。
     * ： scala 只允许保护成员在定义了该成员的的类的子类中被访问。
     * friendly      ： java有 该限定符
     * scope limit   ： Scala中，访问修饰符可以通过使用限定词强调 private|protected[package|class|singleton-instance]
     *                 被修饰 结构 除了对[…]中的类或[…]中的包中的类及它们的伴生对像可见外，对其它所有类都是private。
     */
}

class Outer {

    class Inner extends Outer {
        private def func(): Unit = {

        }

        protected def func1: Unit = {
            (new Inner).func()
        }

        class InnerMost {
            func()
            func1
        }

        func1
    }

    class OtherInner {
        // func1
    }

    // (new Inner).func()  # Symbol func is inaccessible from this place
}

package bobsrockets {
    package navigation {
        /*
            Navigator被标记为private[bobsrockets]:
                Navigator类对包含在bobsrockets包里的所有的类和对象可见。
                从Vehicle对象里对Navigator的访问是被允许的，因为对象Vehicle包含在包launch中，而launch包在bobsrockets中，
                相反，所有在包bobsrockets之外的代码都不能访问类Navigator。
        * */
        private[bobsrockets] class Navigator {
            protected[navigation] def useStarChart() {}

            class LegOfJourney {
                private[Navigator] val distance = 100
            }

            private[this] var speed = 200
        }

    }

    package launch {

        import navigation._

        object Vehicle {
            private[launch] val guide = new Navigator
        }

    }

}
