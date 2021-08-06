package org.coral.net.akka.access;

import akka.actor.UntypedActorWithStash;
import org.coral.net.akka.AkkaCloudCluster;
import org.coral.net.akka.AkkaCloudManager;
import org.coral.net.akka.api.InnerMessageDefault;
import org.coral.net.akka.server.ActorReturnCheck;

public class AccessActor extends UntypedActorWithStash {


	@Override
	public void preStart() throws Exception {
		System.out.println("AccessActor:preStart");
		super.preStart();
	}

	@Override
	public void onReceive(Object o) throws Exception {
		System.out.println("AccessActor:onReceive:Object " + o);
		String serverAddress = this.context().system().provider().getDefaultAddress().toString();
		AkkaCloudCluster akkaCloudCluster = AkkaCloudManager.getAkkaCloudCluster(serverAddress);
		akkaCloudCluster.routeMessage(o, "InnerActor");
		ActorReturnCheck.getResult(o.toString(), o.toString());
		getSender().tell(o, getSelf());
		System.out.println("AccessActor:Return:Object " + o);

	}
}
