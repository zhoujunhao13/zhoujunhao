package com.zjh.test;

import com.google.common.util.concurrent.Monitor;

public class MyWaitNotify3 {
	
	Monitor monitor = new Monitor();
	boolean wasSignalled = false;
	
	public void dowait() {
		synchronized(monitor) {
			while(!wasSignalled) {
				try {
					monitor.wait();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			wasSignalled = false;
		}

	}
	
	public void donotify() {
		synchronized (monitor) {
			wasSignalled = true;
			monitor.notify();
		}
	}

}
