package org.coral.cache.caffeine.entity;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wuhao
 * @createTime 2021-08-30 19:06:00
 */
public class CacheObject {
	private final String data;

	public CacheObject(String data) {
		this.data = data;
	}

	public String getData() {
		objectCounter.incrementAndGet();
		return data;
	}

	private static AtomicInteger objectCounter = new AtomicInteger(0);
}
