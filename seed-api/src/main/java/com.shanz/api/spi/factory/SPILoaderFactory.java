package com.shanz.api.spi.factory;

import com.shanz.api.cache.ICacheClient;
import com.shanz.api.mq.IMQClient;
import com.shanz.api.server.IServiceDiscovery;
import com.shanz.api.server.IServiceRegistry;
import com.shanz.api.spi.SPILoader;

/**
 * @ClassName:SPILoaderFactory
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/6/25 15:39
 * @Version:1.0
 **/
public class SPILoaderFactory {


    private SPILoaderFactory() {}

    /**
     * 功能描述 静态内部类的单利模式
     * @param:
     * @return:
     * @date: 2019/6/25 15:44
     */
    public static SPILoaderFactory getInstance(){
        return InnerFactory.spiLoaderFactory;
    }


    public  <T> T getProduce(Product product){

        switch (product.name()){

            case "MQ":
                return (T)SPILoader.getLoader(IMQClient.class).loadDefault();
            case "REGISTRY":
                return (T)SPILoader.getLoader(IServiceRegistry.class).loadDefault();
            case "DISCOVER":
                return (T)SPILoader.getLoader(IServiceDiscovery.class).loadDefault();
            case "CACHE":
                return (T)SPILoader.getLoader(ICacheClient.class).loadDefault();
        }
        return null;
    }



    private static class InnerFactory{
      final static SPILoaderFactory spiLoaderFactory  = new SPILoaderFactory();
    }

}
