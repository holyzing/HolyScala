package com.holy.sparker

import java.net.URLClassLoader

import org.apache.hadoop.security.UserGroupInformation
import org.apache.spark.sql.SparkSession
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

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

    }

}


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


object SparkRDD {

    var filePath: String = ""
    val os: String = System.getProperty("os.name")

    if (os != null && os.toLowerCase().indexOf("linux")> -1){
        filePath = "/home/holyzing/Desktop/marvin-prod-20201125.db"
    } else {
        filePath = "C:\\Users\\holyz\\Desktop\\spark-test\\player.py"
    }


    def probelms(): Unit = {
        val str: String = "大声道撒大所大所"
        val sizeText = str.size     // immutable 使用 length mutable 使用 size
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
        val textFile = spark.read.textFile(SparkRDD.filePath)

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
         *
         * 默认情况下,输出计算的并行度取决于源RDD的分区个数。当然,你也可以通过设置可选参数 numTasks 来指定并行任务的个数。
         * 按key分组聚合的话(如sum或average),推荐使用 reduceByKey或者 aggregateByKey 以获得更好的性能,而不是用groupBykey.
         */

        /**
         * 有一些Spark算子会触发众所周知的混洗(Shuffle)事件。Spark中的混洗机制是用于将数据重新分布,其结果是所有数据将在各个分区间重新分组。
         * 一般情况下,混洗需要跨执行器(Executor)或跨机器复制数据,这也是混洗操作一般都比较复杂而且开销大的原因。
         *
         * 在 Spark中,通常是由于为了进行某种计算操作,而将数据分布到所需要的各个分区当中。
         * 而在计算阶段,单个任务(task)只会操作单个分区中的数据 –
         * 因此,为了组织好每个 reduceByKey 中 reduce 任务执行时所需的数据,Spark需要执行一个多对多操作。
         * 即,Spark需要读取RDD的所有分区,并找到所有key对应的所有values,然后跨分区传输这些values,
         * 并将每个key对应的所有values放到同一分区,以便后续计算各个key对应values的reduce结果 – 这个过程就叫做混洗(Shuffle)。
         * 混洗后 各个元素按 Key 被组织到 各个分区,但是每个 Key 对应的 Values 是无序的,你可以使用如下高效算子进行排序分区:
         *
         * • mapPartitions 使用 .sorted 对每个分区排序
         * • repartitionAndSortWithinPartitions 重分区的同时,对分区进行排序,比自行组合repartition和sort更高效
         * • sortBy 创建一个全局有序的RDD
         *
         * 会导致混洗的算子有:
         *      重分区(repartition)类算子,如: repartition 和 coalesce;
         *      ByKey 类算子(除了计数类的,如 countByKey) 如:groupByKey 和 reduceByKey;
         *      Join类算子,如:cogroup 和 join.
         *
         * 混洗(Shuffle)之所以开销大,是因为混洗操作需要引入磁盘I/O,数据序列化以及网络I/O等操作。
         * 为了组织好混洗数据,Spark需要生成对应的任务集 – 一系列 map 任务用于组织数据,再用一系列reduce任务来聚合数据.
         *
         * 在Spark内部,单个map任务的输出会尽量保存在内存中,直至放不下为止。然后,这些输出会基于目标分区重新排序,并写到一个文件里。
         * 在reduce端,reduce任务只读取与之相关的并已经排序好的blocks。
         * 某些混洗算子会导致非常明显的内存开销增长,因为这些算子需要在数据传输前后,在内存中维护组织数据记录的各种数据结构。
         * 特别地,reduceByKey和aggregateByKey都会在map端创建这些数据结构,而ByKey系列算子都会在reduce端创建这些数据结构。
         * 如果数据在内存中存不下,Spark会把数据吐到磁盘上,当然这回导致额外的磁盘I/O以及垃圾回收的开销。
         * 混洗还会再磁盘上生成很多临时文件。以Spark-1.3来说,这些临时文件会一直保留到其对应的RDD被垃圾回收才删除。
         * 之所以这样做,是因为如果血统信息需要重新计算的时候,这些混洗文件可以不必重新生成。
         * 如果程序持续引用这些RDD或者垃圾回收启动频率较低,那么这些垃圾回收可能需要等较长的一段时间。
         * 这就意味着,长时间运行的Spark作业可能会消耗大量的磁盘。Spark的临时存储目录,是由spark.local.dir 配置参数指定的。
         *
         * 混洗行为可以由一系列配置参数来调优。参考Spark配置指南中”混洗行为”这一小节
         */

        /*
         * Spark的一项关键能力就是它可以持久化(或者缓存)数据集在内存中,从而跨操作复用这些数据集。
         * 如果你持久化了一个RDD,那么每个节点上都会存储该RDD的一些分区,这些分区是由对应的节点计算出来并保持在内存中,
         * 后续可以在其他施加在该RDD上的action算子中复用(或者从这些数据集派生新的RDD)。
         * 这使得后续动作的速度提高很多(通常高于10倍)。因此,缓存对于迭代算法和快速交互式分析是一个很关键的工具。
         * 你可以用persist() 或者 cache() 来标记一下需要持久化的RDD。
         * 等到该RDD首次被施加action算子的时候,其对应的数据分区就会被保留在内存里。
         * 同时,Spark的缓存具备一定的容错性 – 如果RDD的任何一个分区丢失了,Spark将自动根据其原来的血统信息重新计算这个分区。
         *
         * 另外,每个持久化的RDD可以使用不同的存储级别,比如,你可以把RDD保存在磁盘上,或者以java序列化对象保存到内存里(为了省空间),
         * 或者跨节点多副本,或者使用 Tachyon 存到虚拟机以外的内存里。这些存储级别都可以由persist()的参数StorageLevel对象来控制。
         * cache()方法本身就是一个使用默认存储级别做持久化的快捷方式,默认存储级别是StorageLevel.MEMORY_ONLY(以Java序列化方式存到内存里)。
         *
         * 注意:在Python中存储的对象总是会使用 Pickle 做序列化,所以这时是否选择一个序列化级别已经无关紧要了。
         *
         * Spark会自动持久化一些混洗操作(如:reduceByKey)的中间数据,即便用户根本没有调用persist。
         * 这么做是为了避免一旦有一个节点在混洗过程中失败,就要重算整个输入数据。当然,我们还是建议对需要重复使用的RDD调用其persist算子。
         *
         * Spark的存储级别主要可于在内存使用和CPU占用之间做一些权衡。建议根据以下步骤来选择一个合适的存储级别:
         *  • 如果RDD能使用默认存储级别(MEMORY_ONLY),那就尽量使用默认级别。这是CPU效率最高的方式,所有RDD算子都能以最快的速度运行。
         *  • 如果步骤1的答案是否(不适用默认级别),那么可以尝试MEMORY_ONLY_SER级别,并选择一个高效的序列化协议(
         *    selecting a fast serialization library),这回大大节省数据对象的存储空间,同时速度也还不错。
         *  • 尽量不要把数据吐到磁盘上,除非:1.你的数据集重新计算的代价很大;
         *    2.你的数据集是从一个很大的数据源中过滤得到的结果。否则的话,重算一个分区的速度很可能和从磁盘上读取差不多。
         *  • 如果需要支持容错,可以考虑使用带副本的存储级别(例如:用Spark来服务web请求)。
         *    所有的存储级别都能够以重算丢失数据的方式来提供容错性,但是带副本的存储级别可以让你的应用持续的运行,而不必等待重算丢失的分区。
         *  • 在一些需要大量内存或者并行多个应用的场景下 ,实验性的OFF_HEAP会有以下几个优势 :
         *       – 这个级别下,可以允许多个执行器共享同一个Tachyon中内存池。
         *       – 可以有效地减少垃圾回收的开销。
         *       – 即使单个执行器挂了,缓存数据也不会丢失。
         *
         * 删除数据
         *     Spark能够自动监控各个节点上缓存使用率,并且以LRU(最近经常使用)的方式将老数据逐出内存。
         *     如果你更喜欢手动控制的话,可以用RDD.unpersist() 方法来删除无用的缓存。
         */

        val broadcastVar = sc.broadcast(Array(1, 2, 3))
        // org.apache.spark.broadcast.Broadcast[Array[Int]]= Broadcast(0)
        println(broadcastVar.value.mkString("Array(", ", ", ")"))

        /**
         * 一般而言,当我们给Spark算子(如 map 或 reduce)传递一个函数时,这些函数将会在远程的集群节点上运行,
         * 并且这些函数所引用的变量都是各个节点上的独立副本。
         * 这些变量都会以副本的形式复制到各个机器节点上,如果更新这些变量副本的话,这些更新并不会传回到驱动器(driver)程序。
         * 通常来说,支持跨任务的可读写共享变量是比较低效的。不过,Spark还是提供了两种比较通用的共享变量:广播变量和累加器。
         *
         * 广播变量提供了一种只读的共享变量,它是在每个机器节点上保存一个缓存,而不是每个任务保存一份副本。
         * 通常可以用来在每个节点上保存一个较大的输入数据集,这要比常规的变量副本更高效(一般的变量是每个任务一个副本,一个节点上可能有多个任务)。
         * Spark还会尝试使用高效的广播算法来分发广播变量,以减少通信开销。
         * Spark的操作有时会有多个阶段(stage),不同阶段之间的分割线就是混洗操作。
         * Spark会自动广播各个阶段用到的公共数据。
         * 这些方式广播的数据都是序列化过的,并且在运行各个任务前需要反序列化。
         * 这也意味着,显示地创建广播变量,只有在跨多个阶段(stage)的任务需要同样的数据
         * 或者 缓存数据的序列化和反序列化格式很重要的情况下 才是必须的。
         * 广播变量可以通过一个变量v来创建,只需调用 SparkContext.broadcast(v)即可。
         * 这个广播变量是对变量v的一个包装,要访问其值,可以调用广播变量的 value 方法。
         *
         * 广播变量创建之后,集群中任何函数都不应该再使用原始变量v,这样才能保证v不会被多次复制到同一个节点上。
         * 另外,对象v在广播后不应该再被更新,这样才能保证所有节点上拿到同样的值
         * (例如,更新后,广播变量又被同步到另一新节点,新节点有可能得到的值和其他节点不一样)
         */
        val accum = sc.longAccumulator("lulu")  // 在 spark 的 2.4 版本 累加器和 3.0 版本的差异比较大
        distData.foreach(x => accum.add(x))
        println(accum.value)

        val myVectorAccum = new VectorAccumulatorV2
        sc.register(myVectorAccum, "MyVectorAcc1")

        /**
         * 累加器是一种只支持满足结合律的“累加”操作的变量,因此它可以很高效地支持并行计算。
         * 利用累加器可以实现计数(类似MapReduce中的计数器)或者求和。
         * Spark原生支持了数字类型的累加器,开发者也可以自定义新的累加器。
         * 如果创建累加器的时候给了一个名字,那么这个名字会展示在Spark UI上,这对于了解程序运行处于哪个阶段非常有帮助
         * (注意:Python尚不支持该功能)。
         * 创捷累加器时需要赋一个初始值v,调用 SparkContext.accumulator(v) 可以创建一个累加器。
         * 后续集群中运行的任务可以使用 add 方法 或者 += 操作符 (仅Scala和Python支持)来进行累加操作。
         * 不过,任务本身并不能读取累加器的值,只有驱动器程序可以用 value 方法访问累加器的值。
         *
         * 对于在action算子中更新的累加器,Spark保证每个任务对累加器的更新只会被应用一次, 例如,某些任务如果重启过,则不会再次更新累加器。
         * 而如果在transformation算子中更新累加器,那么用户需要注意,一旦某个任务因为失败被重新执行,那么其对累加器的更新可能会实施多次。
         * 累加器并不会改变Spark懒惰求值的运算模型 。 如果在RDD算子中更新累加器 ,那么其值只会在RDD做action算子计算的时候被更新一次。
         * 因此,在transformation算子(如:map)中更新累加器,其值并不能保证一定被更新。以下代码片段说明了这一特性:
         * val accum = sc.longAccumulator(0) data.map { x => accum += x; x }
         * 这里,accum任然是0,因为没有action算子,所以map也不会进行实际的计算
         */
        sc.stop()
    }
}

