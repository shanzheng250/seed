package com.shanz.api.server;

import com.shanz.api.server.node.ServiceNode;

/**
 * @ClassName:ServiceListener
 * @Description: 服务监听器
 * @Author: shanz
 * @Date: 2019/6/21 16:34
 * @Version:1.0
 **/
public interface IServiceListener {

    void onServiceInitialized(String path, ServiceNode node);

    void onServiceAdded(String path, ServiceNode node);

    void onServiceUpdated(String path, ServiceNode node);

    void onServiceRemoved(String path, ServiceNode node);
}
