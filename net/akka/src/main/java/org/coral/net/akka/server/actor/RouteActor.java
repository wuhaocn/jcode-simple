package org.coral.net.akka.server.actor;

import akka.actor.UntypedActorWithStash;
import org.coral.net.akka.AkkaCloudCluster;
import org.coral.net.akka.AkkaCloudManager;
import org.coral.net.akka.api.AppMessage;
import org.coral.net.akka.config.AkkaServerConfig;
import org.coral.net.akka.server.ActorReturnCheck;

public class RouteActor extends UntypedActorWithStash {

	@Override
	public void preStart() throws Exception {
		System.out.println("RouteActor:preStart");

		super.preStart();
	}


	@Override
	public void onReceive(Object o) throws Exception {
		//System.out.println("RouteActor:Object " + o);
		if (o instanceof AppMessage) {
			processAppMessage((AppMessage) o);
		}

	}

	public void processAppMessage(AppMessage appMessage) throws Exception {

		String serverAddress = this.context().system().provider().getDefaultAddress().toString();
		AkkaCloudCluster akkaCloudCluster = AkkaCloudManager.getAkkaCloudCluster(serverAddress);
		appMessage.setMethod("AppNodeActor");
		akkaCloudCluster.routeMessage(appMessage, getSender());
	}
}
