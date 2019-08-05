package com.shanz.event.eventbus;

/**
 * @ClassName:DefaultCousumerEventBus
 * @Description: 用户自定义事件，事件基类
 * @Author: shanz
 * @Date: 2019/2/14 11:08
 * @Version:1.0
 **/
public abstract class DefaultConsumerEventBus {

    public DefaultConsumerEventBus() {
        DefaultEventBus.register(this);
    }

}
