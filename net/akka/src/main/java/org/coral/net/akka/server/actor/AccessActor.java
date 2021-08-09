package org.coral.net.akka.server.actor;

import akka.actor.UntypedActorWithStash;
import org.coral.net.akka.AkkaCloudCluster;
import org.coral.net.akka.AkkaCloudManager;
import org.coral.net.akka.api.AppMessage;
import org.coral.net.akka.config.AccessServerList;
import org.coral.net.akka.server.ActorReturnCheck;

public class AccessActor extends UntypedActorWithStash {

	@Override
	public void preStart() throws Exception {
		System.out.println("AccessActor:preStart");

		super.preStart();
	}


	@Override
	public void onReceive(Object o) throws Exception {
		if (o instanceof AppMessage) {
			processAppMessage((AppMessage) o);
		}

	}

	public void processAppMessage(AppMessage appMessage) throws Exception {
		String serverAddress = this.context().system().provider().getDefaultAddress().toString();
		AkkaCloudCluster akkaCloudCluster = AkkaCloudManager.getAkkaCloudCluster(serverAddress);
		for (int i = 0; i < AccessServerList.getToAppNodeCount(); i++) {
			AppMessage appMessageTmp = AppMessage.copy(appMessage);
			appMessageTmp.setUuid(appMessageTmp.getUuid() + i);
			appMessageTmp.setTimeSign(System.currentTimeMillis());
			appMessageTmp.setAccessNode(serverAddress);
			akkaCloudCluster.routeMessage(appMessageTmp, "InnerActor");
			ActorReturnCheck.putSign(appMessageTmp.getUuid(), appMessageTmp);
		}
		getSender().tell(appMessage, getSelf());
	}
}
