# -*- encoding: utf-8 -*-

from krbcontext import krbContext
# from hdfs.client import InsecureClient
from hdfs.ext.kerberos import KerberosClient


def hdfs_connect_demo():
    with krbContext(using_keytab=True, principal='houleilei@XTALPI-BJ.COM', keytab_file='./houleilei.client.keytab'):
        client = KerberosClient('http://hadoop01.stor:50070', hostname_override='hadoop01.stor')
        # client = InsecureClient('http://hadoop01.stor:50070', user='houleilei')
        result = client.list('/home/')
        print(type(result), result)


if __name__ == '__main__':
    hdfs_connect_demo()