class SparkRDD {
    /**
     * 1- Exception in thread "main" java.io.IOException: Can't get Master Kerberos principal for use as renewer
     * 2- java.lang.ClassNotFoundException: WordCount
     * 3- Failed on local exception: java.io.IOException: Broken pipe
     * 4- SIMPLE authentication is not enabled.  Available:[TOKEN, KERBEROS]
     */
    def main(args: Array[String]): Unit = {
        // val hadoop1Home = sys.env.get("TEST")
        // val hadoop2Home = System.getenv("TEST")
        // sys.env.toSet("LD_LIBRARY_PATH", hadoop2Home + "/lib/native/")
        // System.setProperty("LD_LIBRARY_PATH", hadoop1Home + "/lib/native/")
        // System.setProperty("HADOOP_CONF_DIR", "/home/holyzing/snap/apache/hadoop-2.7.7/etc/hadoop/")
        // System.setProperty("HADOOP_CLASSPATH", "/home/holyzing/snap/apache/hadoop-2.7.7/etc/hadoop/")
        val classLoader = Thread.currentThread.getContextClassLoader
        val urlclassLoader = classLoader.asInstanceOf[URLClassLoader]
        val urls = urlclassLoader.getURLs
        for (url <- urls) {
            if (url.getPath.contains("target")){
                println("------------------>" + url)
            }
        }
        val master = "spark://spark-master:7077"  // local
        val username = "houleilei"
        val hdfsHome = "/home/holyzing/"
        val hdfsPath = "hdfs://hadoop01.stor:8020"  // 50070 web 管理端口  8020 rpc调用
        val keytabPath = "/home/holyzing/xtalpi/My/_03_Scala/Scala/xtalpi/src/resources/houleilei.client.keytab"

        val conf = new SparkConf().setJars(Array[String]("/home/holyzing/xtalpi/My/_03_Scala/Scala/xtalpi/target/holy-0.0.1-SNAPSHOT.jar"))

        val spark = SparkSession.builder()
            .config(conf)
            .config("spark.submit.deployMode", "client")
            .config("spark.cores.max", 4)
            .config("spark.executor.cores", 2)
            .config("spark.driver.memory", "1g")
            .config("spark.executor.memory", "1g")
            .config("spark.default.parallelism", "8")
            .appName("FisrtStep")
            .master(master).getOrCreate()
        // .config("spark.driver.extraClassPath", "/home/holyzing/snap/apache/hadoop-2.7.7/etc/")
        // .config("spark.yarn.principal", "houleilei@XTALPI-BJ.COM")
        // .config("spark.security.credentials.hive.enabled", "false")
        // .config("spark.security.credentials.hbase.enabled", "false")
        // .config("spark.yarn.keytab", keytabPath)

        // http://hadoop01.stor:50070   // hdfs://10.111.32.184:8020
        // RULE:[2:$1@$0]([nd]n@.*XTALPI-BJ.COM)s/.*/hadoop/
        // DEFAULT
        // dp/admin@GAI.COM

        // kerberos集群配置文件配置
        System.setProperty("java.security.krb5.conf", "/etc/krb5.conf")
        spark.sparkContext.hadoopConfiguration.set("hadoop.security.authentication", "kerberos")
        // spark.sparkContext.hadoopConfiguration.set("dfs.namenode.kerberos.principal.pattern", "*/*@XTALPI-BJ.COM")
        // spark.sparkContext.hadoopConfiguration.set("dfs.namenode.kerberos.principal", "nn/_HOST@XTALPI-BJ.COM")
        spark.sparkContext.hadoopConfiguration.addResource("core-site.xml")
        spark.sparkContext.hadoopConfiguration.addResource("hdfs-site.xml")
        spark.sparkContext.hadoopConfiguration.addResource("yarn-site.xml")

        // spark.sparkContext.hadoopConfiguration.addResource("modules/LogProcess/src/res/core-site.xml")
        // spark.sparkContext.hadoopConfiguration.addResource("modules/LogProcess/src/res/hdfs-site.xml")
        // spark.sparkContext.hadoopConfiguration.addResource("modules/LogProcess/src/res/yarn-site.xml")
        // spark.sparkContext.hadoopConfiguration.addResource("modules/LogProcess/src/res/hadoop-policy.xml")
        // spark.sparkContext.hadoopConfiguration.addResource("modules/LogProcess/src/res/kms-acls.xml")
        // spark.sparkContext.hadoopConfiguration.addResource("modules/LogProcess/src/res/mapred-site.xml")
        // spark.sparkContext.hadoopConfiguration.addResource("modules/LogProcess/src/res/ssl-client.xml")
        // spark.sparkContext.hadoopConfiguration.addResource("modules/LogProcess/src/res/ssl-server.xml")
        // spark.sparkContext.hadoopConfiguration.set("fs.defaultFS","hdfs://10.111.32.184:8020")

        val hadoopConf = spark.sparkContext.hadoopConfiguration
        UserGroupInformation.setConfiguration(hadoopConf)
        UserGroupInformation.loginUserFromKeytab(username, keytabPath)
        val rdd = spark.sparkContext.textFile(hdfsPath + hdfsHome + "test.txt")
        rdd.foreach(x => println(x))  // 当访问内容的时候 RDD 才会去加载数据
        rdd.saveAsTextFile(hdfsPath + hdfsHome + "output")
    }
}
