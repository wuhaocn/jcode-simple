package org.coral.net.akka.api;


import org.coral.net.akka.server.AkkaNode;

import java.util.concurrent.CompletableFuture;

public interface Messenger {
	/**
	 * send
	 *
	 * @param node
	 * @param cluster
	 * @param msg
	 * @return
	 */
    CompletableFuture<Object> send(AkkaNode node, String cluster, IInnerMessage msg);
}
