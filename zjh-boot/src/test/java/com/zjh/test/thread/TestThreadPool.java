package com.zjh.test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThreadPool implements Runnable {
	
	private int num;
	
	public TestThreadPool(int num) {
		this.num = num;
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	public static void main(String[] args) {
		ExecutorService es1 = Executors.newCachedThreadPool();
		TestThreadPool t1 = new TestThreadPool(1);
		TestThreadPool t2 = new TestThreadPool(2);
		TestThreadPool t3 = new TestThreadPool(3);
		TestThreadPool t4 = new TestThreadPool(4);
		
		es1.execute(t1);
		es1.execute(t2);
		es1.execute(t3);
		es1.execute(t4);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(t1.getNum());
		System.out.println(t2.getNum());
		System.out.println(t3.getNum());
		System.out.println(t4.getNum());
	}

	@Override
	public void run() {
		setNum(++this.num);
	}

}
