package com.holy.sparker

import org.apache.hadoop.security.UserGroupInformation
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.HBaseAdmin
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec.InputType

object HadoopUtils {
    val os: String = System.getProperty("os.name")
    var workHome: String = _
    var sparkHome: String = _
    var keytabPath: String = _
    var krb5ConfPath: String = _
    val username = "houleilei"
    // IDE 推荐使用 _ 而不是 null, scala 中 的 _ 代表什么 ? println(_) 会报错

    if (os != null && os.toLowerCase().indexOf("linux")> -1){
        krb5ConfPath = "/etc/krb5.conf"
        workHome = "/home/holyzing/xtalpi/My/_03_Scala/Scala/xtalpi"
        keytabPath = workHome + "/src/resources/houleilei.client.keytab"
        sparkHome = "/home/holyzing/snap/apache/spark-3.0.1-bin-hadoop2.7"
    } else {
        workHome = "F:/mywork/Scala/xtalpi"
        sparkHome = "F:/apache/spark-3.0.1-bin-hadoop2.7"
        krb5ConfPath = workHome + "/src/resources/krb5.conf"
        keytabPath = workHome + "/src/resources/houleilei.client.keytab"
    }
    sparkHome = sys.env.getOrElse("SPARK_HOME", sparkHome)  // windows 下是生效的
    sparkHome = sparkHome.replace("\\", "/")

    val hdfsHome = "hdfs://hadoop01.stor:8020/home/holyzing/"

    def setProperties(): Unit ={
        System.setProperty("user.name", username)
        System.setProperty("HADOOP_USER_NAME", username)
        System.setProperty("java.security.krb5.conf", krb5ConfPath)
    }

    def hadoopConfig(config: Configuration): Unit = {
        // val hadoop1Home = sys.env.get("TEST")
        // val hadoop2Home = System.getenv("TEST")
        // sys.env.toSet("LD_LIBRARY_PATH", hadoop2Home + "/lib/native/")
        // System.setProperty("LD_LIBRARY_PATH", hadoop1Home + "/lib/native/")
        // System.setProperty("HADOOP_CONF_DIR", "/home/holyzing/snap/apache/hadoop-2.7.7/etc/hadoop/")
        // System.setProperty("HADOOP_CLASSPATH", "/home/holyzing/snap/apache/hadoop-2.7.7/etc/hadoop/")

        // val classLoader = Thread.currentThread.getContextClassLoader
        // val urlclassLoader = classLoader.asInstanceOf[URLClassLoader]
        // val urls = urlclassLoader.getURLs
        // for (url <- urls) {
        //     if (url.getPath.contains("target")){
        //         println("------------------>" + url)
        //     }
        // }

        // http://hadoop01.stor:50070   // hdfs://10.111.32.184:8020
        // RULE:[2:$1@$0]([nd]n@.*XTALPI-BJ.COM)s/.*/hadoop/
        // DEFAULT
        // dp/admin@GAI.COM

        setProperties()
        // kerberos集群配置文件配置
        config.set("hadoop.security.authentication", "kerberos")

        // config.set("dfs.namenode.kerberos.principal.pattern", "*/*@XTALPI-BJ.COM")
        // config.hadoopConfiguration.set("dfs.namenode.kerberos.principal", "nn/_HOST@XTALPI-BJ.COM")

        // NOTE Configuration 静态代码块会主动加载 classpath 下的配置文件
        // config.addResource("tmp/core-site.xml")
        // config.addResource("tmp/hdfs-site.xml")
        // config.addResource("tmp/yarn-site.xml")

        // config.addResource("modules/LogProcess/src/res/core-site.xml")
        // config.addResource("modules/LogProcess/src/res/hdfs-site.xml")
        // config.addResource("modules/LogProcess/src/res/yarn-site.xml")
        // config.addResource("modules/LogProcess/src/res/hadoop-policy.xml")
        // config.addResource("modules/LogProcess/src/res/kms-acls.xml")
        // config.addResource("modules/LogProcess/src/res/mapred-site.xml")
        // config.addResource("modules/LogProcess/src/res/ssl-client.xml")
        // config.addResource("modules/LogProcess/src/res/ssl-server.xml")
        // config.set("fs.defaultFS","hdfs://10.111.32.184:8020")

        UserGroupInformation.setConfiguration(config)
        UserGroupInformation.loginUserFromKeytab(username, keytabPath)

        // 50070 web 管理端口  8020 rpc调用
    }

    def hbaseConfig(inputTable: String, outputTable: String): Configuration ={
        val conf = HBaseConfiguration.create()
        conf.set("hbase.zookeeper.quorum", "hbase01.stor:2181,hbase02.stor:2181,hbase03.stor:2181")

        // conf.set("hbase.zookeeper.property.clientPort","2181")
        // TODO 当前classpath 不存在该类
        // conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        // 这个配置的作用就是：一个ip所对应的客户机，只能和zk服务器维持60个连接。
        // conf.set("hbase.zookeeper.property.maxClientCnxns", "60");
        // conf.addResource("hbase-site.xml")
        // !_.equals('$')

        if (inputTable != null){
            conf.set(TableInputFormat.INPUT_TABLE, inputTable) // "houleilei:jobinfos"
        }
        if (outputTable != null){
            conf.set("hbase.mapreduce.hfileoutputformat.table.name", outputTable)
            conf.setInt("hbase.mapreduce.bulkload.max.hfiles.perRegion.perFamily", 5000)
        }

        try {
            hadoopConfig(conf)
            HBaseAdmin.checkHBaseAvailable(conf)
        } catch {
            case ex: Exception =>
                println(ex)
                return null
        }

        conf
    }

}
