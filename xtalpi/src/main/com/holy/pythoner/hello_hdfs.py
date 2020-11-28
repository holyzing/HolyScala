# -*- encoding: utf-8 -*-
from __future__ import print_function

import sys
from operator import add
from pyspark.sql import SparkSession
from pyspark import SparkConf, SparkContext

from krbcontext import krbContext
# from hdfs.client import InsecureClient
from hdfs.ext.kerberos import KerberosClient


def hdfs_connect_demo():
    with krbContext(using_keytab=True, principal='houleilei@XTALPI-BJ.COM', keytab_file='../../resources/houleilei.client.keytab'):
        client = KerberosClient('http://hadoop01.stor:50070', hostname_override='hadoop01.stor')
        # client = InsecureClient('http://hadoop01.stor:50070', user='houleilei')
        result = client.list('/home/holyzing/')
        print(type(result), result)


def pyspark_api():
    text_file_path = "/home/holyzing/Desktop/marvin-prod-20201125.db"
    sc = SparkConf()
    sc.setMaster("local[*]")
    sc.setAppName("PysaprkApi")
    spark = SparkSession.builder(sc).getOrCreate()
    text_file = spark.read.text(text_file_path)
    print(text_file.first(), text_file.count())
    line_with_insert = text_file.filter(text_file.value.contains("insert"))
    print(line_with_insert.count())


if __name__ == '__main__':
    # if len(sys.argv) != 2:
    #     print("Usage: wordcount <file>", file=sys.stderr)
    #     sys.exit(-1)
    #
    # # spark = SparkSession.builder.appName("PythonWordCount").getOrCreate(
    # sc = SparkConf().setAppName("wd_demo1").setMaster("spark://spark-master:7077")
    # sc.set("spark.cores.max", 8)
    # sc.set("spark.executor.cores", 2)
    # sc.set("spark.executor.memory", "1g")
    # sc.set("spark.executor.pyspark.memory", "50m")
    #
    # spark = SparkContext(conf=sc)
    #
    # lines = spark.read.text(sys.argv[1]).rdd.map(lambda r: r[0])
    # counts = lines.flatMap(lambda x: x.split(' ')).map(lambda x: (x, 1)).reduceByKey(add)
    # output = counts.collect()
    # for (word, count) in output:
    #     print("%s: %i" % (word, count))
    # spark.stop()

    hdfs_connect_demo()
# 基于已经存在的 spark 和 hadoop 集群构建 hive

