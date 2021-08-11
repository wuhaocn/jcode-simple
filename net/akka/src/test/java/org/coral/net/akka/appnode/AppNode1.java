package org.coral.net.akka.appnode;


import org.coral.net.akka.config.AkkaServerConfig;
import org.coral.net.akka.server.AkkaMultiServer;
import org.coral.net.akka.server.actor.AppNodeActor;

/**
 * @
 */
public class AppNode1 {
	public static void main(String[] args) throws Exception {
		AkkaMultiServer akkaAccessApp = new AkkaMultiServer();
		akkaAccessApp.startServers(AkkaServerConfig.getAppNodeList(), AppNodeActor.class);
	}

}
