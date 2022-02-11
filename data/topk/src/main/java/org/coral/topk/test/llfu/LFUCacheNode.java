package org.coral.topk.test.llfu;

/**
 * LFUCacheNode
 *
 * @author wuhao
 * @createTime 2022-02-11 14:44:00
 */
public class LFUCacheNode<K,V> {
	private K key;
	private V value;
	private  int frequency = 1;

	public LFUCacheNode(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public int getFrequency() {
		return frequency;
	}
	public int incFrequency() {
		return frequency++;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("key=").append(key).append(", ");
		if (value != null){
			sb.append("value=").append(key).append(", ");
		}
		sb.append("frequency=").append(frequency);
		return sb.toString();
	}
}
