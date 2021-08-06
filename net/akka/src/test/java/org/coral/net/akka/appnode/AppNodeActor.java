package org.coral.net.akka.appnode;

import akka.actor.UntypedActorWithStash;

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
		System.out.println("AppNodeActor:onReceive:Object " + o);
		getSender().tell(o, getSelf());
		System.out.println("AppNodeActor:Return:Object " + o);

    }
}
