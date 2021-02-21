package com.holy.extra

import org.apache.spark.util.AccumulatorV2

object MyVector {
    def createZeroVector: MyVector ={
        new MyVector
    }
}

class MyVector  {
    /**
     * representing mathematical vectors
     */
    def reset(): Unit ={

    }

    def add(V: MyVector): Unit ={
        V.reset()
    }

}



