# -*- encoding: utf-8 -*-
"""
    用户态，进行上下文切换
    一个线程内，切换执行逻辑

    # 多路复用，密集型 IO，执行IO 不占用 CPU逻辑吗 ？？？占用的少 ？？？
"""


def yield0(threshold):
    i = 11
    while i < threshold:
        print("--->yield0 before")
        r = yield i          # NOTE 产出并让步
        i += 1
        print("yield0 send:", r)
    # NOTE 执行到最后都没有遇到 yield 就会 抛出异常 StopIteration

# NOTE 生成器的嵌套 使用 yield from，可以实现代码逻辑的控制切换


g = yield0(15)


def yieldFrom(threshold):
    i = 21
    while i < threshold:
        print("--->yieldFrom before")
        r = yield from g
        i += 1
        print("yieldFrom send:", r)


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

    g = yieldFrom(25)
    print(next(g))



