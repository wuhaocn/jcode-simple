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



	public String routeMessage(Object message, String sender) {
		final AkkaNode node = unicastInCluster(clusterConfig.getServerCluster(), message, actorSender.resolver(sender)).getKey();
		return node == null ? null : node.getName();
	}


	protected Entry<AkkaNode, CompletableFuture<Object>> unicastInCluster(String cluster, Object message, Messenger sender) {
		final AkkaNode node = AkkaNodeManager.getNode();
		final CompletableFuture<Object> future;
		if (node != null) {
			future = sender.send(node, cluster, message);

		} else {
			LOGGER.error("[cluster]Locator not found: appId={}, method={}, rid={}", 1, 1, 1);
			future = null;
		}
		return new SimpleEntry<>(node, future);
	}


	public void shutdown() {
		this.actorSystem.shutdown();
	}

}
