
package com.holy.sparker

import java.net.URLClassLoader

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.hadoop.security.UserGroupInformation

/**
 * 1- Exception in thread "main" java.io.IOException: Can't get Master Kerberos principal for use as renewer
 * 2- java.lang.ClassNotFoundException: WordCount
 * 3- Failed on local exception: java.io.IOException: Broken pipe
 * 4- SIMPLE authentication is not enabled.  Available:[TOKEN, KERBEROS]
 */
object WordCount {
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

//      val df = spark.read.option("header","true").option("inferSchema","true").csv(hdfsPath)
//      df.show()
//      df.write.option("header","true").mode("overwrite").csv("hdfs://10.111.32.184:8020/user/dp/demo")
