package org.coral.net.akka.route;

import org.coral.net.akka.config.AkkaServerConfig;
import org.coral.net.akka.server.AkkaMultiServer;
import org.coral.net.akka.server.actor.RouteActor;

/**
 * @author wuhao
 * @createTime 2021-08-13 15:02:00
 */
public class RouteNode1 {
	public static void main(String[] args) throws Exception {
		AkkaMultiServer akkaAccessApp = new AkkaMultiServer();
		akkaAccessApp.startServers(AkkaServerConfig.getRouteNodeList(), RouteActor.class);
	}

}
