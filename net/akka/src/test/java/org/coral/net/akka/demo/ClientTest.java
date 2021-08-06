package org.coral.net.akka.demo;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.coral.net.akka.config.AccessServerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class ClientTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientTest.class);

	public static void main(String[] args) throws Exception {
		ClientTest akkaClientTest = new ClientTest();
		akkaClientTest.testServer();
	}

	public void testServer() throws Exception {
		int localPort = 20000;
		List<String> serverList = AccessServerList.getAllServerList();
		for (String addr : serverList) {
			localPort++;
			String dstIP = AccessServerList.getIp(addr);
			String dstPort = AccessServerList.getPort(addr);
			send(localPort, dstIP, dstPort);
		}
	}

	public void send(int localPort, String dstIp, String dstPort) throws Exception {

		Thread thread = new Thread(new Runnable() {
			ActorSystem system = createAkka("client" + localPort, localPort);
			@Override
			public void run() {
				while (true){
					try {
						String remoteAddress = "akka.tcp://cluster@" + dstIp + ":" + dstPort + "/user/AccessActor";
						String uuid = UUID.randomUUID().toString();
						System.out.println("init " + remoteAddress + " uuid:" + uuid);
						ActorSelection selection = system.actorSelection(remoteAddress);
						Future<Object> f = Patterns.ask(selection, uuid, 5000);
						System.out.println("uuid: " + uuid + " start");
						Object result = Await.result(f, Duration.create(5000, TimeUnit.MILLISECONDS));
						System.out.println("uuid: " + uuid + " end: " + result);
						Thread.sleep(1500);
						//system.terminate();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});
		thread.start();

	}


	public static ActorSystem createAkka(String name, int port) {
		Config config = ConfigFactory.defaultReference();
		if (port >= 0) {
			config = ConfigFactory.parseMap(new HashMap<String, Object>() {{
				put("akka.actor.provider", "akka.remote.RemoteActorRefProvider");
				put("akka.remote.netty.tcp.hostname", "127.0.0.1");
				put("akka.remote.netty.tcp.port", port);
			}}).withFallback(config);
		}
		return ActorSystem.create(name, config);
	}
}
