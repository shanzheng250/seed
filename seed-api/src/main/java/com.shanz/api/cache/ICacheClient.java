package com.shanz.api.cache;

import com.shanz.api.spi.SPI;

/**
 * @ClassName:ICacheClient
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/6/25 11:42
 * @Version:1.0
 **/
@SPI(value = "redis")
public interface ICacheClient {

    void init();

    void set(String key,String value);

}
