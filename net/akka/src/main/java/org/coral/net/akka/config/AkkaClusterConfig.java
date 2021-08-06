package org.coral.net.akka.config;

/**
 * AkkaClusterConfig
 *
 * @author wuhao
 * @createTime 2021-08-05 14:51:00
 */
public class AkkaClusterConfig {
	public String nodeName = "anode";
	public String serverCluster = "cluster";
	public String clusterSite = "site";
	public String nodeIp = "127.0.0.1";
	public String nodePort = "2000";

	public static AkkaClusterConfig buildAkkaClusterConfig(String nodeIp, String nodePort) {
		return new AkkaClusterConfig(nodeIp, nodePort);
	}

	public AkkaClusterConfig() {

	}

	public AkkaClusterConfig(String nodeIp, String nodePort) {
		this.nodeIp = nodeIp;
		this.nodePort = nodePort;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getServerCluster() {
		return serverCluster;
	}

	public void setServerCluster(String serverCluster) {
		this.serverCluster = serverCluster;
	}

	public String getClusterSite() {
		return clusterSite;
	}

	public void setClusterSite(String clusterSite) {
		this.clusterSite = clusterSite;
	}

	public String getNodeIp() {
		return nodeIp;
	}

	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}

	public String getNodePort() {
		return nodePort;
	}

	public void setNodePort(String nodePort) {
		this.nodePort = nodePort;
	}
}
