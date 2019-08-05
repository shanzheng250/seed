package com.shanz.api.mq;

/**
 * @ClassName:IMQMessageReceiver
 * @Description: mq消息接收接口
 * @Author: shanz
 * @Date: 2019/6/20 10:27
 * @Version:1.0
 **/
public interface IMQMessageReceiver {

    void receive(String topic, Object message);

}
