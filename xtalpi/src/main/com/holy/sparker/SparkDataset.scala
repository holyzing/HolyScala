package com.holy.sparker

import org.apache.spark.sql.SparkSession


object SparkDataset {

    val os: String = System.getProperty("os.name")

    var sparkHome: String = _  // IDE 推荐使用 _ 而不是 null, scala 中 的 _ 代表什么 ? println(_) 会报错

    if (os != null && os.toLowerCase().indexOf("linux")> -1){
        sparkHome = "/home/holyzing/snap/apache/spark-3.0.1-bin-hadoop2.7"
    } else {
        sparkHome = "F:\\"
    }
    sparkHome = sys.env.getOrElse("SPARK_HOME", sparkHome)

    def main(args: Array[String]): Unit = {
        datasetApi()
    }

    def datasetApi(): Unit = {
        val spark = SparkSession.builder().appName("SparkDatasetApi").master("local[*]").getOrCreate()
        // For implicit conversions like converting RDDs to DataFrames
        import spark.implicits._
        spark.sparkContext.setLogLevel("WARN")
        // DataFrame is a alias of Dataset in scal, but in java Dataset[Row] <==> DataFrame,
        // Dataset has not a python api, but python is a dynamic language, you can also use named row (i.e named tuple)
        // DataFrame API: JAVA Python R Scala

        val textFile = spark.read.json(sparkHome + "/examples/src/main/resources/people.json")
        textFile.show()
        spark.stop()
    }
}

class SparkDataset {

}

