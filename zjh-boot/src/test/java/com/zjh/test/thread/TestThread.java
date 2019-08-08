package com.zjh.test.thread;

public class TestThread extends Thread {

	@Override
	public void run() {
		for(int i=0;i<5;i++)
		{
			System.out.println(i);
		}
	}
	
	public static void main(String[] args) {
		TestThread t = new TestThread();
		System.out.println(t.getId());
		t.start();
		System.out.println(t.getName());
		for(int i=0;i<10;i++) {
			System.out.println("......main");
		}
	}
}
