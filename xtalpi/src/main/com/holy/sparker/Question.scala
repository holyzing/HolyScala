package com.holy.sparker

object Question{

}

class Question {

}

/**

Application：Spark Application 的概念和 Hadoop 中的 MapReduce 类似，指的是用户编写的 Spark 应用程序，
             包含了一个 Driver 功能的代码和分布在集群中多个节点上运行的 Executor 代码；

Driver：Spark 中的 Driver 即运行上述 Application 的 main() 函数并且创建 SparkContext，
        其中创建 SparkContext 的目的是为了准备Spark应用程序的运行环境。
        在 Spark 中由 SparkContext 负责和 ClusterManager 通信，进行资源的申请、任务的分配和监控等；
        当 Executor 部分运行完毕后，Driver负责将SparkContext 关闭。通常用 SparkContext 代表 Driver；

Executor：Application运行在Worker 节点上的一个进程，该进程负责运行Task，并且负责将数据存在内存或者磁盘上，
         每个Application都有各自独立的一批Executor。
         其进程名称为CoarseGrainedExecutorBackend，类似于 Hadoop MapReduce 中的 YarnChild。
         一个 CoarseGrainedExecutorBackend 进程有且仅有一个 executor 对象，
         它负责将 Task 包装成 taskRunner，并从线程池中抽取出一个空闲线程运行 Task。
         每个 CoarseGrainedExecutorBackend 能并行运行 Task 的数量就取决于分配给它的 CPU 的个数

Cluster manager:  用于获取集群资源的外部服务(例如，k8s、Mesos、YARN)

Worker node: 运行任何Application 的节点，在k8s相当于node

Job：由多个任务组成的并行计算，这些任务在响应一个Spark操作时产生(如 save, collect)

Stage：每个作业被分成更小的任务集，称为阶段，这些阶段相互依赖(类似于MapReduce中的map和reduce阶段);

Task：送往executor的工作单元
 */

// ---------------------------------------------------------------------------------------------------------------------

/**
quota
metric

1-spark-shell 做了什么? Driver Worker Executor Master 以及 Application 是如何工作的 ?
2-Context UI 又是如何工作的 ?

子项目: SparkSQL、Spark Streaming、GraphX、MLib、SparkR
计算模型: MapReduce, 批处理、迭代算法、交互式查询、流处理
大一统的软件栈，各个组件关系密切并且可以相互调用
高效的DAG执行引擎，可以通过基于内存来高效处理数据流

spark-shell --total-executor-cores 8 --executor-memory 1G --executor-cores 2 --master spark://spark-master:7077

spark-submit --class com.holy.sparker.WordCount --total-executor-cores 4 --executor-memory 1G --executor-cores 2 --master spark://spark-master:7077 holy-0.0.1-SNAPSHOT.jar

spark standalong 模式的执行流程：

1- 通过配置 来确定 Application 运行所需的资源以及在哪个环境上运行
2- spark-submit 提交Application的配置信息 到 Master，完成注册。
3- Master 根据请求信息 进行Worker扫描，找到满足数量且满足要求的Worker，告知其启动符合配额的Executor
4- Executor 启动之后连接 Driver 完成到Driver信息的注册。
5- Driver根据注册的Executor信息开始进行Programer以及一些Application指定的依赖和资源文件到Worker的拷贝。
6- 拷贝完成之后 植入我们Application中的SparkContext 将发送Task 到Executor执行。
   task就是我们在写Application应用中指定的算子函数
7- Driver 和 Executor 在 Application 的整个生命周期中都会产生通信，保持二者之间的通信是关键的
   所以说尽可能让Driver以cluster 模式运行，保证其在集群网络里进行通信，由于网络通信造成整个集群的性能下降。
        Executor 到 Driver 的信息注册
        Executor 执行一个算子结束后通知 Driver 进行下一步的处理
        Driver 发送 Application Programer 及其依赖 到Executor（所在的Worker）
8-

SparkContext
SparkSession

疑问
1- 是由谁来发送 Application Programer 到谁 ？
2- 是由谁来触发在 Executor 中 执行 task
3- 新增worker 之后 master通知 Driver协调资源分配 ？
4- 统一的数据载入入口，如果是本地文件系统，则每一个worker载入的文件不一样呢？ 是怎么处理 ？

Hadoop的MapReduce计算框架，JobTracker和TaskTracker之间由于是通过heartbeat的方式来进行的通信和传递数据，
会导致非常慢的执行速度，而Spark具有出色而高效的Akka和Netty通信系统，通信效率极高。
 */

/**
RDD: resilient distribute dataset
弹性分布式数据集

像java中 的HashMap或者是List一样的数据集合的索引描述
一个RDD 对象中定义了一组分区，分区中定义了其所在的节点信息以及每个区中所映射的数据索引信息

 */

// ---------------------------------------------------------------------------------------------------------------------

/**
 * 1- cass class
 * 2- implicits
 * 3- _
 * 4- cass class 我什么要定义 在 main 函数外边, 否则会报出 value toDF is not a member of Seq[DataRow]
 *
 */
