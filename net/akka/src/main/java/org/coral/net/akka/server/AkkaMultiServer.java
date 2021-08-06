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
		int i = 0;
		for (String serverAddr : list) {
			startServer(serverAddr, classT, i);
			i++;
		}
	}

	public void startServer(String serverAddr, Class classT, int i) throws Exception {

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					Thread.sleep(i * 3000);
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
