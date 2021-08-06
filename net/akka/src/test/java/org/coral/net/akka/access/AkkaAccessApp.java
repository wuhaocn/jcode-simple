package org.coral.net.akka.access;


import org.coral.net.akka.AkkaCloudCluster;
import org.coral.net.akka.config.AccessServerList;
import org.coral.net.akka.config.AkkaClusterConfig;

import java.util.List;

/**
 * @
 */
public class AkkaAccessApp {


	public void startServers(List<String> list) throws Exception {
		for (String serverAddr : list) {
			startServer(serverAddr);
		}
	}

	public void startServer(String serverAddr) throws Exception {

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				try {

					AkkaClusterConfig clusterConfig = AkkaClusterConfig.buildAkkaClusterConfig(AccessServerList.getIp(serverAddr),
							AccessServerList.getPort(serverAddr));
					AkkaCloudCluster akkaCloudCluster = new AkkaCloudCluster();
					akkaCloudCluster.initClusters(clusterConfig, AccessActor.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

}
