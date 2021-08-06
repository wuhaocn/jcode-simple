package org.coral.net.akka.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wuhao
 * @createTime 2021-08-06 10:10:00
 */
public class InnerMessageDefault implements IRoute{
	@Override
	public Map<String, String> getHeaders() {
		return new HashMap<>();
	}

	@Override
	public void setHeaders(Map<String, String> header) {

	}

	@Override
	public void setHeaders(String key, String value) {

	}

	@Override
	public Long getAppId() {
		return 1L;
	}

	@Override
	public String getMethod() {
		return "app";
	}

	@Override
	public String getTargetResourceId() {
		return "resid";
	}

	@Override
	public Serializable getAppMessage() {
		return new AppMessage();
	}

	@Override
	public void setAppMessage(Serializable appMessage) {

	}

	@Override
	public void setMethodWithCluster(String method) {

	}

	@Override
	public IInnerMessage getRouter() {
		return this;
	}
}
