package com.shanz.netty.http;

/**
 * @ClassName:HttpClient
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/7/1 16:48
 * @Version:1.0
 **/
public interface HttpClient {

    void request(RequestContext context) throws Exception;
}
