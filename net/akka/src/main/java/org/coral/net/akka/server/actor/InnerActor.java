package org.coral.net.akka.server.actor;

import akka.actor.UntypedActorWithStash;
import org.coral.net.akka.api.AppMessage;
import org.coral.net.akka.server.ActorReturnCheck;

public class InnerActor extends UntypedActorWithStash {


	@Override
	public void preStart() throws Exception {
		System.out.println("InnerActor :preStart");
		super.preStart();
	}

	@Override
	public void onReceive(Object o) throws Exception {
		if (o instanceof AppMessage) {
			processAppMessage((AppMessage) o);
		}

	}

	public void processAppMessage(AppMessage appMessage) throws Exception {
		ActorReturnCheck.removeSign(appMessage.getUuid());
	}
}
