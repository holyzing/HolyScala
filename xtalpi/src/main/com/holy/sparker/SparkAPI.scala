package com.holy.sparker

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

object SparkAPI {

    def main(args: Array[String]): Unit = {
        DatasetApi()
        rddApi()
    }

    def DatasetApi(): Unit ={
        val spark = SparkSession.builder().appName("SparkDatasetApi").master("local[*]").getOrCreate()
        val textFile = spark.read.textFile("C:\\Users\\holyz\\Desktop\\spark-test\\player.py")
        println(textFile, textFile.getClass)   // class org.apache.spark.sql.Dataset
        println(textFile.count(), textFile.first())
        spark.stop()
    }

    def rddApi(): Unit = {
        val conf = new SparkConf()
        conf.setMaster("local[*]")
        conf.setAppName("SparRDDApi")
        val sc = new SparkContext(conf)
        val textFile = sc.textFile("C:\\Users\\holyz\\Desktop\\spark-test\\player.py")
        println(textFile, textFile.getClass)  // org.apache.spark.rdd.MapPartitionsRDD
        println(textFile.count(), textFile.first())
        sc.stop()
    }
}

class SparkAPI {

}
