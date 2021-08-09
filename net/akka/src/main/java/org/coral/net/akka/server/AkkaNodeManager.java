package org.coral.net.akka.server;

import org.coral.net.akka.api.AppMessage;
import org.coral.net.akka.config.AccessServerList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * AkkaNodeManager
 *
 * @author wuhao
 * @createTime 2021-08-05 17:19:00
 */
public class AkkaNodeManager {
	public static List<AkkaNode> akkaNodeList = new ArrayList<>();

	static {
		for (String addr: AccessServerList.getAppNodeList()){
			AkkaNode akkaNode = new AkkaNode();
			akkaNode.setAkkaAddr(addr);
			akkaNodeList.add(akkaNode);
		}
	}


	/**
	 * getNode
	 *
	 * @return
	 */
	public static AkkaNode getNode(AppMessage appMessage) {
		return akkaNodeList.get(appMessage.getSeq());
	}

}
