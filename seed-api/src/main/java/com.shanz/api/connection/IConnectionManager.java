package com.shanz.api.connection;

import io.netty.channel.Channel;

/**
 * @ClassName:IConnectionManager
 * @Description:
 * @Author: shanzheng
 * @Date: 2019/7/9 13:36
 * @Version:1.0
 **/
public interface IConnectionManager {

    IConnection get(Channel channel);

    IConnection removeAndClose(Channel channel);

    void add(IConnection connection);

    int getConnNum();

    void init();

    void destroy();
}
