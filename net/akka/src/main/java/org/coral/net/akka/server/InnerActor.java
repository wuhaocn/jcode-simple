package org.coral.net.akka.server;

import akka.actor.UntypedActorWithStash;
import org.coral.net.akka.AkkaCloudCluster;
import org.coral.net.akka.AkkaCloudManager;
import org.coral.net.akka.api.InnerMessageDefault;

public class InnerActor extends UntypedActorWithStash {


	@Override
	public void preStart() throws Exception {
		System.out.println("InnerActor :preStart");
		super.preStart();
	}

	@Override
	public void onReceive(Object o) throws Exception {
		System.out.println("InnerActor:onReceive:Object " + o);
		ActorReturnCheck.setResult(o.toString(), o);
	}
}
