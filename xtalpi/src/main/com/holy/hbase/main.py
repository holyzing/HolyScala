import happybase
import datetime
import json


class HBase:
    def __init__(self, host, port=None):
        self.host = host
        self.port = port

    def conn(self):
        self.connector = happybase.Connection(self.host, self.port)
        self.connector.open()
        return self.connector

    def getAll(self, tableName, columns=[], filter=None):
        return self.connector.table(tableName).scan(columns=columns, filter=filter)

    def getOne(self, tableName, row_key):
        return self.connector.table(tableName).row(row_key)

    def close(self):
        self.connector.close()


if __name__ == '__main__':
    client = HBase('192.168.2.24')
    client.conn()
    # 获取所有的项目名称
    data = dict()
    for key, value in client.getAll('job', columns=['info:name','info:project']):
        project = value.get(b'info:project').decode()
        if project in data:
            continue
        # 查找该project的所有数据,同时过滤掉无start_time或end_time
        rows = client.getAll('job',
                             columns=["info:start_time","info:end_time","info:cpu","info:gpu","info:project"],
                             filter="SingleColumnValueFilter('info','project',=,'binary:{0}') AND "
                                    "SingleColumnValueFilter('info','start_time',!=,'binary:NULL') AND "
                                    "SingleColumnValueFilter('info','end_time',!=,'binary:NULL')".format(project))
        tmp_data = dict()
        counter = 0
        for row_key,row_data in rows:
            counter += 1
            cpu = int(row_data.get(b"info:cpu").decode())
            gpu = int(row_data.get(b"info:gpu").decode())
            start_time = datetime.datetime.fromtimestamp(int(row_data.get(b"info:start_time").decode()))
            end_time = datetime.datetime.fromtimestamp(int(row_data.get(b"info:end_time").decode()))
            if start_time.year not in tmp_data:
                tmp_data[start_time.year] = {}
            if start_time.month not in tmp_data[start_time.year]:
                tmp_data[start_time.year][start_time.month] = {
                    "cpu":0.0,
                    "gpu":0.0
                }
            run_hours = (end_time-start_time).seconds/3600
            tmp_data[start_time.year][start_time.month]['cpu'] += run_hours*cpu
            tmp_data[start_time.year][start_time.month]['gpu'] += run_hours*gpu
        data[project] = tmp_data
    client.close()
    with open('results.json','w') as f:
        f.write(json.dumps(data))