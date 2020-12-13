package com.holy.sparker

import org.apache.spark.sql.SparkSession


object SparkDataset {

    val os: String = System.getProperty("os.name")

    var sparkHome: String = _  // IDE 推荐使用 _ 而不是 null, scala 中 的 _ 代表什么 ? println(_) 会报错

    if (os != null && os.toLowerCase().indexOf("linux")> -1){
        sparkHome = "/home/holyzing/snap/apache/spark-3.0.1-bin-hadoop2.7"
    } else {
        sparkHome = "F:/apache/spark-3.0.1-bin-hadoop2.7"
    }
    sparkHome = sys.env.getOrElse("SPARK_HOME", sparkHome)

    def main(args: Array[String]): Unit = {
        datasetApi()
    }

    def datasetApi(): Unit = {
        val spark = SparkSession.builder().appName("SparkDatasetApi").master("local[*]").getOrCreate()
        // For implicit conversions like converting RDDs to DataFrames
        spark.sparkContext.setLogLevel("WARN")
        // DataFrame is a alias of Dataset in scal, but in java Dataset[Row] <==> DataFrame,
        // Dataset has not a python api, but python is a dynamic language, you can also use named row (i.e named tuple)
        // DataFrame API: JAVA Python R Scala
        // DataFrame: 无类型的但肯定是强类型的 （类型是 Row），而DataSet 是强类型的

        val df = spark.read.json(sparkHome + "/examples/src/main/resources/people.json")
        df.printSchema(level = 0)
        df.select("name").show()

        val sTest = "dasd"
        println(s"$sTest hahah")

        // df.select($"name", $"age" + 1).show() // scala-shell 中的 $ 符号
        // df.filter($"age" > 21).show()
        df.groupBy("age").count().show()
        // string manipulation, date arithmetic, common math operations and more.
        // The complete list is available in the DataFrame Function Reference.

        // Global temporary view is tied to a system preserved database `global_temp`
        df.createGlobalTempView("people")
        spark.sql("SELECT * FROM global_temp.people").show()
        // Global temporary view is cross-session
        // THINK 可以有多个 SparkSession 但是只能有一个 SparkContext ？
        spark.newSession().sql("SELECT * FROM global_temp.people").show()
        spark.stop()
    }
}

class SparkDataset {

}

