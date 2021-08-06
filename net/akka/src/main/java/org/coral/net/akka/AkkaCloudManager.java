package org.coral.net.akka;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AkkaCloudManager {
	public static Map<String, AkkaCloudCluster> clusterMap = new ConcurrentHashMap<>();

	public static AkkaCloudCluster getAkkaCloudCluster(String addr) {
		return clusterMap.get(addr);
	}

	public static AkkaCloudCluster putAkkaCloudCluster(String addr, AkkaCloudCluster akkaCloudCluster) {
		return clusterMap.put(addr, akkaCloudCluster);
	}
}
