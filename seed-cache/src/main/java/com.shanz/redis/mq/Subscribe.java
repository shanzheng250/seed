package com.shanz.redis.mq;

import com.shanz.log.Logs;
import redis.clients.jedis.JedisPubSub;

/**
 * @ClassName:Subscribe
 * @Description: redis 生成mq
 * @Author: shanz
 * @Date: 2019/6/18 15:51
 * @Version:1.0
 **/
public final class Subscribe extends JedisPubSub {

    private RedisMqClient redisMqClient;

    public Subscribe(RedisMqClient redisMqClient) {
        this.redisMqClient = redisMqClient;
    }

    @Override
    public void onMessage(String channel, String message) {
        Logs.CACHE.info("onMessage:{},{}", channel, message);
        super.onMessage(channel, message);
        redisMqClient.onMessageCallBack(channel,message);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        Logs.CACHE.info("onPMessage:{},{},{}", pattern, channel, message);
        super.onPMessage(pattern, channel, message);
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        Logs.CACHE.info("onPSubscribe:{},{}", pattern, subscribedChannels);
        super.onPSubscribe(pattern, subscribedChannels);
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        Logs.CACHE.info("onPUnsubscribe:{},{}", pattern, subscribedChannels);
        super.onPUnsubscribe(pattern, subscribedChannels);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        Logs.CACHE.info("onSubscribe:{},{}", channel, subscribedChannels);
        super.onSubscribe(channel, subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        Logs.CACHE.info("onUnsubscribe:{},{}", channel, subscribedChannels);
        super.onUnsubscribe(channel, subscribedChannels);
    }


    @Override
    public void unsubscribe() {
        Logs.CACHE.info("unsubscribe");
        super.unsubscribe();
    }

    @Override
    public void unsubscribe(String... channels) {
        Logs.CACHE.info("unsubscribe:{}", channels);
        super.unsubscribe(channels);
    }
}
