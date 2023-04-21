package org.letter.zookeeper;

import com.google.common.collect.Sets;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ZookeeperWatcherClient
 *
 * @author wuhao
 */
public class ZookeeperWatcherClient {
	private static String clusterPath = "/quarantinecluster";
	private static String clusterNamePre = "qutest";
	private static String data = "1,2,3,4,5,6,7,8,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25";
	private static String url = "10.41.0.185:2182";
	private static int time = 1000;

	public static void main(String[] args) throws Exception {
		ZookeeperWatcherClient zookeeperClient = new ZookeeperWatcherClient();
		for (int i = 0; i < 350; i++) {
			int finalI = i;
			Thread threadWatcher = new Thread(() -> {
				zookeeperClient.watchPath();
				System.out.println("-----------------------" + finalI);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			});
			threadWatcher.start();
		}

		Thread.sleep(60 * 60 * 24 * 1000 );

	}

	private void watchPath() {
		try {
			CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(url,
					new ExponentialBackoffRetry(1000, 3));
			curatorFramework.start();
			Watcher watcher = new WatcherImpl(curatorFramework);
			watchMembers(curatorFramework, watcher);
			System.out.println("watchPath" + (System.currentTimeMillis() - time));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void watchMembers(CuratorFramework client, Watcher watcherOfMembers) {
		try {
			Set<String> groups = Sets.newHashSet(client.getChildren().
					usingWatcher(watcherOfMembers).forPath(clusterPath));
			ConcurrentHashMap<Long, Set<String>> tmp = new ConcurrentHashMap<>();
			System.out.println("zk cex  do watchMembers: groups:{}" + groups.size());
			for (String member : groups) {
				List<String> appIds = client.getChildren().
						usingWatcher(watcherOfMembers).forPath(clusterPath + "/" + member );
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	static class WatcherImpl implements Watcher {
		CuratorFramework curatorFramework;
		public WatcherImpl(CuratorFramework curatorFramework) {
			this.curatorFramework = curatorFramework;
		}

		public CuratorFramework getCuratorFramework() {
			return curatorFramework;
		}

		public void setCuratorFramework(CuratorFramework curatorFramework) {
			this.curatorFramework = curatorFramework;
		}

		@Override
		public void process(WatchedEvent event) {
			try {
				watchMembers(curatorFramework, this);
			} catch (Exception e) {
				System.out.println("Error processing members event: " + event);
			}
		}
	}

	public List<String> getTestCluster(int size) {
		List<String> testClusters = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			testClusters.add(clusterNamePre + i);
		}
		return testClusters;
	}

	public List<Long> getTestClusterApp(int base, int size) {
		long baseNumber = 1000000;
		List<Long> testClusters = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			testClusters.add(baseNumber * base + i);
		}
		return testClusters;
	}
}
