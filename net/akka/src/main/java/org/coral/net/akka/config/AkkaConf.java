package org.coral.net.akka.config;


import java.util.HashMap;
import java.util.Map;

/**
 * @author wuhao
 * @createTime 2021-08-05 11:44:00
 */
public class AkkaConf {

	public static Map<String, Object> getConfig(String host, String port) {
		final Map<String, Object> conf = new HashMap<>();
//		conf.put("akka.remote.netty.tcp.maximum-frame-size", 10485760);
//		conf.put("akka.actor.default-dispatcher.thread-pool-executor.core-pool-size-factor", 16.0);
//		conf.put("akka.actor.default-dispatcher.thread-pool-executor.max-pool-size-factor", 16.0);
//		conf.put("akka.actor.default-dispatcher.thread-pool-executor.core-pool-size-max", 128);
//		conf.put("akka.actor.default-dispatcher.thread-pool-executor.max-pool-size-max", 128);
		conf.put("akka.actor.provider", "akka.remote.RemoteActorRefProvider");
		conf.put("akka.remote.netty.tcp.bind-hostname", "0.0.0.0");
		conf.put("akka.remote.netty.tcp.bind-port", Integer.parseInt(port));
		conf.put("akka.remote.netty.tcp.hostname", host);
		conf.put("akka.remote.netty.tcp.port", Integer.parseInt(port));
		return conf;
	}
}
