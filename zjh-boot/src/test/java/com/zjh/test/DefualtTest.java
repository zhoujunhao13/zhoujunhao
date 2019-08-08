package com.zjh.test;

public interface DefualtTest {
	
	default void test() {
		System.out.println(1111);
	}
	
	static void print() {
		System.out.println("print");
	}

}
