package org.coral.net.akka.server;

import org.coral.net.akka.api.AppMessage;
import org.coral.net.akka.config.AkkaServerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * AkkaNodeManager
 *
 * @author wuhao
 * @createTime 2021-08-05 17:19:00
 */
public class AkkaNodeManager {
	public static List<AkkaNode> akkaNodeList = new ArrayList<>();
	public static List<AkkaNode> akkaNodeRouteList = new ArrayList<>();

	static {
		for (String addr: AkkaServerConfig.getAppNodeList()){
			AkkaNode akkaNode = new AkkaNode();
			akkaNode.setAkkaAddr(addr);
			akkaNodeList.add(akkaNode);
		}
		for (String addr: AkkaServerConfig.getRouteNodeList()){
			AkkaNode akkaNode = new AkkaNode();
			akkaNode.setAkkaAddr(addr);
			akkaNodeRouteList.add(akkaNode);
		}
	}


	/**
	 * getNode
	 *
	 * @return
	 */
	public static AkkaNode getNode(AppMessage appMessage) {
		if (appMessage.getMethod().equalsIgnoreCase("RouteActor")){
			return akkaNodeRouteList.get(appMessage.getSeq());
		}
		return akkaNodeList.get(appMessage.getSeq());
	}

}
