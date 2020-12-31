package com.holy.sparker

import org.apache.spark.sql.{Encoder, SparkSession}


object SparkDataset {

    val os: String = System.getProperty("os.name")

    var sparkHome: String = _  // IDE 推荐使用 _ 而不是 null, scala 中 的 _ 代表什么 ? println(_) 会报错
    var workHome: String = _

    if (os != null && os.toLowerCase().indexOf("linux")> -1){
        sparkHome = "/home/holyzing/snap/apache/spark-3.0.1-bin-hadoop2.7"
        workHome = "/home/holyzing/xtalpi/My/_03_Scala/Scala/xtalpi"
    } else {
        sparkHome = "F:/apache/spark-3.0.1-bin-hadoop2.7"
        workHome = "F:/mywork/Scala/xtalpi"
    }
    sparkHome = sys.env.getOrElse("SPARK_HOME", sparkHome)

    def main(args: Array[String]): Unit = {
        datasetApi()
    }

    case class Person(name: String, age: Long)
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

        /**
         * Dataset 不是使用java 原生的序列化 或者 kryo 进行网络间对象的传输，
         *  While both encoders and standard serialization are responsible for turning an object into bytes,
         *  encoders are code generated dynamically and use a format that allows Spark to perform many
         *  operations like filtering, sorting and hashing without deserializing the bytes back into an object.
         */
        import spark.implicits._

        val caseClassDS = Seq(Person("Andy", 32)).toDS()
        caseClassDS.show()

        // Encoders for most common types are automatically provided by importing spark.implicits._
        val primitiveDS = Seq(1, 2, 3).toDS()
        primitiveDS.map(_ + 1).collect()        // Returns: Array(2, 3, 4)
        primitiveDS.map(x => x + 2).collect()
        // DataFrames can be converted to a Dataset by providing a class. Mapping will be done by name

        val peopleDS = df.as[Person]
        peopleDS.show()

        val peopleDF = spark.sparkContext
            .textFile("examples/src/main/resources/people.txt")
            .map(_.split(","))
            .map(attributes => Person(attributes(0), attributes(1).trim.toInt))
            .toDF()
        // Register the DataFrame as a temporary view
        peopleDF.createOrReplaceTempView("people")
        val teenagersDF = spark.sql("SELECT name, age FROM people WHERE age BETWEEN 13 AND 19")
        // The columns of a row in the result can be accessed by field index
        teenagersDF.map(teenager => "Name: " + teenager(0)).show()
        teenagersDF.map(teenager => "Name: " + teenager.getAs[String]("name")).show()

        // No pre-defined encoders for Dataset[Map[K,V]], define explicitly
        implicit val mapEncoder: Encoder[Map[String, Any]] = org.apache.spark.sql.Encoders.kryo[Map[String, Any]]
        // Primitive types and case classes can be also defined as
        // implicit val stringIntMapEncoder: Encoder[Map[String, Any]] = ExpressionEncoder()

        // row.getValuesMap[T] retrieves multiple columns at once into a Map[String, T]
        teenagersDF.map(teenager => teenager.getValuesMap[Any](List("name", "age"))).collect()
        // Array(Map("name" -> "Justin", "age" -> 19))

        // -----------------------------------------------------------------------------------
        import org.apache.spark.sql.Row
        import org.apache.spark.sql.types._

        // Create an RDD
        val peopleRDD = spark.sparkContext.textFile("examples/src/main/resources/people.txt")

        // The schema is encoded in a string
        val schemaString = "name age"  // 字段不确定的，事先无法定义的结构化数据

        // Generate the schema based on the string of schema
        val fields = schemaString.split(" ")
            .map(fieldName => StructField(fieldName, StringType, nullable = true))
        val schema = StructType(fields)

        // Convert records of the RDD (people) to Rows
        val rowRDD = peopleRDD
            .map(_.split(","))
            .map(attributes => Row(attributes(0), attributes(1).trim))

        // Apply the schema to the RDD   NOTE 按顺序映射
        val peopleDF2 = spark.createDataFrame(rowRDD, schema)
        peopleDF2.createOrReplaceTempView("people")
        val results = spark.sql("SELECT name FROM people")
        // The results of SQL queries are DataFrames and support all the normal RDD operations
        // The columns of a row in the result can be accessed by field index or by field name
        results.map(attributes => "Name: " + attributes(0)).show()
        spark.stop()
    }
}

class SparkDataset {

}

