
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


quota
metric

1-spark-shell 做了什么? Driver Worker Executor Master 以及 Application 是如何工作的 ?
2-Context UI 又是如何工作的 ?

子项目: SparkSQL、Spark Streaming、GraphX、MLib、SparkR
计算模型: MapReduce, 批处理、迭代算法、交互式查询、流处理
大一统的软件栈，各个组件关系密切并且可以相互调用
高效的DAG执行引擎，可以通过基于内存来高效处理数据流

spark-shell --total-executor-cores 8 --executor-memory 4G --executor-cores 2 --master spark://spark-master:7077