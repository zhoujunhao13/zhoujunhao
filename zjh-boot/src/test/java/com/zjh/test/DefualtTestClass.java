package com.zjh.test;

import java.util.List;

import org.assertj.core.util.Arrays;

public class DefualtTestClass implements DefualtTest {
	
	/*public void test() {
		DefualtTest.super.test();
	}*/
	
	public static void main(String[] args) {
		new DefualtTestClass().test();
		DefualtTest.print();
		String str[] = new String[] {"1","2","3","4","5"};
		List<Object> list = Arrays.asList(str);
		list.stream().forEach(e-> {
			int i = Integer.valueOf(String.valueOf(e)) + 1;
			System.out.println(i);
		});
	}

}
