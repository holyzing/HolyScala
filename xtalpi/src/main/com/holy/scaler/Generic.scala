package com.holy.scaler

import org.junit.Test

class Generic {
    @Test
    def genericTest(): Unit ={
        // java 中泛型声明的不变性，引用和定义 前后泛型类型必须保持一致
        // java 中的在编译器的泛型引用校验，只能对使用泛型之后的代码起作用
        /**
         * List l1 = new ArrayList()
         * l1.add(new Student)
         * List<Person> l2 = l1
         * l2.add(new Person)
         *
         * sysout(l2)
         *
         * 类树
         *      Class<? extend Father>  上限
         *      Class<? super Son>      下限
         *
         */
        class Person{}
        class User extends Person{}
        class Student extends User{}

        def testGeneric1[T <: User](t: T): Unit ={println(t)}
        def testGeneric2[T >: User](t: T): Unit ={println(t)}
        testGeneric1(new Student)
        // testGeneric1(new Person)  // 编译时异常
        testGeneric2(new Person)
        testGeneric2(new Student)    // 该泛型限制编译为字节码后，就是一个无限制泛型，没什么卵用

        class Base[Person]{}

        // val b: Base[Person] = new Base[User]() // 也存在泛型的不变性
        // println(b)
        // 为了丰富泛型，受制于和 java 泛型引用一样的不变形，scala 中提供了泛型的 协变和 逆变
        // 所谓 协变 就是向下兼容，做出功能扩展，所谓 逆变 就是向上兼容，做出功能缩减

        class Base2[+User]{}  // 协变
        val b2: Base2[User] = new Base2[Student]

        class Base3[-User]{}  // 逆变
        val b3: Base3[User] = new Base3[Person]
        println(b2, b3)

    }
}
