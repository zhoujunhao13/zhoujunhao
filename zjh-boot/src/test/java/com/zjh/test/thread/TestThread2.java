package com.zjh.test.thread;

public class TestThread2 implements Runnable {

	@Override
	public void run() {
		for(int i=0;i<5;i++)
		{
			System.out.println(i);
		}
	}
	
	public static void main(String[] args) {
		TestThread2 t = new TestThread2();
		new Thread(t).start();
		for(int i=0;i<5;i++) {
			System.out.println("......main");
		}
	}

}
