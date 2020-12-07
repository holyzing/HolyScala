package com.holy.sparker

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.mapred.{InputFormat, InputSplit, JobConf, RecordReader, Reporter}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

object SparkAPI {

    var filePath: String = ""
    val os: String = System.getProperty("os.name")

    if (os != null && os.toLowerCase().indexOf("linux")> -1){
        filePath = "/home/holyzing/Desktop/marvin-prod-20201125.db"
    } else {
        filePath = "C:\\Users\\holyz\\Desktop\\spark-test\\player.py"
    }


    def probelms(): Unit = {
        val str: String = "大声道撒大所大所"
        val sizeText = str.size
        val lengthText = str.length // str.length() 是一样的
        println(sizeText.getClass, sizeText, lengthText.getClass, lengthText)

        /*
         * JAVA 中 length 是 数组的属性 length() 是字符串的方法, size() 是集合的方法
         */
    }

    def main(args: Array[String]): Unit = {
        DatasetApi()
        // rddApi()
    }

    def DatasetApi(): Unit = {
        val spark = SparkSession.builder().appName("SparkDatasetApi").master("local[*]").getOrCreate()
        spark.sparkContext.setLogLevel("WARN")
        import spark.implicits._
        val textFile = spark.read.textFile(SparkAPI.filePath)

        def base(): Unit = {
            println(textFile, textFile.getClass) // class org.apache.spark.sql.Dataset
            println(textFile.count(), textFile.first())

            val lineFilter: String => Boolean = line => line.contains("insert")
            val insertCount = textFile.filter(lineFilter).count()
            println(insertCount)

            val mapToLineSize = textFile.map(line => line.split(" ").length)
            println(mapToLineSize.tail(10).mkString("[", ", ", "]"))
            val getMax: (Int, Int) => Int = (a, b) => Math.max(a, b)
            val reduceToMax = mapToLineSize.reduce(getMax) // (a, b) => if (a > b) a else b
            println(reduceToMax)
        }

        def diffMapReduce(): Unit = {
            //WordCount
            val lineList = List(
                "hadoop, spark,hadopp,hive",
                "spark,spark ,mapreduce",
                "hive  ,spark ,hive,   "
            )
            //Map：对数据进行分割，压平
            val mapperWords: List[(String, Int)] = lineList
                .flatMap(line => line.split(",").map(line => line.trim))
                .filterNot(line => line.isEmpty)
                .map(word => (word, 1))

            //数据分组
            val groupWords: Map[String, List[(String, Int)]] = mapperWords.groupBy(tuple => tuple._1)

            //reduce:每组进行数据聚合计算
            val result: Map[String, Int] = groupWords
                .map(tuple => {
                    //获取单词：
                    val word = tuple._1
                    //计算该word对应的数量
                    val count = tuple._2.map(t => t._2).sum
                    //返回结果
                    (word, count)
                })
            //遍历输出结果
            result.foreach(println)
        }

        def mapReduce(): Unit = {
            val wordCounts = textFile.flatMap(
                line => line.split(" ").map(word => word.trim).filterNot(word => word.isEmpty))
            wordCounts.cache()

            // val wordCountsGroupByIdentity = wordCounts.groupByKey(identity).count()
            // val result = wordCountsGroupByIdentity.collect()
            // println(result, result.length)
            // println("------------------------")
            // println(result.head)
            // println("------------------------")
            // println(result.tail)
            // println("------------------------")

            val groupByFun: ((String, Int)) => String = tuple => tuple._1
            val wordCounts2 = wordCounts.map(word => (word, 1))
            println(wordCounts2, "<==========>", wordCounts2.getClass)
            println("------------------------")
            val groupByColumn = wordCounts2.columns
            println(groupByColumn.getClass, groupByColumn(0), groupByColumn(1))
            println("------------------------")
            val result2 = wordCounts2.groupBy(groupByColumn.head).count().collect()
            println(result2(0), result2(1), result2.head)
        }

        mapReduce()
        spark.stop()
    }

    def rddApi(): Unit = {
        /**
         * 共享变量是一种可以在并行操作之间共享使用的变量。默认情况
         * 下，当Spark把一系列任务调度到不同节点上运行时，Spark会同时把每个变量的副本和任务代码一起发送给
         * 各个节点。但有时候，我们需要在任务之间，或者任务和驱动器之间共享一些变量。Spark 支持两种类型的
         * 共享变量：广播变量 和 累加器，广播变量可以用于在各个节点上缓存数据，而累加器则是用来执行跨节点
         * 的 “累加” 操作，
         */
        import org.apache.spark.SparkContext._

        val conf = new SparkConf()
        conf.setMaster("local[*]")
        conf.setAppName("SparRDDApi")
        val sc = new SparkContext(conf)
        sc.setLogLevel("warn")
        // 分区数不能小于 block 数, 默认一个block一个分区4
        val textFile = sc.textFile(filePath, 4) // RDD[String]
        // println(textFile, textFile.getClass)  // org.apache.spark.rdd.MapPartitionsRDD
        // val insertCount = textFile.filter(line => line.contains("insert")).count()  // 使用了泛型
        // textFile.map(line =>  line.split("").length)

        // val wholeTextFile =  sc.wholeTextFiles(filePath) // RDD[(String, String)]
        // sc.sequenceFile(filePath, Int.getClass, String.getClass, minPartitions = 1)

        // textFile.saveAsObjectFile("java对象序列化的方式序列化, 不如 AVro 高效")
        // sc.objectFile("加载被序列化的RDD")

        /**
        val inputFormat = new InputFormat[String, Int] {
            override def getSplits(jobConf: JobConf, i: Int): Array[InputSplit] = ???
            override def getRecordReader(inputSplit: InputSplit,
                                         jobConf: JobConf, reporter: Reporter): RecordReader[String, Int] = ???
        }
        sc.hadoopRDD(new JobConf(), inputFormat.getClass, "".getClass, Int.getClass)
        sc.newAPIHadoopRDD(Configuration ,"".getClass, "".getClass, 8.getClass)
        */

        /*
            action 算子在聚合后,会将聚合结果传回给 Driver,
            例如reduce 和  reduceByKey, 前者聚合返回一个泛型指定的类型,而后者则返回一个 RDD这种的 Map 集合
            transformation 是懒执行的, 当触发action 操作的时候,才会 触发 transfer,并不会在driver端保存中间数据,
            中间过程转换形成 的 RDD 可以调用 cache 和 persist 进行持久化,以便于后续重复使用,而不需要重新计算
         */
        val arr = Array(1, 2, 3, 4, 5)
        val distData = sc.parallelize(arr)
        sc.stop()
    }
}

class SparkAPI {

}




