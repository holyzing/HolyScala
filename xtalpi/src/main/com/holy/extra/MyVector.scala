package com.holy.extra

import org.apache.spark.util.AccumulatorV2
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.client.Result

import io.netty.buffer.PooledByteBufAllocator

import org.apache.spark.examples.pythonconverters.HBaseResultToStringConverter
import org.apache.spark.examples.pythonconverters.ImmutableBytesWritableToStringConverter
import org.apache.hbase.thirdparty.com.google.gson.GsonBuilder

import org.apache.hadoop.hbase.protobuf.generated.MasterProtos
import com.yammer.metrics.core.Gauge


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



