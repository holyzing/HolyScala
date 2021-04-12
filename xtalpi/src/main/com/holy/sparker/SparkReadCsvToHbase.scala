package com.holy.sparker

import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase.client.{ConnectionFactory, Table}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.{KeyValue, TableName}
import org.apache.hadoop.hbase.mapreduce.{HFileOutputFormat2, LoadIncrementalHFiles}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.storage.StorageLevel

import scala.collection.mutable.ArrayBuffer



object SparkReadCsvToHbase {
    Logger.getLogger("org").setLevel(Level.INFO)
    def main(args: Array[String]): Unit = {
        val tableName = "houleilei:test"
        val readPath = "hdfs://hadoop01/home/holyzing/nohead_batchjob_info.csv"
        val filePath = "hdfs://hadoop01/home/holyzing/"
        val columnf ="t"

        val ss = SparkSession.builder().getOrCreate()
        val hconf = HadoopUtils.hbaseConfig(null, tableName)

        val conn = ConnectionFactory.createConnection(hconf)
        val regionLocator = conn.getRegionLocator(TableName.valueOf(tableName))
        val table: Table = conn.getTable(TableName.valueOf(tableName))

        val job = Job.getInstance(hconf)
        job.setMapOutputKeyClass(classOf[ImmutableBytesWritable])
        job.setMapOutputValueClass(classOf[KeyValue])
        HFileOutputFormat2.configureIncrementalLoadMap(job, table)

        val df: DataFrame = ss.read.csv(readPath)
        //	val df: DataFrame = ss.read.parquet(readPath)
        val rdd1 = df.rdd.flatMap(row => {
            val fields: Array[StructField] = row.schema.fields
            val values = ArrayBuffer[(String, (String, String, String))]()
            val rowkey = row.getAs(0).toString
            fields.foreach(col => {
                values.append((rowkey, (columnf,col.name,row.getAs(col.name))))
            })
            values
        }).persist(StorageLevel.MEMORY_AND_DISK_SER)

        rdd1.sortBy(x => (x._1, x._2._1, x._2._2))
            .map(rdd => {
                val rowKey = Bytes.toBytes(rdd._1)
                val family = Bytes.toBytes(rdd._2._1)
                val colum = Bytes.toBytes(rdd._2._2)
                val value = Bytes.toBytes(rdd._2._3)
                (new ImmutableBytesWritable(rowKey), new KeyValue(rowKey, family, colum, value))
            }).saveAsNewAPIHadoopFile(filePath,
            classOf[ImmutableBytesWritable],
            classOf[KeyValue],
            classOf[HFileOutputFormat2],
            hconf)

        val load = new LoadIncrementalHFiles(hconf)
        load.doBulkLoad(new Path(filePath), conn.getAdmin, table, regionLocator)

        table.close()
        conn.close()
        ss.close()
    }
}
