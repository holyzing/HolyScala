package com.holy.leetcode;

import org.junit.Test;

/**
 * byte  ->   8 bits  -->1字节
 * char  ->   16 bit  -->2字节
 * short ->   16 bits -->2字节
 * int   ->   32 bits -->4字节
 * float ->   32 bits -->4字节
 * long  ->   64 bits -->8字节
 *
 * int数据底层以补码形式存储。int型变量使用32bit存储数据，其中最高位是符号位，
 * 0表示正数，1表示负数，可通过Integer.toBinaryString()转换为bit字符串
 *
 * 源码 反码 补码 真值
 */

public class BitMap {
    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(10));
        // 01010 = 10 原码 反码 补码
        System.out.println(Integer.toBinaryString(-10));
        // 11111111111111111111111111110110 补码
        // 11111111111111111111111111110101 反码
        // 10000000000000000000000000001010 原码
        System.out.println(Integer.toBinaryString(5));
        // 0101
        System.out.println(Integer.toBinaryString(5 << 2));  // 左移2位，低位补零
        // 010100
        System.out.println(Integer.toBinaryString(5 >> 2));  // 右移2位，高位补零
        // 01
        System.out.println(Integer.toBinaryString(5 >>> 3)); // 右移3位，排除符号位

        // 2进制补码的加法运算，和原码一样，而且符号位也参与运算，不过最后只保留32位

        System.out.println("----------------------------------------");

        System.out.println(Integer.toBinaryString(-5));
        // 1,101 -> 1,010 -> 1,011 -> 11111111111111111111111111111011
        System.out.println(Integer.toBinaryString(-5 << 2));  // 左移2位，低位补零
        // 11111111111111111111111111101100
        // 11111111111111111111111111101011
        // 10000000000000000000000000010100 -> 2^4 + 2^2 = 20
        System.out.println(Integer.toBinaryString(-5 >> 2));  // 右移2位，高位补1
        // 11111111111111111111111111111110
        // 11111111111111111111111111111101
        // 10000000000000000000000000000010

        // 带符号移位：正数左移右移高低位均补零，负数左移低位补零，右移高位补1
        // 无符号移位：正数左移右移高低位均补零，负数左移右移高低位均补零

        System.out.println(Integer.toBinaryString(-5 >>> 3));
        // 11111111111111111111111111111111                     // 用1补位
        // 11111111111111111111111111111110
        // 10000000000000000000000000000001 -> -1

        // 用0进行补位，包括符号位,不对移位后的补码进行还原
        // 00011111111111111111111111111111 -> 536870911

        // 按位 与（&）
        // 按位 或（|）
        // 移位超过 数据类型的长度没有什么意义，所以一般会 按类型长度取模 （mod|%）

        // 位与 代替 取余 运算
        // 31转换为二进制后，低位值全部为1，高位全为0。所以和其进行与运算，高位和0与，结果是0，
        // 相当于将高位全部截取，截取后的结果肯定小于等于31，低位全部为1，与1与值为其本身，所以相当于对数进行了取余操作。

        // 011111 -> 2^4 + 2^3 + 2^2 + 2^1 + 2^0 = 16 + 8 + 4 + 2 + 1 = 31
        // NOTE 当 a < b 时 a % b <==> a & b

        System.out.println(Integer.toHexString(8));
        System.out.println(Integer.toOctalString(12));
        System.out.println(Integer.toBinaryString(8));
        System.out.println(Integer.valueOf("11111", 3).toString());
    }

    @Test
    public void bitMap(){
        /*
        在 32 个整形数字中查询需要 32 * 32 bit，假如用每一位来表示每一个数字，则只需要一个整形数字的存储容量。
        前提是 如何将每一位和 40 亿数据做关联映射 ？？？
        事先这些数据显然要是有序的？？？，这和二分查找有啥区别 ？？？
         */
        long x = 100000001L;
        System.out.println(Math.floor(x >> 5));  // Index
        System.out.println(x % 32);              // Position
        /*
        　 1：看个小场景 > 在3亿个整数中找出不重复的整数，限制内存不足以容纳3亿个整数。
             对于这种场景我可以采用2-BitMap来解决，即为每个整数分配2bit，用不同的0、1组合来标识特殊意思，
             如00表示此整数没有出现过，01表示出现一次，11表示出现过多次，就可以找出重复的整数了，其需要的内存空间是正常BitMap的2倍，
             为：3亿*2/8/1024/1024=71.5MB。

             扫描着3亿个整数，组BitMap，先查看BitMap中的对应位置，如果00则变成01，是01则变成11，是11则保持不变，
             当将3亿个整数扫描完之后也就是说整个BitMap已经组装完毕。最后查看BitMap将对应位为11的整数输出即可。

　　       2: 已知某个文件内包含一些电话号码，每个号码为8位数字，统计不同号码的个数。
            8位最多99 999 999，大概需要99m个bit，大概10几m字节的内存即可。
            （可以理解为从0-99 999 999的数字，每个数字对应一个Bit位，所以只需要99M个Bit==1.2MBytes，
            这样，就用了小小的1.2M左右的内存表示了所有的8位数的电话）

          3: BitMap 的思想在面试的时候还是可以用来解决不少问题的，然后在很多系统中也都会用到，算是一种不错的解决问题的思路。
          但是 BitMap 也有一些局限，因此会有其它一些基于 BitMap 的算法出现来解决这些问题。

          数据碰撞。比如将字符串映射到 BitMap 的时候会有碰撞的问题，那就可以考虑用 Bloom Filter 来解决，
          Bloom Filter 使用多个 Hash 函数来减少冲突的概率。
          数据稀疏。又比如要存入(10,8887983,93452134)这三个数据，我们需要建立一个 99999999 长度的 BitMap ，
          但是实际上只存了3个数据，这时候就有很大的空间浪费，碰到这种问题的话，可以通过引入 Roaring BitMap 来解决。

          NOTE 确定BIt Map的 大小往往是那个最大值
        */
    }

}
