package org.coral.net.akka.access;

import org.coral.net.akka.server.AkkaMultiServer;
import org.coral.net.akka.config.AccessServerList;

/**
 * @author wuhao
 * @createTime 2021-08-05 19:02:00
 */
public class AccessNode3 {
	public static void main(String[] args) throws Exception {
		AkkaMultiServer akkaAccessApp = new AkkaMultiServer();
		akkaAccessApp.startServers(AccessServerList.getServer3List(), AccessActor.class);
	}
}