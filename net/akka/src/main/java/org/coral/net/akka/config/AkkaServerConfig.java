package org.coral.net.akka.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wuhao
 * @createTime 2021-08-05 19:03:00
 */
public class AkkaServerConfig {
	public static List<String> getAppNodeList() {
		List asList = Arrays.asList(
//				"127.0.0.1:10001",
//				"127.0.0.1:10002",
//				"127.0.0.1:10003",
//				"127.0.0.1:10004",
//				"127.0.0.1:10005",
//				"127.0.0.1:10006",
//				"127.0.0.1:10007",
//				"127.0.0.1:10008",
//				"127.0.0.1:10009",
//				"127.0.0.1:10010",
//				"127.0.0.1:10011",
				"127.0.0.1:10012"
		);
		return asList;
	}

	public static List<String> getServerList() {
		List asList = Arrays.asList(
				"127.0.0.1:2001",
				"127.0.0.1:2002",
				"127.0.0.1:2003",
				"127.0.0.1:2004",
				"127.0.0.1:2005",
				"127.0.0.1:2006",
				"127.0.0.1:2007",
				"127.0.0.1:2008",
				"127.0.0.1:2009",
				"127.0.0.1:2010",
				"127.0.0.1:2011",
				"127.0.0.1:2012",
				"127.0.0.1:2013",
				"127.0.0.1:2014",
				"127.0.0.1:2015",
				"127.0.0.1:2016",
				"127.0.0.1:2017",
				"127.0.0.1:2018",
				"127.0.0.1:2019"
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
				"127.0.0.1:3006",
				"127.0.0.1:3007",
				"127.0.0.1:3008",
				"127.0.0.1:3009",
				"127.0.0.1:3010",
				"127.0.0.1:3011",
				"127.0.0.1:3012",
				"127.0.0.1:3013",
				"127.0.0.1:3014",
				"127.0.0.1:3015",
				"127.0.0.1:3016",
				"127.0.0.1:3017",
				"127.0.0.1:3018",
				"127.0.0.1:3019");
		return asList;
	}
	public static List<String> getServer3List() {
		List asList = Arrays.asList(
				"127.0.0.1:4001",
				"127.0.0.1:4002",
				"127.0.0.1:4003",
				"127.0.0.1:4004",
				"127.0.0.1:4005",
				"127.0.0.1:4006",
				"127.0.0.1:4007",
				"127.0.0.1:4008",
				"127.0.0.1:4009",
				"127.0.0.1:4010",
				"127.0.0.1:4011",
				"127.0.0.1:4012",
				"127.0.0.1:4013",
				"127.0.0.1:4014",
				"127.0.0.1:4015",
				"127.0.0.1:4016",
				"127.0.0.1:4017",
				"127.0.0.1:4018",
				"127.0.0.1:4019");
		return asList;
	}


	public static List<String> getServer4List() {
		List asList = Arrays.asList(
				"127.0.0.1:5001",
				"127.0.0.1:5002",
				"127.0.0.1:5003",
				"127.0.0.1:5004",
				"127.0.0.1:5005",
				"127.0.0.1:5006",
				"127.0.0.1:5007",
				"127.0.0.1:5008",
				"127.0.0.1:5009",
				"127.0.0.1:5010",
				"127.0.0.1:5011",
				"127.0.0.1:5012",
				"127.0.0.1:5013",
				"127.0.0.1:5014",
				"127.0.0.1:5015",
				"127.0.0.1:5016",
				"127.0.0.1:5017",
				"127.0.0.1:5018",
				"127.0.0.1:5019");
		return asList;
	}
	public static List<String> getServer5List() {
		List asList = Arrays.asList(
				"127.0.0.1:6001",
				"127.0.0.1:6002",
				"127.0.0.1:6003",
				"127.0.0.1:6004",
				"127.0.0.1:6005",
				"127.0.0.1:6006",
				"127.0.0.1:6007",
				"127.0.0.1:6008",
				"127.0.0.1:6009",
				"127.0.0.1:6010",
				"127.0.0.1:6011",
				"127.0.0.1:6012",
				"127.0.0.1:6013",
				"127.0.0.1:6014",
				"127.0.0.1:6015",
				"127.0.0.1:6016",
				"127.0.0.1:6017",
				"127.0.0.1:6018",
				"127.0.0.1:6019");
		return asList;
	}

	/**
	 * getToAppNodeCount
	 * 
	 * @return
	 */
	public static int getToAppNodeCount() {
		return 2;
	}

	public static List<String> getAllServerList() {
		List asList = new ArrayList();
		asList.addAll(getServerList());
		asList.addAll(getServer2List());
		asList.addAll(getServer3List());
		asList.addAll(getServer4List());
		asList.addAll(getServer5List());
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
