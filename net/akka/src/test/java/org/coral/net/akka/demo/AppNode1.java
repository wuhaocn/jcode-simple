package org.coral.net.akka.demo;


import org.coral.net.akka.access.AkkaAccessApp;
import org.coral.net.akka.config.AccessServerList;

/**
 * @
 */
public class AppNode1 {
	public static void main(String[] args) throws Exception {
		AkkaAccessApp akkaAccessApp = new AkkaAccessApp();
		akkaAccessApp.startServers(AccessServerList.getAppNodeList());
	}

}
