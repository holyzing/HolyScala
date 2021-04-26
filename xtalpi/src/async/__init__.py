# -*- encoding: utf-8 -*-
"""
    1）使用最常规的同步编程要实现异步并发效果并不理想，或者难度极高。
    2）由于GIL锁的存在，多线程的运行需要频繁的加锁解锁，切换线程，这极大地降低了并发性能；

    1.协程是在单线程里实现任务的切换的
    2.利用同步的方式去实现异步
    3.不再需要锁，提高了并发性能


    用户态，进行上下文切换
    一个线程内，切换执行逻辑

    # 多路复用，密集型 IO，执行IO 不占用 CPU逻辑吗 ？？？占用的少 ？？？
"""
from typing import Any, Generator


def yield0(threshold):
    i = 11
    while i < threshold:
        print("--->yield0 before")
        r = yield i          # NOTE 产出并让步
        i += 1
        print("yield0 send:", r)
    # NOTE 执行到最后都没有遇到 yield 就会 抛出异常 StopIteration

# g = yield0(15)


def yieldFrom(threshold):
    i = 21
    g = yield0(15)
    # print(type(g), g.send(None))
    while i < threshold:
        print("--->yieldFrom before")
        r = yield from g           # 会自动预激
        i += 1
        print("yieldFrom send:", r)

# TODO 为什么 字符串 列表，字典，（生成器）（都属于 iterable的），一次 yield from 可以产出所有元素
#      而 由函数转化来的 生成器并不能一次 yield from 出所有元素 ？？
#      yield from 语法使用的限制 是 （函数体内 ？ 循环体内 ？）


def temp():
    # 字符串
    astr = 'ABC'
    # 列表
    alist = [1, 2, 3]
    # 字典
    adict = {"name": "wangbm", "age": 18}
    # 生成器
    agen: Generator[int, Any, None] = (i for i in range(4, 8))

    # <generator object temp.<locals>.<genexpr> at 0x7fd1d08bb2d0>
    # <class 'generator'>

    def gen(*args):
        for item in args:
            yield from item

    new_list = gen(astr, alist, adict, agen)
    print(list(new_list))


if __name__ == '__main__':
    # NOTE 首次 Next 在业务上可以理解为 预激, 首次调用 send(None) 也是预激，
    #      send 和 next 都会使生成器产出并让步
    #      预激：只执行 yield 右侧后就产出并让步，
    #      预激后：先执行 yield 左侧，直到碰到 yield，执行其右侧后产出并让步

    # g = yield0(15)
    # print(g)
    # print("--------------")
    # print(g.send(None))       # can't send non-None value to a just-started generator
    # print("--------------")
    # print(g.send(-1))
    # print("--------------")
    # print(next(g))
    # print("--------------")
    # print(g.send(-2))
    # print("--------------")
    # print(next(g))

    gF = yieldFrom(25)
    print("预激", next(gF))
    print("-1", gF.send(-1))
    print("-2", gF.send(-2))
    print("-3", gF.send(-3))
    print("-4", gF.send(-4))
