package com.shanz.event.rx;

import com.google.common.collect.Lists;
import com.shanz.api.event.Event;
import com.shanz.api.event.IEventListener;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName:EventPub
 * @Description: rxjava 实现的事件机制
 * @Author: shanzheng
 * @Date: 2019/6/26 15:41
 * @Version:1.0
 **/
public class EventPubSub implements IEventPubSub {

    private static Map<Class<? extends Event>, List<IEventListener>> listeners;

    private static Map<Class<? extends Event>, Observable> publishers;

    private static EventPubSub pubSub;


    private static EventPubSub getInstance(){
        if (pubSub == null){
            synchronized (listeners){
                if (pubSub == null){
                    pubSub = new EventPubSub();
                    listeners = new ConcurrentHashMap<>();
                    publishers = new ConcurrentHashMap<>();
                }
            }
        }
        return pubSub;
    }



    @Override
    public void publish(Event event) {
        Observable<Event> observable = Observable.create(new Observable.OnSubscribe<Event>(){
            @Override
            public void call(Subscriber<? super Event> subscriber) {
                subscriber.onNext(event);
            }
        });

        publishers.putIfAbsent(event.getClass(),observable);

    }


    @Override
    public void register(Event event,IEventListener iEventListener) {
        listeners.computeIfAbsent(event.getClass(),l -> Lists.newArrayList()).add(iEventListener);
        List<IEventListener> ieventListeners = listeners.get(event.getClass());

        Observable observable =  publishers.get(event.getClass());

        if (observable == null){
            return;
        }

        // 启动新线程进行处理
        observable.subscribeOn(Schedulers.newThread()).subscribe(new Observer<Event>() {
            @Override
            public void onCompleted() {
                ieventListeners.forEach(IEventListener::onCompleted);
            }

            @Override
            public void onError(Throwable throwable) {
                ieventListeners.forEach(i->{i.onError(throwable);});
            }

            @Override
            public void onNext(Event event) {
                ieventListeners.forEach(i->{i.onNext(event);});
            }
        });


    }
}
