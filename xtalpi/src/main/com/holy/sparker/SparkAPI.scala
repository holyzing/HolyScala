package com.holy.sparker

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

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

        val arr = Array(1, 2, 3, 4, 5)
        val distData = sc.parallelize(arr)

        /**
        // val wholeTextFile =  sc.wholeTextFiles(filePath) // RDD[(String, String)]
        // sc.sequenceFile(filePath, Int.getClass, String.getClass, minPartitions = 1)

        // textFile.saveAsObjectFile("java对象序列化的方式序列化, 不如 AVro 高效")
        // sc.objectFile("加载被序列化的RDD")

        // -------------------------------------------------------------------------------------------------------------

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
        /**
         * Driver 通过 action 算子 触发集群的并行化操作, action算子中的参数会从 Driver 传递到 其它 worker 节点,
         * 参数如果是一个函数,则建议函数是独立的 (静态的,匿名的,全局的),如果不够独立,则需要将其所在的scope 也得一并传递.
         * 具体一点的场景就是 避免直接使用 this 引用一个对象内的成员，并直接传递个算子，而是将该对象成员 “固定” 到和算子一个局部内
         * 在进行传递。(python 和 scala 差不多)
         */

        var counter = 0
        // Wrong: Don't do this!!
        distData.foreach(x => counter += x)
        println("Counter value: " + counter)

        /**
         * Spark里一个比较难的事情就是，理解在整个集群上跨节点执行的变量和方法的作用域以及生命周
         * 期。Spark里一个频繁出现的问题就是RDD算子在变量作用域之外修改了其值
         * 如果不是在同一个 JVM 中执行，其表现将有很大不同. 例如，集合类和计算
         * 如果使用Spark本地模式（–master=local[n]）运行，和用spark-submit提交到YARN上）结果完全不同。
         *
         * 上面这段代码其行为是不确定的。在本地模式下运行，所有代码都在运行于单个JVM中，所以RDD的元素
         * 都能够被累加并保存到counter变量中，这是因为本地模式下，counter变量和驱动器节点在同一个内存空间中。
         *
         * 然而，在集群模式下，情况会更复杂，以上代码的运行结果就不是所预期的结果了。为了执行这
         * 个作业，Spark会将 RDD 算子的计算过程分割成多个独立的任务（task）- 每个任务分发给不同的执
         * 行器（executor）去执行。而执行之前，Spark需要计算闭包。闭包是由执行器执行RDD算子（本例中
         * 的foreach()）时所需要的变量和方法组成的。闭包将会被序列化，并发送给每个执行器。由于本地模式
         * 下，只有一个执行器，所有任务都共享同样的闭包。而在其他模式下，情况则有所不同，每个执行器都运
         * 行于不同的worker节点，并且都拥有独立的闭包副本。
         * 在上面的例子中，闭包中的变量会跟随不同的闭包副本，发送到不同的执行器上，所以等到foreach真正在
         * 执行器上运行时，其引用的counter已经不再是驱动器上所定义的那个counter副本了，驱动器内存中仍然会
         * 有一个counter变量副本，但是这个副本对执行器是不可见的！执行器只能看到其所收到的序列化闭包中包
         * 含的counter副本。因此，最终驱动器上得到的counter将会是0。
         * 为了确保类似这样的场景下，代码能有确定的行为，这里应该使用累加器（Accumulator）。累加器
         * 是Spark中专门用于集群跨节点分布式执行计算中，安全地更新同一变量的机制。本指南中专门有一节
         * 详细说明累加器。
         * 通常来说，闭包（由循环或本地方法组成），不应该改写全局状态。Spark中改写闭包之外对象的行为是未
         * 定义的。这种代码，有可能在本地模式下能正常工作，但这只是偶然情况，同样的代码在分布式模式下其
         * 行为很可能不是你想要的。所以，如果需要全局聚合，请记得使用累加器（Accumulator）。
         *
         * 另一种常见习惯是，试图用 rdd.foreach(println) 或者 rdd.map(println) 来打印RDD中所有的元素。
         * 如果是在单机上，这种写法能够如预期一样，打印出RDD所有元素。然后，在集群模式下，这些输出将会被
         * 打印到执行器的标准输出（stdout）上，因此驱动器的标准输出（stdout）上神马也看不到！
         * 如果真要在驱动器上把所有RDD元素都打印出来，你可以先调用collect算子，把RDD元素先拉到驱动器上来，代
         * 码可能是这样：rdd.collect().foreach(println)。不过如果RDD很大的话，有可能导致驱动器内存溢出，因
         * 为collect会把整个RDD都弄到驱动器所在单机上来；如果你只是需要打印一部分元素，那么take是更安全的
         * 选择：rdd.take(100).foreach(println)
         */

        /**
         * 大部分Spark算子都能在包含任意类型对象的RDD上工作，但也有一部分特殊的算子要求RDD包含的元素必须是键值对（key-value pair）。
         * 这种算子常见于做分布式混洗（shuffle）操作，如：以key分组或聚合。
         * 在Scala中，这种操作在包含 Tuple2 （内建与scala语言，可以这样创建：(a, b) ）类型对象的RDD上自动可用。
         * 键值对操作是在 PairRDDFunctions 类上可用，这个类型也会自动包装到包含tuples的RDD上。
         * 例如，以下代码将使用 reduceByKey 算子来计算文件中每行文本出现的次数：
         */

        val lines = sc.textFile("data.txt")
        val pairs = lines.map(s => (s, 1))
        val counts = pairs.reduceByKey((a, b) => a + b)

        /**
         * 同样，我们还可以用 counts.sortByKey() 来对这些键值对按字母排序，最后再用 counts.collect() 将数据以对象
         * 数据组的形式拉到驱动器内存中。
         * 注意：如果使用自定义类型对象做键值对中的key的话，你需要确保自定义类型实现了 equals() 方法（通常
         * 需要同时也实现hashCode()方法）。完整的细节可以参考：Object.hashCode()文档
         *
         * 详细请参考 RDD API doc (Scala, Java, Python, R) 以及 键值对 RDD 函数 (Scala, Java)
         */

        sc.stop()
    }
}

class SparkAPI {

}




