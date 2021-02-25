package com.holy.scaler

import com.holy.ClassBase
import com.holy.scaler.innerPackage.InnferPackageObject

object ClassMore{
    def main(args: Array[String]): Unit = {
        ClassBase.test()
        val cb = new ClassBase()
        cb.packageDeclare()
        InnferPackageObject.test()
        scalar.say()
    }
}

class ClassMore {

}
