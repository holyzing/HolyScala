# -*- encoding: utf-8 -*-
import redis
from redis import Redis

if __name__ == '__main__':
    connection_pool = redis.ConnectionPool(host='127.0.0.1', db=0, password="holyzing")
    rd: Redis = redis.Redis(connection_pool=connection_pool)
    db, keys = rd.scan()  # keys
    for r in keys:
        print(f"{r}={rd.get(r)}")
    print(rd.scan(1))


# redis的数据结构设计的挺巧妙的, redis的数据结构设计 ？
# redis作为多线程的锁
