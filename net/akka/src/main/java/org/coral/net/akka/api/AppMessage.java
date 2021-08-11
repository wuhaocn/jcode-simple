package org.coral.net.akka.api;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;

/**
 * @author wuhao
 * @createTime 2021-08-06 10:20:00
 */
public class AppMessage implements Serializable {
	private int seq;
	private String uuid;
	private String method;
	private String version;
	private Object message;

	private long timeSign;

	private String accessNode;

	public AppMessage(int seq, String uuid, String method, String version, Object message, long timeSign) {
		this.seq = seq;
		this.uuid = uuid;
		this.method = method;
		this.version = version;
		this.message = message;
		this.timeSign = timeSign;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public long getTimeSign() {
		return timeSign;
	}

	public void setTimeSign(long timeSign) {
		this.timeSign = timeSign;
	}

	public String getAccessNode() {
		return accessNode;
	}

	public void setAccessNode(String accessNode) {
		this.accessNode = accessNode;
	}

	public static AppMessage build(int seq, String uuid, Object object) {
		return new AppMessage(seq, uuid, "default", "1.0", object, 0);
	}

	public static AppMessage copy(AppMessage appMessage) {
		AppMessage appMessageCopy = new AppMessage(appMessage.getSeq(), appMessage.getUuid(),
				appMessage.getMethod(), appMessage.getVersion(), appMessage.getMessage(), 0);
		return appMessageCopy;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("time:").append(DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.format(timeSign)).append(" ");
		sb.append("seq:").append(seq).append(" ");
		sb.append("uuid:").append(uuid).append(" ");
		sb.append("message:").append(message).append(" ");
		sb.append("accessNode:").append(accessNode).append(" ");
		return sb.toString();
	}
}
