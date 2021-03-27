# -*- encoding: utf-8 -*-
from __future__ import print_function

import re
import time
import pandas as pd

from operator import add
from pyspark import SparkConf
from krbcontext import krbContext
from pyspark.rdd import RDD
from pyspark.sql import SparkSession
from happybase_kerberos_patch import KerberosConnection



principal = ''
keytab = '*.client.keytab'
cm_table, cm_pre = "namespace:jobinfos", "infos"
cm_quorum = "hbase01.stor:2181,hbase02.stor:2181,hbase03.stor:2181"


def read_csv_to_hbase():
    """
    read_csv_to_hbase
    :return:
    """
    columns = ["id", "name", "category", "project", "cluster", "creator", "datasets", "result",
               "status", "start_time", "end_time", "image", "git_url", "git_branch", "git_commit",
               "command", "cpu", "gpu", "spot", "memory", "gpu_model", "relation_report"]
    df = pd.read_csv("/home/holyzing/Desktop/batchjob_info.csv", delimiter=",", header=None)
    df.columns = columns
    print(len(df.index))
    with krbContext(using_keytab=True, principal=principal, keytab_file=keytab):
        conn = KerberosConnection('hbase02.stor', protocol='compact', use_kerberos=True)
        table = conn.table(cm_table)
        # batch = table.batch()
        # batch.put()
        for index, row in df.iterrows():
            data = {f"infos:{column}": str(row[column]) for column in columns}
            data["infos:cpu"] = re.search(r"\d+", data["infos:cpu"]).group()
            data["infos:gpu"] = re.search(r"\d+", data["infos:gpu"]).group()
            data["infos:memory"] = re.search(r"\d+", data["infos:memory"]).group()
            data["infos:spot"] = re.search(r"true|false", data["infos:spot"]).group()
            data["infos:gpu_model"] = data["infos:gpu_model"].split(": ")[1][1:-3]
            table.put(str(index), data)
        conn.close()


def timestamp_to_datestr(ts, project):
    """
    将时间戳格式化为日期格式
    """
    if pd.isna(ts):
        return str(project)[2:-1]
    t = time.localtime(ts)
    datestr = time.strftime("%Y%m", t)
    return datestr


def pandas_group():
    """
    从hbase 中读取数据后，使用pandas进行聚合统计
    """
    with krbContext(using_keytab=True, principal=principal, keytab_file=keytab):
        conn = KerberosConnection('hbase02.stor', protocol='compact', use_kerberos=True)
        table = conn.table(cm_table)
        columns = ["project", "start_time", "end_time", "cpu", "gpu", "memory"]
        data = table.scan(columns=["infos:%s" % column for column in columns], filter=None)
        # TODO 强行使用 filters
        # "SingleColumnValueFilter('info','project',=,'binary:{0}')
        df = pd.DataFrame(data=[d[1] for d in data])
        print(df.columns, len(df.index))
        df.columns = [str(column).split(":")[1][:-1] for column in df.columns]
        df["start_time"] = df["start_time"].apply(float)
        df["end_time"] = df["end_time"].apply(float)
        df["cpu"] = df["cpu"].apply(int)
        df["gpu"] = df["gpu"].apply(int)
        df["memory"] = df["memory"].apply(int)

        data = []
        project_groups = df.groupby("project", as_index=True)
        for project, group in project_groups.groups.items():
            project_df = df.loc[group]
            month_groups = project_df.groupby(by=lambda i: timestamp_to_datestr(
                project_df.loc[i, "start_time"], project), as_index=True)
            df_sum = month_groups.sum()
            print(df_sum.index)
            for month, row in df_sum.iterrows():
                start_time = 0 if pd.isna(row["start_time"]) else row["start_time"]
                end_time = 0 if pd.isna(row["end_time"]) else row["end_time"]
                time_long = (end_time - start_time) / 3600
                time_long = 0 if time_long <= 0 else time_long
                data.append([str(project)[2:-1], month, time_long, row["gpu"] * time_long,
                             row["cpu"] * time_long, row["memory"]])
        data.sort(key=lambda x: (x[0], x[1]))
        columns = ["project", "month", "time", "cpu", "gpu", "memory"]
        df = pd.DataFrame(data=data, columns=columns)
        writer = pd.ExcelWriter("批任务核时月度统计.xls")
        df.to_excel(writer, sheet_name="batch_jobs_info", index=False)
        writer.save()
        writer.close()


