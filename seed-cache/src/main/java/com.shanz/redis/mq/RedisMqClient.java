package com.shanz.redis.mq;


import com.google.common.collect.Lists;
import com.shanz.api.mq.IMQClient;
import com.shanz.api.mq.IMQMessageReceiver;
import com.shanz.api.spi.SPIOrder;
import com.shanz.log.Logs;
import com.shanz.redis.RedisManager;
import org.omg.CORBA.ORB;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName:RedisMqClient
 * @Description: mq
 * @Author: shanz
 * @Date: 2019/6/19 11:11
 * @Version:1.0
 **/
@SPIOrder(order = -1)
public class RedisMqClient implements IMQClient {

    private Map<String,List<IMQMessageReceiver>> receivers = new ConcurrentHashMap<>();       // topic 和 监听器容器

    private Subscribe subscribe;

    ExecutorService service;

    public RedisMqClient() {
        this.subscribe = new Subscribe(this);
    }

    @Override
    public void init() {
        // 初始化redis链接
        RedisManager.instance.init();
        // 监听器执行线程池
        service = Executors.newFixedThreadPool(5);
    }

    @Override
    public void subscribe(String channel,IMQMessageReceiver listener) {
        // 不存在则添加到map中
        receivers.computeIfAbsent(channel,l-> Lists.newArrayList()).add(listener);

        RedisManager.instance.subscribe(subscribe,channel);
    }

    @Override
    public void publish(String channel, Object message) {
        RedisManager.instance.publish(channel,message);
    }

    // 接收到消息的回调函数
    public void onMessageCallBack(String channel,String message){
        if (receivers.get(channel).isEmpty()){
            Logs.CACHE.info("no listener to execute");
            return;
        }

        for (IMQMessageReceiver receiver : receivers.get(channel)){
           service.execute(()->receiver.receive(channel,message));
        }
    }
}
