package com.shanz.mq;

import com.shanz.api.mq.IMQClient;
import com.shanz.api.mq.IMQMessageReceiver;
import com.shanz.api.spi.SPILoader;

import java.io.IOException;

/**
 * @ClassName:Main
 * @Description: TODO
 * @Author: shanz
 * @Date: 2019/6/20 11:03
 * @Version:1.0
 **/
public class Main {
    public static void main(String[] args) throws IOException {
//        IMQClient client = new RedisMqClient();

        IMQClient client = SPILoader.getLoader(IMQClient.class).loadDefault();

        client.init();

        client.subscribe("cctv", new IMQMessageReceiver() {
            @Override
            public void receive(String topic, Object message) {
                System.out.println("---------->" + topic +"-------------->" + (String)message);
            }
        });

        Runnable r = new Runnable() {
            @Override
            public void run() {
                client.publish("cctv","tiantian");
            }
        };

       Thread t = new Thread(r);

       t.start();

       System.in.read();
    }
}