def spark_hbase():
    sc = SparkConf().setAppName("sparkHbase").setMaster("local[2]")
    sc.set("spark.cores.max", 8)
    sc.set("spark.executor.cores", 2)
    sc.set("spark.executor.memory", "1g")
    sc.set("spark.executor.pyspark.memory", "50m")

    # cluster mode 下 executor 端应该也需要 Hbase 下的 lib
    # Hbase 1.4.13 下的 netty 包对于 spark 2.4 以上版本来说太旧
    # 各环境版本中各 jar 包 版本不一致的问题真令人头疼

    # client 模式下无效
    sc.set("spark.driver.extraClassPath", "/home/holyzing/snap/apache/extrajars/*")
    # sc.set("spark.executor.extraClassPath", "/home/holyzing/snap/apache/hbase-1.4.13/lib/*")
    # sc.set("jars", ["/home/holyzing/snap/apache/extrajars/spark-examples_2.10-1.1.1.jar,\
    #                 /home/holyzing/snap/apache/hbase-1.4.13/lib/hbase-server-1.4.13.jar,\
    #                 /home/holyzing/snap/apache/hbase-1.4.13/lib/hbase-common-1.4.13.jar,\
    #                 /home/holyzing/snap/apache/hbase-1.4.13/lib/hbase-client-1.4.13.jar"])
    spark = SparkSession.builder.config(conf=sc).enableHiveSupport().getOrCreate()
    # spark = SparkContext(conf=sc)

    yz_quorum = "hadoop01:2181,hadoop02:2181,hadoop03:2181"
    yz_table, yz_pre = "default:job", "info"

    columns = ["project", "start_time", "end_time", "cpu", "gpu"] # "memory"
    columns = [f"{yz_pre}:%s" % column for column in columns]

    hbase_conf = {
        "hbase.zookeeper.quorum": cm_quorum,
        "hbase.mapreduce.inputtable": cm_table,
        # "hbase.mapreduce.scan.row.start": '_',
        # "hbase.mapreduce.scan.row.stop": '_',
        # "hbase.mapreduce.scan.column.family": "info",
        # TODO columns 只能选取一列？ 缺省后也不是选取全部列，family 如何 配置 ？？？？
        # TODO 这么多配置项中似乎没有配置 过滤器的选项 ？？？？
        "hbase.mapreduce.scan.columns": "infos:project"  # " ".join(columns)
    }

    # 在 cluster mode 下，Executor 中应该也需要这两个 类
    keyConv = "org.apache.spark.examples.pythonconverters.ImmutableBytesWritableToStringConverter"
    valueConv = "org.apache.spark.examples.pythonconverters.HBaseResultToStringConverter"

    # help(spark.sparkContext.newAPIHadoopRDD)
    # TODO 无法连接，spark 无法将 认证信息带到 HBase
    with krbContext(using_keytab=True, principal='houleilei@XTALPI-BJ.COM', keytab_file=keytab):
        # 在 cluster mode 下，Executor 中应该也需要这两个 类，不是单单在 Driver 端需要
        hbase_rdd: RDD = spark.sparkContext.newAPIHadoopRDD(
            "org.apache.hadoop.hbase.mapreduce.TableInputFormat",
            "org.apache.hadoop.hbase.io.ImmutableBytesWritable",
            "org.apache.hadoop.hbase.client.Result",
            keyConverter=keyConv, valueConverter=valueConv, conf=hbase_conf)

    # hbase_rdd.collect()
    # help(hbase_rdd)
    fisrt_rdd = hbase_rdd.first()
    print(fisrt_rdd)
    print(hbase_rdd.count())
    # hbase_rdd.foreach(print)

    # 将 Hbase 数据映射为 Hive 的表 (性能会下降，速度会变慢)
    spark.stop()


if __name__ == '__main__':
    spark_hbase()
