package org.coral.net.akka.server.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorWithStash;
import org.coral.net.akka.AkkaCloudCluster;
import org.coral.net.akka.AkkaCloudManager;
import org.coral.net.akka.api.AppMessage;
import org.coral.net.akka.config.AkkaServerConfig;
import org.coral.net.akka.server.ActorReturnCheck;

public class AccessDynamicActor extends UntypedActor {

	@Override
	public void preStart() throws Exception {
		System.out.println("AccessDynamicActor:onReceive");

		super.preStart();
	}


	@Override
	public void onReceive(Object o) throws Exception {
		System.out.println("AccessDynamicActor:onReceive " + o);

	}

	public static void processAppMessage(AkkaCloudCluster akkaCloudCluster, AppMessage appMessage) throws Exception {
		final ActorRef askSender = akkaCloudCluster.getActorSystem().actorOf(Props
				.create(AccessDynamicActor.class), appMessage.getUuid()); // 处理qosFlag的Actor

		akkaCloudCluster.routeMessage(appMessage, askSender);
	}
}
