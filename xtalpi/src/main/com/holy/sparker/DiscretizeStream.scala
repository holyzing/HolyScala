package com.holy.sparker

import org.apache.spark._
import org.apache.spark.streaming._
// import org.apache.spark.streaming.StreamingContext._ // not necessary since Spark 1.3


object DiscretizeStream {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
        val ssc = new StreamingContext(conf, Seconds(1))
        val lines = ssc.socketTextStream("localhost", 12001)
        val words = lines.flatMap(_.split(" "))
        val pairs = words.map(word => (word, 1))
        val wordCounts = pairs.reduceByKey(_ + _)

        // Print the first ten elements of each RDD generated in this DStream to the console
        wordCounts.print()
        ssc.start()             // Start the computation
        ssc.awaitTermination()  // Wait for the computation to terminate
    }

}
