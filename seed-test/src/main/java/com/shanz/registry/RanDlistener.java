package com.shanz.registry;

import com.shanz.api.server.IServiceListener;
import com.shanz.api.server.node.ServiceNode;

/**
 * @ClassName:RanDlistener
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/6/24 11:16
 * @Version:1.0
 **/
public class RanDlistener implements IServiceListener {

    @Override
    public void onServiceInitialized(String path, ServiceNode node) {
        System.out.println(path +   "---------------init----------------" + node.toString());
    }

    @Override
    public void onServiceAdded(String path, ServiceNode node) {
        System.out.println(path +   "---------------add----------------" + node.toString());

    }

    @Override
    public void onServiceUpdated(String path, ServiceNode node) {
        System.out.println(path +   "---------------udpate----------------" + node.toString());

    }

    @Override
    public void onServiceRemoved(String path, ServiceNode node) {
        System.out.println(path +   "---------------remove----------------" + node.toString());

    }
}
