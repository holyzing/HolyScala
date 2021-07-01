# -*- encoding: utf-8 -*-
import redis
from redis import Redis
"""
设置密码：
    redis.conf配置来启用认证，在配置文件中有个参数： requirepass 
    redis-cli -p 6379 -a test123

info  dbsize  monitor  flushdb 
showlog get 10
confg set requirepass 123

type exists
get mget keys randomkey
set setnx mset msetnx getset append del rename
 
1- redis 为什么那么快
2- redis 为什么是线程安全的
3- redis 的作用有哪些？
4- redis 事务
5- redis 中的乐观锁
6- redis 集群选举  集群情况下是否支持一个实例多个db
7- redis 的原子性
答：缓存，消息队列，分布式锁


"""


def code_test():
    a = "大萨达"  # 字面量默认使用 UTF-8 编码
    ab = a.encode(encoding="UTF-8")
    print(a, ab, ab.decode("GBK"))


if __name__ == '__main__':
    connection_pool = redis.ConnectionPool(host='127.0.0.1', db=0, password="holyzing")
    rd: Redis = redis.Redis(connection_pool=connection_pool)
    r = rd.incr("a", 1)


# redis的数据结构设计的挺巧妙的, redis的数据结构设计 ？
# redis作为多线程的锁
