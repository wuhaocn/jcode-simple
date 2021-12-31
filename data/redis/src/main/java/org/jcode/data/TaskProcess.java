package org.jcode.data;

import java.util.Date;

/**
 * @author wuhao
 * @createTime 2021-12-22 10:50:00
 */
public class TaskProcess {
	public static void main(String[] args) {
		process(10000 * 20);
	}

	public static void process(int count) {
		System.out.println("start time" + new Date());
		for (int i = 0; i < count; i++) {
			//doWork();
		}
		System.out.println("end time" + new Date());
	}

	public static void doWork(){
		try {
			Thread.sleep(5);
		} catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		}
	}
}
