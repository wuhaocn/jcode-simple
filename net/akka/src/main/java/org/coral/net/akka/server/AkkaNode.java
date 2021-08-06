package org.coral.net.akka.server;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class AkkaNode {

    private String version = "0";

    private String name = StringUtils.EMPTY;
    private String akkaAddr = StringUtils.EMPTY;
    private String site = StringUtils.EMPTY;
    private String cluster = StringUtils.EMPTY;

    private Set<String> methods = Sets.newHashSet();
    private Set<String> roles = Sets.newHashSet();

    private float weight = 1;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? StringUtils.EMPTY : name;
    }

    public String getAkkaAddr() {
        return akkaAddr;
    }

    public void setAkkaAddr(String akkaAddr) {
        this.akkaAddr = akkaAddr == null ? StringUtils.EMPTY : akkaAddr;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site == null ? StringUtils.EMPTY : site;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster == null ? StringUtils.EMPTY : cluster;
    }

    public Set<String> getMethods() {
        return methods;
    }

    public void setMethods(Set<String> methods) {
        this.methods = methods == null ? Sets.newHashSet() : methods;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles == null ? Sets.newHashSet() : roles;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
