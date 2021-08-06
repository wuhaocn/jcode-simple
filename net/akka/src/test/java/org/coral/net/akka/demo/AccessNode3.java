package org.coral.net.akka.demo;

import org.coral.net.akka.access.AkkaAccessApp;
import org.coral.net.akka.config.AccessServerList;

/**
 * @author wuhao
 * @createTime 2021-08-05 19:02:00
 */
public class AccessNode3 {
	public static void main(String[] args) throws Exception {
		AkkaAccessApp akkaAccessApp = new AkkaAccessApp();
		akkaAccessApp.startServers(AccessServerList.getServer3List());
	}
}
