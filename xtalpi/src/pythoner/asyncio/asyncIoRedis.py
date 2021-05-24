# -*- encoding: utf-8 -*-

import time
import redis
import asyncio
from queue import Queue
from threading import Thread


# 将协程态添加到事件循环中
class EventLoopCoroutine(object):

    @staticmethod
    def start_loop(loop):
        # 一个在后台永远运行的事件循环
        asyncio.set_event_loop(loop)
        loop.run_forever()

    @staticmethod
    def do_sync_sleep(x, que, mesg=""):
        time.sleep(x)
        que.put(mesg)

    @staticmethod
    async def async_do_sleep(x, que, mesg="ok"):
        await asyncio.sleep(x)
        que.put(mesg)

    @staticmethod
    def get_redis():
        connection_pool = redis.ConnectionPool(host='127.0.0.1', db=0)
        return redis.Redis(connection_pool=connection_pool)

    @staticmethod
    def consumer(rcon, que, event_loop):
        while True:
            task = rcon.rpop("queue")
            if not task:
                time.sleep(1)
                continue
            asyncio.run_coroutine_threadsafe(EventLoopCoroutine.async_do_sleep(int(task), que), event_loop)

    @staticmethod
    def coroutineThread(way="async"):
        """
        对于并发任务，通常是用生产者消费着模型，对队列的处理可以使用类似master-worker的方式，
        master主要用户获取队列的msg，worker用户处理消息。
        为了简单起见，并且协程更适合单线程的方式，主线程用来监听队列，子线程用于处理队列。
        使用redis的队列。主线程中有一个是无限循环，用户消费队列。

        输入key=queue，value=5,3,1的消息

        loop_thread：单独的线程，运行着一个事件对象容器，用于实时接收新任务。
        consumer_thread：单独的线程，实时接收来自Redis的消息队列，并实时往事件对象容器中添加新任务。
        """
        queue = Queue()
        print(time.ctime())
        new_loop = asyncio.new_event_loop()
        if way != redis:
            # 定义一个线程，并传入一个事件循环对象
            t = Thread(target=EventLoopCoroutine.start_loop, args=(new_loop,))
            t.start()
            # 动态添加两个协程
            if way == "sync":
                # 这种方法，在主线程是同步的 NOTE 找出当前 eventloop 所在的线程
                new_loop.call_soon_threadsafe(EventLoopCoroutine.do_sync_sleep, 6, queue, "第一个")
                new_loop.call_soon_threadsafe(EventLoopCoroutine.do_sync_sleep, 3, queue, "第二个")
            elif way == "async":
                # 这种方法，在主线程是异步的
                asyncio.run_coroutine_threadsafe(EventLoopCoroutine.async_do_sleep(6, queue, "第一个"), new_loop)
                asyncio.run_coroutine_threadsafe(EventLoopCoroutine.async_do_sleep(3, queue, "第二个"), new_loop)
            else:
                return "invalid async way"
        else:
            # 定义一个线程，运行一个事件循环对象，用于实时接收新任务
            loop_thread = Thread(target=EventLoopCoroutine.start_loop, args=(new_loop,))
            loop_thread.setDaemon(True)
            loop_thread.start()
            # 创建redis连接
            rcon = EventLoopCoroutine.get_redis()
            # 子线程：用于消费队列消息，并实时往事件对象容器中添加新任务
            consumer_thread = Thread(target=EventLoopCoroutine.consumer, args=(rcon, queue, new_loop))
            consumer_thread.setDaemon(True)
            consumer_thread.start()

        while True:
            msg = queue.get()
            print("{} 协程运行完..".format(msg))
            print(time.ctime())


if __name__ == '__main__':
    EventLoopCoroutine.coroutineThread()
