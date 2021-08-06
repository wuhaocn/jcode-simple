package org.coral.net.akka.access;

import akka.actor.UntypedActorWithStash;
import org.coral.net.akka.AkkaCloudCluster;
import org.coral.net.akka.AkkaCloudManager;
import org.coral.net.akka.api.IInnerMessage;
import org.coral.net.akka.api.IRoute;
import org.coral.net.akka.api.InnerMessageDefault;

public class AccessActor extends UntypedActorWithStash {


	@Override
	public void preStart() throws Exception {
		System.out.println("AccessActor:preStart");
		super.preStart();
	}

	@Override
	public void onReceive(Object o) throws Exception {
		System.out.println("AccessActor:onReceive:Object " + o);
//		AkkaCloudCluster.fCluster.routeMessage(RouteMessage.wrap(request), "default");
		String serverAddress = this.context().system().provider().getDefaultAddress().toString();
		AkkaCloudCluster akkaCloudCluster = AkkaCloudManager.getAkkaCloudCluster(serverAddress);
		akkaCloudCluster.routeMessage(new InnerMessageDefault(), "AppNodeActor");
		getSender().tell(o, getSelf());
		System.out.println("AccessActor:Return:Object " + o);

	}
}
