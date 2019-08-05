package com.shanz.api.server;

/**
 * @ClassName:ResigerType
 * @Description: 服务注册中心类型
 * @Author: shanz
 * @Date: 2019/6/22 9:06
 * @Version:1.0
 **/
public enum ServiceResigerType {
    ZK("zk"),
    REDIS("redis"),
    EUREKA("eureka"),
    NACOS("nacos");

    private String type;

    ServiceResigerType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
