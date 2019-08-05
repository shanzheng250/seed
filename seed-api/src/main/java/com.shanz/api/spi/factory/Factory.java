package com.shanz.api.spi.factory;

import com.shanz.api.spi.SPI;

/**
 * @ClassName:SPILoaderFactory
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/6/24 17:08
 * @Version:1.0
 **/
@SPI(value = "cache")
public interface Factory<T> {

    <T> T get();

}
