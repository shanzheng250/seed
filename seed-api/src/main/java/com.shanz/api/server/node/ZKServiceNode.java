package com.shanz.api.server.node;

import com.shanz.api.server.ServiceResigerType;

/**
 * @ClassName:ZKServiceNode
 * @Description: 注册中心为zk时的服务节点
 * @Author: shanz
 * @Date: 2019/6/22 9:04
 * @Version:1.0
 **/
public class ZKServiceNode extends ServiceNode {

    private transient boolean persistent;

    private String nodePath;

    public String getServiceId() {
        return  ServiceResigerType.ZK.getType()+ "://" +  getServiceIpAdd() +":" + getServicePort() +getUri();
    }

    public String getNodePath() {
        return getServiceName() + getUri();
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }
}
