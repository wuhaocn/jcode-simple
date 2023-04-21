package org.letter.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.ArrayList;
import java.util.List;

/**
 * ZookeeperClient
 *
 * @author wuhao
 * @createTime 2023-03-10
 */
public class ZookeeperClient {
	private static String clusterPath = "/quarantinecluster/";
	private static String clusterNamePre = "qutest";
	private static String data = "1,2,3,4,5,6,7,8,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25";
	private static String url = "10.41.0.185:2182";
	private static int time = 1000;

	public static void main(String[] args) throws Exception {
		ZookeeperClient zookeeperClient = new ZookeeperClient();
		Thread threadAdd = new Thread(() -> {
			for (int i = 0; i < 10000000; i++) {
				zookeeperClient.addPath(30, 30);

			}
		});
		Thread threadDel = new Thread(() -> {
			for (int i = 0; i < 10000000; i++) {
				zookeeperClient.delPath(30, 30);

			}
		});
		threadAdd.start();
//		threadDel.start();
		
	}

	private void addPath(int clusterSize, int appSize) {
		try {
			CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(url,
					new ExponentialBackoffRetry(1000, 3));
			curatorFramework.start();
			long time = System.currentTimeMillis();
			List<String> clusterNames = getTestCluster(clusterSize);
			for (int i = 0; i < clusterNames.size(); i++) {
				String parPath = clusterPath + clusterNames.get(i);
				try {
					Thread.sleep(100);
					curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(parPath, data.getBytes());
				} catch (Exception e) {

				}

				List<Long> clusterApps = getTestClusterApp(i, appSize);
				for (Long app : clusterApps) {
					try {
						Thread.sleep(100);
						curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(parPath + "/" + app);
					} catch (Exception e) {

					}

				}
				System.out.println("add Path" + (System.currentTimeMillis() - time));
			}
			System.out.println("add Path" + (System.currentTimeMillis() - time));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void delPath(int clusterSize, int appSize) {
		try {
			CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(url,
					new ExponentialBackoffRetry(1000, 3));
			curatorFramework.start();
			long time = System.currentTimeMillis();
			List<String> clusterNames = getTestCluster(clusterSize);
			for (int i = 15; i < clusterNames.size(); i++) {
				String parPath = clusterPath + clusterNames.get(i);
				List<Long> clusterApps = getTestClusterApp(i, appSize);
				for (Long app : clusterApps) {
					try {
						Thread.sleep(500);
						curatorFramework.delete().forPath(parPath + "/" + app);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				try {
					//Thread.sleep(time);
					curatorFramework.delete().forPath(parPath);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("delete Path" + (System.currentTimeMillis() - time));
			}
			System.out.println("delete Path" + (System.currentTimeMillis() - time));
		} catch (Exception e) {
			e.printStackTrace();
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
