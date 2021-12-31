package org.jcode.data;

import java.util.concurrent.CountDownLatch;

/**
 * @author wuhao
 * @createTime 2021-12-22 11:14:00
 */
public class TaskMark {
	private int count = 100;
	private CountDownLatch record = null;

	public TaskMark(int count) {
		this.count = count;
		this.record = new CountDownLatch(count);
	}

	public void mark() {
		record.countDown();
	}

	public void waitComplete() throws InterruptedException {
		record.await();
	}

	public TaskMark build(int count) {
		TaskMark taskMark = new TaskMark(count);
		return taskMark;
	}

}
