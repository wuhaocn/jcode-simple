package org.coral.net.akka.server;


import org.coral.net.akka.AkkaCloudCluster;
import org.coral.net.akka.config.AccessServerList;
import org.coral.net.akka.config.AkkaClusterConfig;

import java.util.List;

/**
 * @
 */
public class AkkaMultiServer {


	public void startServers(List<String> list, Class classT) throws Exception {
		for (String serverAddr : list) {
			startServer(serverAddr, classT);
		}
	}

	public void startServer(String serverAddr, Class classT) throws Exception {

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					AkkaClusterConfig clusterConfig = AkkaClusterConfig.buildAkkaClusterConfig(AccessServerList.getIp(serverAddr),
							AccessServerList.getPort(serverAddr));
					AkkaCloudCluster akkaCloudCluster = new AkkaCloudCluster();
					akkaCloudCluster.initClusters(clusterConfig, classT);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

}
