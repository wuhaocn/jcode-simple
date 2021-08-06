package org.coral.net.akka.access;

import org.coral.net.akka.config.AccessServerList;
import org.coral.net.akka.server.AkkaMultiServer;

/**
 * @author wuhao
 * @createTime 2021-08-05 19:02:00
 */
public class AccessNode4 {
	public static void main(String[] args) throws Exception {
		AkkaMultiServer akkaAccessApp = new AkkaMultiServer();
		akkaAccessApp.startServers(AccessServerList.getServer4List(), AccessActor.class);
	}
}
