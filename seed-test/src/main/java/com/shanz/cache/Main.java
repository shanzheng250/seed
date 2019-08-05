package com.shanz.cache;

import com.shanz.api.cache.ICacheClient;
import com.shanz.api.spi.factory.Product;
import com.shanz.api.spi.factory.SPILoaderFactory;

/**
 * @ClassName:Main
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/6/25 16:09
 * @Version:1.0
 **/
public class Main {


    public static void main(String[] args) {

        ICacheClient a =  SPILoaderFactory.getInstance().getProduce(Product.CACHE);
        a.init();
        a.set("shanz","123");

    }
}
