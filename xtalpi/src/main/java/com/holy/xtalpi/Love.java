package com.holy.xtalpi;

import static java.lang.System.out;

public class Love {
	public static void main(String[] args) {
		String sjoin = String.join("1", "a", "b", "c");
		out.println(sjoin);
		char[] chars1 = new char[10];
		char[] chars2 = {'a', 'c', 'b'};
		char[] chars3 = new char[]{'a', 'c', 'b'};
		out.println(chars1);
		out.println(chars2);
		out.println(chars3);
		for (char a: chars2) {
			out.println(a);
		}
		StaticClass sc = new StaticClass();
		sc.test();
	}

	public static class StaticClass {
		public static void main(String[] args) {
			out.println("内部静态类的调用！与实例化");
		}
		public void test() {
			out.println("抽象类不能被实例化！");
		}
	}
}
