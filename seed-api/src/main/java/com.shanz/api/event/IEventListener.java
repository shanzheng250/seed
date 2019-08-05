package com.shanz.api.event;

/**
 * @ClassName:IEventListener
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/6/27 9:41
 * @Version:1.0
 **/
public interface IEventListener<Event> {

    public void onCompleted();

    public void onError(Throwable throwable);

    public void onNext(Event event);

}
