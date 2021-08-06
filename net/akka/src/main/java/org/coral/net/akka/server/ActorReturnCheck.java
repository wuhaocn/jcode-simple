package org.coral.net.akka.server;

import scala.collection.mutable.LinkedHashMap;

/**
 * ActorReturnCheck
 *
 * @author wuhao
 * @createTime 2021-08-06 18:39:00
 */
public class ActorReturnCheck {

	public static LinkedHashMap linkedHashMap = new LinkedHashMap();

	/**
	 * @param key
	 * @param object
	 */
	public static void setResult(String key, Object object) {
		linkedHashMap.put(key, object);
	}

	/**
	 * @param key
	 * @return
	 */
	public static void getResult(String key, String ret) {
		try {

			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Object o = linkedHashMap.get(key);
		int ol = key.length();
		int rl = ret.length();

		if (!(ol == rl)) {
			throw new RuntimeException("getResult Time Out" + o);
		}
	}
}
