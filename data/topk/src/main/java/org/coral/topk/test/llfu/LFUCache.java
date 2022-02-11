package org.coral.topk.test.llfu;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LFUCache
 *
 * @author wuhao
 */
public class LFUCache<K,V> {

	/**
	 * 容量限制
	 */
	private int capacity;
	/**
	 * 当前数据个数
	 */
	private int size;
	/**
	 * 当前最小频率
	 */
	private int minFreq;
	/**
	 * key和数据的映射
	 */
	private Map<K, LFUCacheNode> map;
	/**
	 * 数据频率和对应数据组成的链表
	 */
	private Map<Integer, LinkedHashSet<LFUCacheNode>> freqMap;

	public LFUCache(int capacity) {
		this.capacity = capacity;
		this.size = 0;
		this.minFreq = 1;
		this.map = new ConcurrentHashMap<>();
		this.freqMap = new ConcurrentHashMap<>();
	}

	public V get(K key) {

		LFUCacheNode<K,V> node = map.get(key);
		if (node == null) {
			return null;
		}
		// 增加数据的访问频率
		freqPlus(node);
		return node.getValue();
	}

	public Map<K, LFUCacheNode> report() {
		Map<K, LFUCacheNode> report = map;
		map = new ConcurrentHashMap<>();
		freqMap = new ConcurrentHashMap<>();
		return report;
	}

	public void put(K key, V value) {

		if (capacity <= 0) {
			return;
		}

		LFUCacheNode node = map.get(key);
		if (node != null) {
			// 如果存在则增加该数据的访问频次
			node.setValue(value);
			freqPlus(node);
		} else {
			// 淘汰数据
			eliminate();
			// 新增数据并放到数据频率为1的数据链表中
			LFUCacheNode newNode = new LFUCacheNode(key, value);
			map.put(key, newNode);
			LinkedHashSet<LFUCacheNode> set = freqMap.get(1);
			if (set == null) {
				set = new LinkedHashSet<>();
				freqMap.put(1, set);
			}

			set.add(newNode);
			minFreq = 1;
			size++;
		}

	}

	private void eliminate() {

		if (size < capacity) {
			return;
		}

		LinkedHashSet<LFUCacheNode> set = freqMap.get(minFreq);
		LFUCacheNode node = set.iterator().next();
		set.remove(node);
		map.remove(node.getKey());

		size--;
	}

	void freqPlus(LFUCacheNode node) {

		int frequency = node.getFrequency();
		LinkedHashSet<LFUCacheNode> oldSet = freqMap.get(frequency);
		if (oldSet != null){
			oldSet.remove(node);
			// 更新最小数据频率
			if (minFreq == frequency && oldSet.isEmpty()) {
				minFreq++;
			}
		}

		frequency++;
		node.incFrequency();
		LinkedHashSet<LFUCacheNode> set = freqMap.get(frequency);
		if (set == null) {
			set = new LinkedHashSet<>();
			freqMap.put(frequency, set);
		}
		set.add(node);
	}
}

