package org.coral.net.akka.access;

import org.coral.net.akka.config.AkkaServerConfig;
import org.coral.net.akka.server.AkkaMultiServer;
import org.coral.net.akka.server.actor.AccessActor;

/**
 * @author wuhao
 * @createTime 2021-08-05 19:02:00
 */
public class AccessNode2 {
	public static void main(String[] args) throws Exception {
		AkkaMultiServer akkaAccessApp = new AkkaMultiServer();
		akkaAccessApp.startServers(AkkaServerConfig.getServer2List(), AccessActor.class);
	}
}
