package com.shanz.config;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName:RedisConfig
 * @Description: redis 配置
 * @Author: shanz
 * @Date: 2019/6/19 9:44
 * @Version:1.0
 **/
public class RedisConfig {

    public static final int MAXACTIVE = 1024;
    public static final int MAXIDLE = 200;
    public static final Long MAXWAIT = 5000L;
    public static final boolean TESTONBORROW = true;
    public static final boolean TESTONRETURN = true;
    public static final int SOCKET_TIMEOUT = 5000;

    private  String password;

    private  String clusterModel;

    private  String sentinelMaster;

    public boolean isCluster() {
        return "cluster".equals(clusterModel);
    }

    public boolean isSentinel() {
        return "sentinel".equals(clusterModel);
    }


    public JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(5000);
        config.setMaxIdle(MAXIDLE);
        config.setMaxWaitMillis(MAXWAIT);
        config.setTestOnBorrow(TESTONBORROW);
        config.setTestOnReturn(TESTONRETURN);
        return config;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClusterModel() {
        return clusterModel;
    }

    public void setClusterModel(String clusterModel) {
        this.clusterModel = clusterModel;
    }

    public String getSentinelMaster() {
        return sentinelMaster;
    }

    public void setSentinelMaster(String sentinelMaster) {
        this.sentinelMaster = sentinelMaster;
    }
}
