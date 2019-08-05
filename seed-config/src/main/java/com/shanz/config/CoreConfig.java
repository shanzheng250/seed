package com.shanz.config;

/**
 * @ClassName:CoreConfig
 * @Description: 系统核心配置
 * @Author: shanzheng
 * @Date: 2019/7/10 9:26
 * @Version:1.0
 **/
public class CoreConfig {

    private int minHeartbeat = 3 * 60;  // 最小心跳间隔s

    private int maxHeartbeat = 3 * 60;  // 最大心跳间隔s

    private int heartbeatTimeOutTimes = 3;  // 心跳超时最大次数

    private int sessionExpiredTime = 86400;  // session重连过期时间


    public static CoreConfig buildConfig(){
        return new CoreConfig()
                .setMaxHeartbeat(3*60 * 1000)
                .setHeartbeatTimeOutTimes(3)
                .setMinHeartbeat(3*60*1000)
                .setSessionExpiredTime(86400);
    }


    public int getMinHeartbeat() {
        return minHeartbeat;
    }

    public CoreConfig setMinHeartbeat(int minHeartbeat) {
        this.minHeartbeat = minHeartbeat;
        return this;
    }

    public int getMaxHeartbeat() {
        return maxHeartbeat;
    }

    public CoreConfig setMaxHeartbeat(int maxHeartbeat) {
        this.maxHeartbeat = maxHeartbeat;
        return this;
    }

    public int getHeartbeatTimeOutTimes() {
        return heartbeatTimeOutTimes;
    }

    public CoreConfig setHeartbeatTimeOutTimes(int heartbeatTimeOutTimes) {
        this.heartbeatTimeOutTimes = heartbeatTimeOutTimes;
        return this;
    }

    public int getSessionExpiredTime() {
        return sessionExpiredTime;
    }

    public CoreConfig setSessionExpiredTime(int sessionExpiredTime) {
        this.sessionExpiredTime = sessionExpiredTime;
        return this;
    }
}
