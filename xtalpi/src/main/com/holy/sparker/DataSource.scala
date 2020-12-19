package com.holy.sparker

import org.apache.spark.sql.{SaveMode, SparkSession}

object DataSource {
    def main(args: Array[String]): Unit = {
        /**
         * Spark 的数据保存格式
         * 1）如果说 HDFS 是大数据时代分布式文件系统首选标准，那么 parquet 则是整个大数据时代文件存储格式实时首选标准。
         * 2）速度更快：从使用 spark sql 操作普通文件 CSV 和 parquet 文件速度对比上看，
         *    绝大多数情况会比使用 csv等普通文件速度提升10倍左右，
         *    在一些普通文件系统无法在 spark上成功运行的情况下，使用 parquet 很多时候可以成功运行。
         * 3）parquet 的压缩技术非常稳定出色，在 spark sql 中对压缩技术的处理可能无法正常的完成工作
         *   （例如会导致 lost task，lost executor）但是此时如果使用 parquet 就可以正常的完成。
         * 4）极大的减少磁盘 I/O，通常情况下能够减少75%的存储空间，由此可以极大的减少 spark sql 处理数据的时候的数据输入内容，
         *    尤其是在 spark1.6x 之后版本的下推过滤器在一些情况下可以极大的减少磁盘的 I/O 和内存的占用（下推过滤器）。
         * 5）从 spark 1.6x 开始的 parquet 方式极大的提升了扫描的吞吐量，极大提高了数据的查找速度 spark1.6 和 spark1.5x 相比而言，
         *    提升了大约1倍的速度，在 spark1.6X 中，操作 parquet 的时候 cpu 也进行了极大的优化，有效的降低了cpu 消耗。
         * 6）采用 parquet 可以极大的优化 spark 的调度和执行。
         *    测试 spark 如果用 parquet 可以有效的减少 stage 的执行消耗，同时可以优化执行路径。
         *
         *  Parquet是一种列式存储格式，很多种处理引擎都支持这种存储格式，也是sparksql的默认存储格式。
         *  Spark SQL支持灵活的读和写Parquet文件，并且对parquet文件的schema可以自动解析。
         *  当Spark SQL需要写成Parquet文件时，处于兼容的原因所有的列都被自动转化为了nullable。
         */

        // NOTE parquet
        val parquetPath = SparkDataset.sparkHome + "/examples/src/main/resources/users.parquet"
        val tempPath = "/home/holyzing/xtalpi/My/_03_Scala/Scala/xtalpi/tmp"
        val spark = SparkSession.builder().appName("SparkDataSource").master("local[*]").getOrCreate()
        val usersDF = spark.read.load(parquetPath)
        println(usersDF.columns.mkString("Array(", ", ", ")"))
        val usersDF2 =  spark.read.parquet(parquetPath)
        usersDF2.write.parquet(tempPath + "/parquet.test")
        usersDF.select("name", "favorite_color").write.save(tempPath + "/parquet.namesAndFavColors")

        /**
         * DataFrame 分区 默认按字段顺序分区 (Cassandra 列式存储), 比如 先 gender 后 country
         * 读取的时候会抽取分区信息,返回 DataFrame 的表结构
         */

        // NOTE json
        val jsonPath = SparkDataset.sparkHome +  "/examples/src/main/resources/people.json"
        val peopleDF = spark.read.format("json").load(jsonPath)
        peopleDF.select("name", "age").write.format("parquet").save("parquet.json")

        // NOTE CSV
        val csvPath = SparkDataset.sparkHome + "/examples/src/main/resources/people.csv"
        val peopleDFCsv = spark.read.format("csv")
            .option("sep", ";")
            .option("inferSchema", "true")
            .option("header", "true")
            .load(csvPath )

        // NOTE ORC  // For Parquet, there exists parquet.enable.dictionary
        usersDF.write.format("orc")
            .option("orc.bloom.filter.columns", "favorite_color")
            .option("orc.dictionary.key.threshold", "1.0")
            .option("orc.column.encoding.direct", "name")
            .save("users_with_options.orc")

        val sqlDF = spark.sql("SELECT * FROM parquet.`examples/src/main/resources/users.parquet`")

        sqlDF.write.mode(SaveMode.Ignore).save(tempPath + "parquet.saveMode")
        sqlDF.write.mode("overwrite").save(tempPath + "parquet.saveMode")

        //  Notice that an existing Hive deployment is not necessary to use this feature
        //  Spark will create a default local Hive metastore (using Derby) for you
        //  persistent datasource tables have per-partition metadata stored in the Hive metastore
        //  data 按column 分区后,会为每一个分区持久化元信息,这为更多 类 SQL 的 DDL 操作 成为现实

        peopleDF.write.bucketBy(42, "name").sortBy("age").saveAsTable("people_bucketed")
        usersDF.write.partitionBy("favorite_color").format("parquet").save("namesPartByColor.parquet")
        // It is possible to use both partitioning and bucketing for a single table:
        usersDF.write.partitionBy("favorite_color").bucketBy(42, "name").saveAsTable("users_partitioned_bucketed")
        // partitionBy creates a directory structure as described in the Partition Discovery section.
        // Thus, it has limited applicability to columns with high cardinality.
        // In contrast bucketBy distributes data across a fixed number of buckets
        // and can be used when the number of unique values is unbounded.

        /**
         * spark 读取 metastore_db(derby) 可通过 spark.driver.extraJavaOptions -Dderby.system.home=/tmp/derby 配置位置
         */
    }

}
