package org.coral.net.akka.server.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActorWithStash;
import org.coral.net.akka.api.AppMessage;

import java.util.Random;

public class AppNodeActor extends UntypedActorWithStash {



    @Override
    public void preStart() throws Exception {
		String serverAddress = this.context().system().provider().getDefaultAddress().toString();
        System.out.println("AppNodeActor:preStart" + serverAddress);
		System.out.println("AppNodeActor:preStart" + this.context().system().provider().rootPath());
        super.preStart();
    }

    @Override
    public void onReceive(Object o) throws Exception {
    	//System.out.println("AppNodeActor:onReceive:Object " + o);
//		Random random = new Random();
//		int i = random.nextInt(10);
//		Thread.sleep(i);
		if (o instanceof AppMessage){
			AppMessage appMessage = (AppMessage) o;
			appMessage.setMessage("12345678901234567890");
			getSender().tell(o, ActorRef.noSender());
		}

		//System.out.println("AppNodeActor:Return:Object " + o);
    }

	public ActorSelection getNewSender(){
		ActorRef actorRef = super.getSender();
		ActorSelection actorSelection = this.context().actorSelection(actorRef.path());
		return actorSelection;
	}
}
