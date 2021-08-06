package org.coral.net.akka.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wuhao
 * @createTime 2021-08-05 19:03:00
 */
public class AccessServerList {
	public static List<String> getAppNodeList() {
		List asList = Arrays.asList(
//				"127.0.0.1:5001",
//				"127.0.0.1:5002",
				"127.0.0.1:5003"
		);
		return asList;
	}

	public static List<String> getServerList() {
		List asList = Arrays.asList(
//				"127.0.0.1:2001",
//				"127.0.0.1:2002",
//				"127.0.0.1:2003",
//				"127.0.0.1:2004",
//				"127.0.0.1:2005",
				"127.0.0.1:2006"
		);
		return asList;
	}

	public static List<String> getServer2List() {
		List asList = Arrays.asList(
				"127.0.0.1:3001",
				"127.0.0.1:3002",
				"127.0.0.1:3003",
				"127.0.0.1:3004",
				"127.0.0.1:3005",
				"127.0.0.1:3006");
		return asList;
	}

	public static List<String> getServer3List() {
		List asList = Arrays.asList(
				"127.0.0.1:4001",
				"127.0.0.1:4002",
				"127.0.0.1:4003",
				"127.0.0.1:4004",
				"127.0.0.1:4005",
				"127.0.0.1:4006");
		return asList;
	}

	public static List<String> getAllServerList() {
		List asList = new ArrayList();
		asList.addAll(getServerList());
//		asList.addAll(getServer2List());
		return asList;
	}

	public static String getIp(String serverAddr) {
		String addr = serverAddr.split(":")[0];
		return addr;
	}

	public static String getPort(String serverAddr) {
		String port = serverAddr.split(":")[1];
		return port;
	}
}
