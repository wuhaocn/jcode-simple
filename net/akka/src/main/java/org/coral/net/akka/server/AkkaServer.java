package org.coral.net.akka.server;

import akka.actor.*;
import akka.remote.RemoteScope;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.coral.net.akka.config.AkkaConf;
import org.coral.net.akka.config.AkkaProfiler;
import org.coral.net.akka.server.actor.InnerActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

/**
 * AkkaServer
 *
 * @author wuhao
 */
public class AkkaServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(AkkaServer.class);


	private Address akkaAddress;

	private ActorSystem akkaSystem;


	public ActorSystem akkaSystem() {
		return this.akkaSystem;
	}

	public void start(String cluster, String host, String port, Class classT) throws Exception {

		final Config conf = ConfigFactory.parseMap(AkkaConf.getConfig(host, port))
				.withFallback(ConfigFactory.load());
		final String endpoint = StringUtils.isAllBlank(host, port)
				? String.format("akka://%s", cluster)
				: String.format("akka.tcp://%s@%s:%s", cluster, host, port);
		this.akkaAddress = AddressFromURIString.parse(endpoint);
		this.akkaSystem = AkkaProfiler.createSystem(cluster, conf);
		this.akkaSystem.actorOf(Props.create(classT).withDeploy(new Deploy(new RemoteScope(this.akkaAddress))), classT.getSimpleName());
		this.akkaSystem.actorOf(Props.create(InnerActor.class).withDeploy(new Deploy(new RemoteScope(this.akkaAddress))), InnerActor.class.getSimpleName());
		//this.akkaSystem.actorOf(Props.create(classT), classT.getSimpleName());
		LOGGER.info("start Avatar actor is inited with config : {}", akkaAddress);
	}

	public void shutdown() {
		if (this.akkaSystem != null) {
			try {
				Await.result(this.akkaSystem.terminate(), Duration.Inf());
			} catch (Exception e) {
				LOGGER.error("error shutting down akka", e);
			}
		}
	}

}
