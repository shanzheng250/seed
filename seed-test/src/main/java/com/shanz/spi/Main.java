package com.shanz.spi;

import com.shanz.api.connection.ICipher;
import com.shanz.api.mq.IMQClient;
import com.shanz.api.spi.SPILoader;
import com.shanz.api.spi.factory.Factory;

/**
 * @ClassName:Main
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/6/25 14:30
 * @Version:1.0
 **/
public class Main {


    public static void main(String[] args) {


        ICipher iCipher = SPILoader.getLoader(ICipher.class).loadDefault();

        String s = ";北京";

        String key = new String(iCipher.decrypt(s.getBytes()));

        System.out.println("加密--->{" +key  +"}");


        System.out.println("解密--->{" + new String(iCipher.decrypt(key.getBytes())) +"}");
    }
}
