package com.holy.scaler

import com.holy.ClassBase
import com.holy.scaler.innerPackage.InnferPackageObject

object ClassMore{
    def main(args: Array[String]): Unit = {
        ClassBase.test()            // 包外伴生对象的引用
        val cb = new ClassBase()    // 包外伴生类的引用, 和当前类在同一包下.
        cb.packageDeclare()         // 包外类实例的引用
        InnferPackageObject.test()  // 包内对象的调用
        scalar.say()                // 包对象函数的调用
    }
}

class ClassMore {


}
