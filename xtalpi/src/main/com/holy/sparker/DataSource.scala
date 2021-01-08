package com.holy.sparker

import org.apache.spark.sql.SparkSession

object DataSource {
    def main(args: Array[String]): Unit = {

        val tempPath = SparkDataset.workHome + "/tmp"
        val peopleCsvPath = SparkDataset.sparkHome + "/examples/src/main/resources/people.csv"
        val peopleJsonPath = SparkDataset.sparkHome +  "/examples/src/main/resources/people.json"
        val usersParquetPath = SparkDataset.sparkHome + "/examples/src/main/resources/users.parquet"
        val spark = SparkSession.builder().appName("SparkDataSource").master("local[*]").getOrCreate()
        // Encoders for most common types are automatically provided by importing spark.implicits._

        // Encoders for most common types are automatically provided by importing spark.implicits._
        import spark.implicits._

        def parquetTest(): Unit ={
            val spark: SparkSession = spark
            // NOTE parquet
            val usersDF = spark.read.load(usersParquetPath)
            println(usersDF.columns.mkString("Array(", ", ", ")"))
            val usersDF2 =  spark.read.parquet(usersParquetPath)
            usersDF2.write.parquet(tempPath + "/parquet.test")
            usersDF.select("name", "favorite_color").write.save(tempPath + "/parquet.namesAndFavColors")
        }

        /**
         * DataFrame 分区 默认按字段顺序分区 (Cassandra 列式存储), 比如 先 gender 后 country
         * 读取的时候会抽取分区信息,返回 DataFrame 的表结构
         */
        def jsonTest(){
            // NOTE json
            val peopleDF = spark.read.format("json").load(peopleJsonPath)
            peopleDF.select("name", "age").write.format("parquet").save(tempPath + "/parquet.json")
            peopleDF.write.bucketBy(42, "name")
                .sortBy("age").saveAsTable("people_bucketed")
        }

        def csvTest(): Unit ={
            // NOTE CSV
            // val df = spark.read.option("header","true").option("inferSchema","true").csv(hdfsPath)
            // df.show()
            // df.write.option("header","true").mode("overwrite").csv("hdfs://10.111.32.184:8020/user/dp/demo")
            val peopleDFCsv = spark.read.format("csv")
                .option("sep", ";")
                .option("inferSchema", "true")
                .option("header", "true")
                .load(peopleCsvPath )
        }

        def orcTest(): Unit ={
            // NOTE ORC
            // For Parquet, there exists parquet.enable.dictionary 默认为true，
            // 是否启用dictionary编码；默认大小与page.size相同，为1M。dictionary创建时会占用较多的内存。
            val usersDF = spark.read.load(usersParquetPath)
            usersDF.write.format("orc")
                .option("orc.bloom.filter.columns", "favorite_color")
                .option("orc.dictionary.key.threshold", "1.0")
                .option("orc.column.encoding.direct", "name")
                .save(tempPath +  "users_with_options.orc")
        }

        //  Notice that an existing Hive deployment is not necessary to use this feature
        //  Spark will create a default local Hive metastore (using Derby) for you
        //  persistent datasource tables have per-partition metadata stored in the Hive metastore

        // Setting hive.metastore.warehouse.dir ('null') to the value of spark.sql.warehouse.dir
        // Not allowing to set spark.sql.warehouse.dir or hive.metastore.warehouse.dir in SparkSession's options,
        // it should be set statically for cross-session usages

        def partion(): Unit ={
            val usersDF = spark.sql("SELECT * FROM parquet.`"+ usersParquetPath +"`")
            // usersDF.write.mode(SaveMode.Ignore).save(tempPath + "/parquet.saveMode")

            usersDF.foreach(x => println(x))
            // [Alyssa,null,WrappedArray(3, 9, 15, 20)] [Ben,red,WrappedArray()]

            // usersDF.write.mode("overwrite").partitionBy("favorite_color")
            //     .format("parquet").save(tempPath + "/namesPartByColor.parquet")

            //  data 按column 分区后,会为每一个分区持久化元信息,这为更多 类 SQL 的 DDL 操作 成为现实

            // It is possible to use both partitioning and bucketing for a single table:
            usersDF.write.partitionBy("favorite_color")
                .bucketBy(42, "name").saveAsTable("users_partitioned_bucketed")
            // partitionBy creates a directory structure as described in the Partition Discovery section.
            // Thus, it has limited applicability to columns with high cardinality.
            // In contrast bucketBy distributes data across a fixed number of buckets
            // and can be used when the number of unique values is unbounded.
        }

        // ETL 经过抽取（extract）、转换（transform）、加载（load）

        // options | configurations: parquet, orc, avro, json, csv, tex
        def genericFileDataResourceOption(): Unit ={
            // dir1/file3.json is corrupt from parquet's view
            spark.sql("set spark.sql.files.ignoreCorruptFiles=true")
            // after Construct the Dataframe, read the missing file
            spark.sql("set spark.sql.files.ignoreMissingFiles=true")

            val testCorruptDF = spark.read.parquet(
                SparkDataset.sparkHome + "examples/src/main/resources/dir1/",
                SparkDataset.sparkHome + "examples/src/main/resources/dir1/dir2/")
            testCorruptDF.show()

            //  The syntax follows org.apache.hadoop.fs.GlobFilter.
            //  It does not change the behavior of partition discovery
            val testGlobFilterDF = spark.read.format("parquet")
                .option("pathGlobFilter", "*.parquet") // json file should be filtered out
                .load(SparkDataset.sparkHome + "examples/src/main/resources/dir1")
            testGlobFilterDF.show()

            // it disables partition inferring.
            // If data source explicitly specifies the partitionSpec when recursiveFileLookup is true,
            // exception will be thrown
            val recursiveLoadedDF = spark.read.format("parquet")
                .option("recursiveFileLookup", "true")
                .load(SparkDataset.sparkHome + "examples/src/main/resources/dir1")
            recursiveLoadedDF.show()

            /**
             * spark 设置 metastore_db(derby) 可通过 spark.driver.extraJavaOptions -Dderby.system.home=/tmp/derby 配置位置
             */
        }
    }



}
