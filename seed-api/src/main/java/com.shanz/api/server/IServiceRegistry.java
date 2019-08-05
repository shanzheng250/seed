package com.shanz.api.server;

import com.shanz.api.server.node.ServiceNode;
import com.shanz.api.spi.SPI;

/**
 * @ClassName:IServiceResiger
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/6/24 9:31
 * @Version:1.0
 **/
@SPI("zk")
public interface IServiceRegistry {

    void resiger(ServiceNode node); //注册

    void unresiger(ServiceNode node); //注销

}
