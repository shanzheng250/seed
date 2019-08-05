package com.shanz.netty.http;

import com.google.common.primitives.Ints;
import com.shanz.Constants;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName:RequestContext
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/7/1 16:50
 * @Version:1.0
 **/
public class RequestContext implements HttpCallback {

    private static final  int TIME_OUT = 5000;

    private final long startime = System.currentTimeMillis();

    AtomicBoolean cancelled = new AtomicBoolean(false);

    final int readTimeout;

    private long endTime = startime;

    private String uri;

    private HttpCallback callback;

    FullHttpRequest request;

    String host;


    public RequestContext(FullHttpRequest request, HttpCallback callback) {
        this.callback = callback;
        this.request = request;
        this.uri = request.uri();
        this.readTimeout = parseTimeout();
    }

    private int parseTimeout() {
        String timeout = request.headers().get(Constants.HTTP_HEAD_READ_TIMEOUT);
        if (timeout != null) {
            request.headers().remove(Constants.HTTP_HEAD_READ_TIMEOUT);
            Integer integer = Ints.tryParse(timeout);
            if (integer != null && integer > 0) {
                return integer;
            }
        }
        return TIME_OUT;
    }

    @Override
    public void onResponse(HttpResponse response) {
        callback.onResponse(response);
        endTime = System.currentTimeMillis();
        destroy();
    }

    @Override
    public void onFailure(int statusCode, String reasonPhrase) {
        callback.onFailure(statusCode, reasonPhrase);
        endTime = System.currentTimeMillis();
        destroy();
    }

    @Override
    public void onException(Throwable throwable) {
        callback.onException(throwable);
        endTime = System.currentTimeMillis();
        destroy();
    }

    @Override
    public void onTimeout() {
        callback.onTimeout();
        endTime = System.currentTimeMillis();
        destroy();
    }

    @Override
    public boolean onRedirect(HttpResponse response) {
        endTime = System.currentTimeMillis();
        return callback.onRedirect(response);
    }

    private void destroy() {
        request = null;
        callback = null;
    }
}
