package com.shanz.registry;

import com.shanz.api.server.IServiceDiscovery;
import com.shanz.api.server.IServiceRegistry;
import com.shanz.api.server.node.ServiceNode;
import com.shanz.api.server.node.ZKServiceNode;
import com.shanz.zk.ZKRegistryAndDiscovery;
import org.apache.curator.utils.ZKPaths;

import java.util.List;

/**
 * @ClassName:RandD
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/6/24 11:04
 * @Version:1.0
 **/
public class RandD {



    public static void main(String[] args) {

        IServiceDiscovery discovery = new ZKRegistryAndDiscovery();

        IServiceRegistry registry = new ZKRegistryAndDiscovery();


        ServiceNode serviceNode = new ZKServiceNode();
        serviceNode.setServiceName("test-service");
        serviceNode.setServiceIpAdd("10.0.21.44");
        serviceNode.setServicePort(8080);
        serviceNode.setVersion("v1.0");


        ServiceNode serviceNode1 = new ZKServiceNode();
        serviceNode1.setServiceName("test-service");
        serviceNode1.setServiceIpAdd("10.0.21.44");
        serviceNode1.setServicePort(8080);
        serviceNode1.setVersion("v1.0");
        serviceNode1.setModuleName("rule");
        serviceNode1.setUri("/user/local");


        ServiceNode serviceNode2 = new ZKServiceNode();
        serviceNode2.setServiceName("test-service");
        serviceNode2.setServiceIpAdd("10.0.21.44");
        serviceNode2.setServicePort(8080);
        serviceNode2.setVersion("v1.0");
        serviceNode2.setModuleName("event");
        serviceNode2.setUri("/user/local111");

//        registry.resiger(serviceNode);
//
//        registry.resiger(serviceNode1);

        String path = serviceNode2.getServiceName() +  ZKPaths.PATH_SEPARATOR + serviceNode2.getVersion();


        discovery.subscribe(path,new RanDlistener());

        registry.resiger(serviceNode2);


        List<ServiceNode> s =  discovery.lookup(serviceNode);



        System.out.println(s.toString());

    }



}
