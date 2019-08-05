package com.shanz.event.rx;

import com.shanz.api.event.Event;
import com.shanz.api.event.IEventListener;

/**
 * @ClassName:IEventPubSub
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/6/27 9:37
 * @Version:1.0
 **/
public interface IEventPubSub {

    void register(Event event,IEventListener iEventListener);       //注册

    void publish(Event e);              // 发布

}
