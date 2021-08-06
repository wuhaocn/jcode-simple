package org.coral.net.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.coral.net.akka.api.IRoute;
import org.coral.net.akka.api.Messenger;
import org.coral.net.akka.config.AkkaClusterConfig;
import org.coral.net.akka.server.AkkaNode;
import org.coral.net.akka.server.AkkaNodeManager;
import org.coral.net.akka.server.AkkaSender;
import org.coral.net.akka.server.AkkaServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class AkkaCloudCluster {

	private static final Logger LOGGER = LoggerFactory.getLogger(AkkaCloudCluster.class);


	private AkkaServer actorSystem;
	private AkkaSender actorSender;
	private AkkaClusterConfig clusterConfig;

	public AkkaCloudCluster() {
		this.actorSystem = new AkkaServer();
	}


	public ActorSystem getActorSystem() {
		return this.actorSystem.akkaSystem();
	}

	private final AtomicBoolean isInitCluster = new AtomicBoolean(false);

	public void initClusters(AkkaClusterConfig clusterConfig, Class classT) throws Exception {
		if (isInitCluster.compareAndSet(false, true)) {
			this.actorSystem.start(clusterConfig.getServerCluster(),
					clusterConfig.getNodeIp(), clusterConfig.getNodePort(), classT);
			this.actorSender = new AkkaSender(this.actorSystem.akkaSystem());
			this.clusterConfig = clusterConfig;
			String serverAddress = this.actorSystem.akkaSystem().provider().getDefaultAddress().toString();
			AkkaCloudManager.putAkkaCloudCluster(serverAddress, this);
		}
	}

	public String routeMessage(IRoute message) {
		return routeMessage(message, ActorRef.noSender());
	}

	public String routeMessage(IRoute message, ActorRef sender) {
		final AkkaNode node = unicastInCluster(clusterConfig.getServerCluster(), message, actorSender.teller(sender)).getKey();
		if (node == null) {
			return null;
		}
		return node.getName() + "@" + node.getCluster() + "@" + node.getSite();
	}


	public String routeMessage(IRoute message, String sender) {
		final AkkaNode node = unicastInCluster(
				clusterConfig.getServerCluster(), message, actorSender.resolver(sender)).getKey();
		return node == null ? null : node.getName();
	}


	/**
	 * 在可用 Site 范围内进行单点发送
	 *
	 * @param cluster 在目标集群内进行路由 私有云默认忽略,使用当前的clustername
	 * @param message 影响发送范围的属性：appId + method + targetResourceId
	 * @param sender  发送方式
	 * @return 目标节点: Promise
	 */
	protected Entry<AkkaNode, CompletableFuture<Object>> unicastInCluster(
			String cluster, IRoute message, Messenger sender) {
		final AkkaNode node = AkkaNodeManager.getNode();
		final CompletableFuture<Object> future;
		if (node != null) {
			future = sender.send(node, cluster, message.getRouter());
			LOGGER.debug("[cluster]RouteMessage[appId={}, method={}, rid={}] is sent to: {}",
					message.getAppId(), message.getMethod(), message.getTargetResourceId(),
					node.getAkkaAddr());
		} else {
			LOGGER.error("[cluster]Locator not found: appId={}, method={}, rid={}",
					message.getAppId(), message.getMethod(), message.getTargetResourceId());
			future = null;
		}
		return new SimpleEntry<>(node, future);
	}


	public void shutdown() {
		this.actorSystem.shutdown();
	}

}
