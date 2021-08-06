package org.coral.net.akka.server;

import org.coral.net.akka.config.AccessServerList;
import org.coral.net.akka.config.AkkaClusterConfig;

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
	public static AkkaNode getNode() {
		Random random = new Random();
		int i = random.nextInt(akkaNodeList.size());
		return akkaNodeList.get(i);
	}

}
