package com.holy.extra

import org.apache.spark.util.AccumulatorV2


class VectorAccumulatorV2 extends AccumulatorV2[MyVector, MyVector]{

    private val myVector: MyVector = MyVector.createZeroVector

    override def isZero: Boolean = {
        false
    }

    override def copy(): AccumulatorV2[MyVector, MyVector] = {
        new VectorAccumulatorV2
    }

    override def reset(): Unit = {
        myVector.reset()
    }

    override def add(v: MyVector): Unit = {
        myVector.add(v)
    }

    override def merge(other: AccumulatorV2[MyVector, MyVector]): Unit = {

    }

    override def value: MyVector = {
        myVector
    }
}
