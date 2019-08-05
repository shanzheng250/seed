package com.shanz.config;

/**
 * @ClassName:ZkConfig
 * @Description: zookeeper 配置类
 * @Author: shanz
 * @Date: 2019/6/21 11:07
 * @Version:1.0
 **/
public class ZKConfig {

    public static final int ZK_MAX_RETRY = 3;
    public static final int ZK_MIN_TIME = 5000;
    public static final int ZK_MAX_TIME = 5000;
    public static final int ZK_SESSION_TIMEOUT = 5000;
    public static final int ZK_CONNECTION_TIMEOUT = 5000;
    public static final String ZK_DEFAULT_CACHE_PATH = "/";

    private String hosts;   // zk地址 可配置为集群，用逗号分隔

    private String digest;

    private String namespace;   //业务隔离

    private int maxRetries = ZK_MAX_RETRY;  // 最大重试次数

    private int baseSleepTimeMs = ZK_MIN_TIME;      // 初始休眠时间

    private int maxSleepMs = ZK_MAX_TIME;       // 最大休眠时间

    private int sessionTimeout = ZK_SESSION_TIMEOUT;

    private int connectionTimeout = ZK_CONNECTION_TIMEOUT;

    private String watchPath = ZK_DEFAULT_CACHE_PATH;


    public ZKConfig(String hosts) {
        this.hosts = hosts;
    }

    public ZKConfig build() {
        return new ZKConfig(hosts)
                .setConnectionTimeout(connectionTimeout)
                .setDigest(digest)
                .setWatchPath(watchPath)
                .setMaxRetries(maxRetries)
                .setMaxSleepMs(maxSleepMs)
                .setBaseSleepTimeMs(baseSleepTimeMs)
                .setNamespace(namespace)
                .setSessionTimeout(sessionTimeout)
                ;
    }


    public String getHosts() {
        return hosts;
    }

    public ZKConfig setHosts(String hosts) {
        this.hosts = hosts;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public ZKConfig setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public ZKConfig setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public ZKConfig setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
        return this;
    }

    public int getMaxSleepMs() {
        return maxSleepMs;
    }

    public ZKConfig setMaxSleepMs(int maxSleepMs) {
        this.maxSleepMs = maxSleepMs;
        return this;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public ZKConfig setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
        return this;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public ZKConfig setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public String getDigest() {
        return digest;
    }

    public ZKConfig setDigest(String digest) {
        this.digest = digest;
        return this;
    }

    public String getWatchPath() {
        return watchPath;
    }

    public ZKConfig setWatchPath(String watchPath) {
        this.watchPath = watchPath;
        return this;
    }

    @Override
    public String toString() {
        return "ZKConfig{" +
                "hosts='" + hosts + '\'' +
                ", digest='" + digest + '\'' +
                ", namespace='" + namespace + '\'' +
                ", maxRetries=" + maxRetries +
                ", baseSleepTimeMs=" + baseSleepTimeMs +
                ", maxSleepMs=" + maxSleepMs +
                ", sessionTimeout=" + sessionTimeout +
                ", connectionTimeout=" + connectionTimeout +
                ", watchPath='" + watchPath + '\'' +
                '}';
    }
}
