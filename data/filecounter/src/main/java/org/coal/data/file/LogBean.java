package org.coal.data.file;

/**
 * @author wuhao
 * @createTime 2021-10-09 11:47:00
 */
public class LogBean {
	private String key;
	private String type;
	private long size;

	private String message;

	public LogBean(String key, String type, long size, String message) {
		this.key = key;
		this.type = type;
		this.size = size;
		this.message = message;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
