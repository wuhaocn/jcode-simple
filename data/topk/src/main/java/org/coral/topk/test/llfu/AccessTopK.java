package org.coral.topk.test.llfu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * AccessTop
 *
 * @author wuhao
 * @createTime 2022-02-10 16:13:00
 */
public class AccessTopK {
	private ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1);
	private ThreadPoolExecutor reportPool = new ThreadPoolExecutor(1, 1,
			0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

	private LFUCache lfuCache = new LFUCache(10000);

	private AccessQueue accessQueue = new AccessQueue();

	public void init() {
		scheduledPool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					accessQueue.clean();
					System.out.println("-----------");
					List<LFUCacheNode> nodeList = getTopK();
					for (LFUCacheNode entry : nodeList) {
						System.out.println(entry);
					}
					System.out.println("-----------");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, 1, 2, TimeUnit.SECONDS);
		reportPool.execute(new Runnable() {
			@Override
			public void run() {

				while (true) {
					try {
						String key = accessQueue.poll();
						if (key == null) {
							Thread.sleep(500);
						} else {
							lfuCache.put(key, null);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});

	}


	public void access(String key) {
		lfuCache.put(key, null);
	}

	public void accessAsyn(String key) {
		accessQueue.access(key);
	}

	public List<LFUCacheNode> getTopK() {
		Map<Integer, LFUCacheNode> map = lfuCache.report();
		List<LFUCacheNode> list = new ArrayList<>();
		for (Map.Entry<Integer, LFUCacheNode> entry : map.entrySet()) {
			list.add(entry.getValue());
		}
		list.sort(new Comparator<LFUCacheNode>() {
			@Override
			public int compare(LFUCacheNode o1, LFUCacheNode o2) {
				return o2.getFrequency() - o1.getFrequency();
			}
		});
		return list;
	}


}
