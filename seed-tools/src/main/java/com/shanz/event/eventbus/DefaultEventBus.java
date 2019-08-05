package com.shanz.event.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.shanz.api.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * @ClassName:DefaultEventBus
 * @Description: T自定义事件
 * @Author: shanz
 * @Date: 2019/2/15 15:51
 * @Version:1.0
 **/
public class DefaultEventBus {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEventBus.class);


    private static EventBus eventBus;


    public static void create(Executor executor) {
        eventBus = new AsyncEventBus(executor, (exception, context)
                -> LOGGER.error("event bus subscriber ex", exception));
    }

    public static void post(Event event) {
        eventBus.post(event);
    }

    public static void register(Object bean) {
        eventBus.register(bean);
    }

    public static void unregister(Object bean) {
        eventBus.unregister(bean);
    }
}
