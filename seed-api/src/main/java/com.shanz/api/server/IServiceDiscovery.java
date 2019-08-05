package com.shanz.api.server;

import com.shanz.api.server.node.ServiceNode;
import com.shanz.api.spi.SPI;

import java.util.List;

/**
 * @ClassName:IServiceDiscovery
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/6/24 9:32
 * @Version:1.0
 **/
@SPI("zk")
public interface IServiceDiscovery {

    /**
     * 功能描述 发现服务主动  服务器pull
     * @param:
     * @return:
     * @date: 2019/6/24 9:36
     */
    List<ServiceNode> lookup(ServiceNode serviceNode);

    /**
     * 功能描述 服务变动被动接受 服务器push 订阅
     * @param:
     * @return:
     * @date: 2019/6/24 10:32
     */
    void subscribe(String path, IServiceListener listener);

    /**
     * 功能描述 取消订阅
     * @param:
     * @return:
     * @date: 2019/6/24 10:32
     */
    void unsubscribe(String path, IServiceListener listener);

}
