package com.holy.sparker

import org.apache.hadoop.security.UserGroupInformation
import org.apache.spark.SparkContext

import java.net.URLClassLoader

object HadoopUtils {

    val hdfsHome = "hdfs://hadoop01.stor:8020/home/holyzing/"

    def addHadoopConfigToSaprk(spark: SparkContext): Unit = {
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

        val username = "houleilei"
        val keytabPath = "/home/holyzing/xtalpi/My/_03_Scala/Scala/xtalpi/src/resources/houleilei.client.keytab"

        // http://hadoop01.stor:50070   // hdfs://10.111.32.184:8020
        // RULE:[2:$1@$0]([nd]n@.*XTALPI-BJ.COM)s/.*/hadoop/
        // DEFAULT
        // dp/admin@GAI.COM

        // kerberos集群配置文件配置
        System.setProperty("java.security.krb5.conf", "/etc/krb5.conf")
        spark.hadoopConfiguration.set("hadoop.security.authentication", "kerberos")
        // spark.hadoopConfiguration.set("dfs.namenode.kerberos.principal.pattern", "*/*@XTALPI-BJ.COM")
        // spark.hadoopConfiguration.set("dfs.namenode.kerberos.principal", "nn/_HOST@XTALPI-BJ.COM")

        spark.hadoopConfiguration.addResource("core-site.xml")
        spark.hadoopConfiguration.addResource("hdfs-site.xml")
        spark.hadoopConfiguration.addResource("yarn-site.xml")

        // spark.hadoopConfiguration.addResource("modules/LogProcess/src/res/core-site.xml")
        // spark.hadoopConfiguration.addResource("modules/LogProcess/src/res/hdfs-site.xml")
        // spark.hadoopConfiguration.addResource("modules/LogProcess/src/res/yarn-site.xml")
        // spark.hadoopConfiguration.addResource("modules/LogProcess/src/res/hadoop-policy.xml")
        // spark.hadoopConfiguration.addResource("modules/LogProcess/src/res/kms-acls.xml")
        // spark.hadoopConfiguration.addResource("modules/LogProcess/src/res/mapred-site.xml")
        // spark.hadoopConfiguration.addResource("modules/LogProcess/src/res/ssl-client.xml")
        // spark.hadoopConfiguration.addResource("modules/LogProcess/src/res/ssl-server.xml")
        // spark.hadoopConfiguration.set("fs.defaultFS","hdfs://10.111.32.184:8020")

        UserGroupInformation.setConfiguration(spark.hadoopConfiguration)
        UserGroupInformation.loginUserFromKeytab(username, keytabPath)

        // 50070 web 管理端口  8020 rpc调用
    }
}
