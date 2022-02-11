package org.coral.topk.test.llfu;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author wuhao
 * @createTime 2022-02-11 14:45:00
 */
public class AccessQueue {
	private ConcurrentLinkedQueue<String> linkedQueue = new ConcurrentLinkedQueue<>();

	public void access(String key) {
		linkedQueue.offer(key);
	}

	public String poll() {
		return linkedQueue.poll();
	}

	public void clean() {
		//linkedQueue.clear();
	}

}
