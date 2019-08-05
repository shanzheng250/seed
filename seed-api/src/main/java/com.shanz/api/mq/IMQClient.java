package com.shanz.api.mq;

import com.shanz.api.spi.SPI;

/**
 * @ClassName:MqClient
 * @Description: mq 操作实现类
 * @Author: shanz
 * @Date: 2019/6/19 13:48
 * @Version:1.0
 **/
@SPI(value = "redis")
public interface IMQClient {

    void init();    // mq 初始化

    void subscribe(String channel,IMQMessageReceiver listener); // 监听者

    void publish(String channel,Object message);            // 发布者
}
