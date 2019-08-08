package com.zjh.test.thread;

import java.util.concurrent.ThreadFactory;

public class ThreadFactoryImpl implements ThreadFactory {

	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r);
	}

}
