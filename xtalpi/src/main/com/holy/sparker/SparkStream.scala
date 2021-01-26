package com.holy.sparker

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.scheduler.StreamingListener
import org.junit.Test
// import org.apache.spark.streaming.StreamingContext._ // not necessary since Spark 1.3


object SparkStream {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
        val ssc = new StreamingContext(conf, Seconds(1))

        ssc.addStreamingListener(new StreamingListener {

        })
        // nc -lk 9999  || input
        // Input Dstream (except file Stream )
        val lines = ssc.socketTextStream("localhost", 9999, StorageLevel.MEMORY_AND_DISK_SER)
        val words = lines.flatMap(_.split(" "))                                                        // Dstream
        val pairs = words.map(word => (word, 1))
        val wordCounts = pairs.reduceByKey(_ + _)
        print(ssc.sparkContext.version)

        // Print the first ten elements of each RDD generated in this DStream to the console
        wordCounts.print()
        ssc.start()             // Start the computation
        ssc.awaitTermination()  // Wait for the computation to terminate
        // ssc.awaitTerminationOrTimeout(5)
        ssc.stop()
    }


}

class SparkStream {
    @Test
    def kafkaTest(): Unit ={
        val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
        val ssc = new StreamingContext(new SparkContext(conf), Seconds(1))
        // ssc.fileStream()
        val lines = ssc.textFileStream("/home/holyzing/xtalpi/My/_03_Scala/Scala/xtalpi/tmp/streamDir")
        lines.print()
        ssc.start()
        ssc.awaitTermination()
        ssc.stop()


        /**
        After a context is defined, you have to do the following.
            1- Define the input sources by creating input DStreams.
            2- Define the streaming computations by applying transformation and output operations to DStreams.
            3- Start receiving data and processing it using streamingContext.start().
            4- Wait for the processing to be stopped (manually or due to any error)
               using streamingContext.awaitTermination().
            5- The processing can be manually stopped using streamingContext.stop().
        Points to remember:
            1- Once a context has been started, no new streaming computations can be set up or added to it.
            2- Once a context has been stopped, it cannot be restarted.
            3- Only one StreamingContext can be active in a JVM at the same time.
            4- stop() on StreamingContext also stops the SparkContext. To stop only the StreamingContext,
               set the optional parameter of stop() called stopSparkContext to false.
            5- A SparkContext can be re-used to create multiple StreamingContexts,
               as long as the previous StreamingContext is stopped (without stopping the SparkContext)
               before the next StreamingContext is created.
        */

        // 1- For ingesting data from sources like Kafka and Kinesis
        //    that are not present in the Spark Streaming core API .
        // 2- The batch interval must be set based on the latency
        //    requirements of your application and available cluster resources
        // 3- Each RDD in a DStream contains data from a certain interval, as shown in the following figure.
        // 4- The flatMap operation is applied on each RDD in the lines DStream
        //    to generate the RDDs of the words DStream
        // 5- These underlying RDD transformations are computed by the Spark engine.
        //    The DStream operations hide most of these details and provide
        //    the developer with a higher-level API for convenience

        /*
        1-  Every input DStream (except file stream) is associated with a Receiver (Scala doc, Java doc) object
            which receives the data from a source and stores it in Spark’s memory for processing.

        2-  Spark Streaming provides two categories of built-in streaming sources.
            Basic sources: Sources directly available in the StreamingContext API.
                           Examples: file systems, and socket connections.
            Advanced sources: Sources like Kafka, Kinesis, etc. are available through extra utility classes.
                              These require linking against extra dependencies as discussed in the linking section.
        3-  receive multiple streams of data in parallel,
            a Spark Streaming application needs to be allocated enough cores
        4-  when running locally, always use “local[n]” as the master URL, where n > number of receivers to run .
            The number of cores allocated to the Spark Streaming application must be more than the number of receivers .
         */

    }
}
