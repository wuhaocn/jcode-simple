package org.coral.topk.test.lfu;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * LFUCache
 *
 * @param <K>
 * @param <V>
 * @author wuhao
 */
public class LFUCache<K, V> {
	/**
	 * key 到 value 的映射（一对一）
	 */
	private Map<K, V> KV;
	/**
	 * key 到 frequency 的映射（一对一）
	 */
	private Map<K, Integer> KF;
	/**
	 * frequency 到 key 的映射（一对多）
	 */
	private Map<Integer, LinkedHashSet<K>> FK;
	/**
	 * 记录最小频次
	 */
	private int minFrequency;
	/**
	 * 缓存容量
	 */
	private int cap;

	public LFUCache(int capacity) {
		KV = new HashMap<>();
		KF = new HashMap<>();
		FK = new HashMap<>();
		this.cap = capacity;
		this.minFrequency = 0;
	}

	/**
	 * get
	 * @param key
	 * @return
	 */
	public V get(K key) {
		if (!KV.containsKey(key)) {
			return null;
		}
		increaseFreq(key);
		return KV.get(key);
	}

	/**
	 * put
	 *
	 * @param key
	 * @param value
	 */
	public void put(K key, V value) {
		if (this.cap <= 0) {
			return;
		}
		// 给定的 key 在 KV 中不存在
		if (!KV.containsKey(key)) {
			// 容量已经满了，删掉一个
			if (this.cap == KV.size()) {
				removeOne();
			}
			KV.put(key, value);
			KF.put(key, 1);
			FK.putIfAbsent(1, new LinkedHashSet<K>());
			FK.get(1).add(key);
			this.minFrequency = 1;
		} else {
			// 给定的 key 在 KV 中已经存在
			KV.put(key, value);
			increaseFreq(key);
		}
	}

	/**
	 * 增加相应key的频数
	 *
	 * @param key
	 */
	private void increaseFreq(K key) {
		int oldFreq = KF.get(key);
		int newFreq = oldFreq + 1;
		KF.put(key, newFreq);
		if (!FK.containsKey(newFreq)) {
			LinkedHashSet<K> sameFreqKey = new LinkedHashSet<>();
			sameFreqKey.add(key);
			FK.put(newFreq, sameFreqKey);
		} else {
			FK.get(newFreq).add(key);
		}
		FK.get(oldFreq).remove(key);
		if (FK.get(oldFreq).isEmpty()) {
			FK.remove(oldFreq);
			if (oldFreq == this.minFrequency) {
				this.minFrequency++;
			}
		}
	}

	/**
	 * 删掉使用频率最低的值
	 *
	 */
	private void removeOne() {
		LinkedHashSet<K> keyList = FK.get(this.minFrequency);
		K deletedKey = keyList.iterator().next();
		KV.remove(deletedKey);
		KF.remove(deletedKey);
		keyList.remove(deletedKey);
		if (keyList.isEmpty()) {
			FK.remove(this.minFrequency);
		}
	}
}