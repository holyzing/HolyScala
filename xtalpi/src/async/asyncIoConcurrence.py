# -*- encoding: utf-8 -*-
import time
import asyncio
from asyncio.futures import Future
from collections.abc import Coroutine  # Generator
"""
    将协程对象转为task任务对象
    定义一个事件循环对象容器用来存放task
    将task任务扔进事件循环对象中并触发

    协程中的并发
    协程中的嵌套
    协程中的状态
    gather与wait
"""


# 协程中的单任务
class SingleTaskTest(object):

    def __init__(self, name, count=0):
        self.name = name
        self.count = count

    async def coroutineFunc(self):
        while self.count < 20:
            await asyncio.sleep(2)    # 等待一个 Coroutine，并产生一个 Future
            self.count += 1
            print("------->>", "after last await", self.count)

    @asyncio.coroutine
    def generatorFunc(self):
        while self.count < 20:
            r = yield self.count
            self.count += 1
            print("------->>", "after last yield:", self.count, r)
        return self.count

    @asyncio.coroutine
    def generatorFunc2(self):
        while self.count < 20:
            # NOTE 去掉 @asyncio.coroutine 抛出异常
            # cannot 'yield from' a coroutine object in a non-coroutine generator
            r = yield from asyncio.sleep(2)
            self.count += 1
            print("------->>", "after last yield:", self.count, r)

    async def asyncIOFunc(self, sleepTime):
        print(self.name, "sleep begin")
        time.sleep(sleepTime)
        print(self.name, "sleep finished")
        return self.count

    @staticmethod
    def test():
        st = SingleTaskTest("ll", 10)
        c = st.coroutineFunc()        # 并不是直接调用一个函数, 而是产生一个协程对象
        print(c.send(None))
        # print(c.send(None))         # RuntimeError: await wasn't used with future
        print("---------------------------------------------------")
        c = st.generatorFunc()
        print(type(c))
        print(c.send(None))
        print(c.send(1))
        print("---------------------------------------------------")
        c = st.generatorFunc2()
        print(type(c))
        f: Future = c.send(None)
        f.set_result(2)
        print(f.result())
        print("---------------------------------------------------")
        c = st.asyncIOFunc(5)
        print(type(c))
        # c.send(None)
        print("---------------------------------------------------")
        loop = asyncio.get_event_loop()
        task = asyncio.ensure_future(c)
        loop.run_until_complete(task)


class MultiTaskTest(object):
    @staticmethod
    async def do_some_work(x):
        print('Waiting: ', x)
        await asyncio.sleep(x)
        return 'Done after {}s'.format(x)

    @staticmethod
    def create_tasks():
        coroutine1 = MultiTaskTest.do_some_work(1)
        coroutine2 = MultiTaskTest.do_some_work(2)
        coroutine3 = MultiTaskTest.do_some_work(4)

        # 将协程转成task，并组成list
        tasks = [
            asyncio.ensure_future(coroutine1),
            asyncio.ensure_future(coroutine2),
            asyncio.ensure_future(coroutine3)
        ]
        return tasks

    @staticmethod
    def run_tasks(tasks):
        loop = asyncio.get_event_loop()
        if isinstance(tasks, list):
            loop.run_until_complete(asyncio.wait(tasks))
            for task in tasks:
                print('Task ret: ', task.result())
        elif callable(tasks):
            loop.run_until_complete(tasks())

    @staticmethod
    async def nestedCoroutines(method="wait"):
        # 使用async可以定义协程，协程用于耗时的io操作，我们也可以封装更多的io操作过程，
        # 这样就实现了嵌套的协程，即一个协程中await了另外一个协程，如此连接起来。
        # 【重点】：await 一个task列表（协程）
        # dones：表示已经完成的任务
        # pendings：表示悬而未决的任务
        if method == "wait":
            dones, pendings = await asyncio.wait(MultiTaskTest.create_tasks())
            for task in dones:
                print('Task ret: ', task.result())
        elif method == "gather":
            results = await asyncio.gather(*MultiTaskTest.create_tasks())
            for result in results:
                print('Task ret: ', result)


if __name__ == '__main__':
    # SingleTaskTest.test()
    MultiTaskTest.run_tasks(MultiTaskTest.create_tasks())
