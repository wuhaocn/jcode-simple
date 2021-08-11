package org.coral.net.akka.server;


import org.coral.net.akka.AkkaCloudCluster;
import org.coral.net.akka.config.AkkaClusterConfig;
import org.coral.net.akka.config.AkkaServerConfig;

import java.util.List;

/**
 * @author wuhao
 */
public class AkkaMultiServer {


	public void startServers(List<String> list, Class classT) throws Exception {
		int i = 0;
		for (String serverAddr : list) {
			startServer(serverAddr, classT, i);
			i++;
		}

		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					ActorReturnCheck.printCurRst();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread1.start();
	}

	public void startServer(String serverAddr, Class classT, int i) throws Exception {

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					AkkaClusterConfig clusterConfig = AkkaClusterConfig.buildAkkaClusterConfig(AkkaServerConfig.getIp(serverAddr),
							AkkaServerConfig.getPort(serverAddr));
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
