package org.coral.net.akka.server;

import org.coral.net.akka.api.AppMessage;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ActorReturnCheck
 *
 * @author wuhao
 * @createTime 2021-08-06 18:39:00
 */
public class ActorReturnCheck {

	public static LinkedHashMap<String, AppMessage> linkedHashMap = new LinkedHashMap();

	/**
	 * @param key
	 * @param appMessage
	 */
	public static synchronized void putSign(String key, AppMessage appMessage) {
		linkedHashMap.put(key, appMessage);
	}

	/**
	 * 结果返回 remove
	 *
	 * @param key
	 * @return
	 */
	public static synchronized void removeSign(String key) {
		linkedHashMap.remove(key);
	}

	public static synchronized void printCurRst(){

		Iterator<Map.Entry<String, AppMessage>> entries = linkedHashMap.entrySet().iterator();
		long time = System.currentTimeMillis();
		while (entries.hasNext()) {
			Map.Entry<String, AppMessage> entry = entries.next();
			AppMessage appMessage = entry.getValue();
			long cost = System.currentTimeMillis() - appMessage.getTimeSign();
			if (cost > 50000){
				System.out.println(time + "----" + entry.getValue());
			}
		}
	}
}
