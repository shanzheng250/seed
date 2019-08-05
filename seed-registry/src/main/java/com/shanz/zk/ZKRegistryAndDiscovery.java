package com.shanz.zk;

import com.shanz.Jsons;
import com.shanz.api.server.IServiceDiscovery;
import com.shanz.api.server.IServiceListener;
import com.shanz.api.server.IServiceRegistry;
import com.shanz.api.server.node.ServiceNode;
import com.shanz.api.server.node.ZKServiceNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.utils.ZKPaths;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.curator.utils.ZKPaths.PATH_SEPARATOR;

/**
 * @ClassName:ZKResigerAndDiscovery
 * @Description: 注册和服务发现
 * @Author: shanzheng
 * @Date: 2019/6/24 9:29
 * @Version:1.0
 **/
public class ZKRegistryAndDiscovery implements IServiceDiscovery,IServiceRegistry {

    private static final ZKRegistryAndDiscovery RD = new ZKRegistryAndDiscovery();

    private ZKClient client;

    public ZKRegistryAndDiscovery() {
        this.client  = ZKClient.I;
    }

    @Override
    public List<ServiceNode> lookup(ServiceNode serviceNode) {

        String path = getRegistryNodePath(serviceNode);

        List<String> keys =  client.getChildren(path);

        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }

        return  keys.stream()
                .map(key -> path + PATH_SEPARATOR + key)
                .map(client::get)
                .filter(Objects::nonNull)
                .map(key -> Jsons.fromJson(key,ZKServiceNode.class))
                .collect(Collectors.toList());
    }

    @Override
    public void subscribe(String path, IServiceListener listener) {
        client.registerListener(new ZKListener(ZKPath.SEED_SERVER_PROVIDER.getFullPath(path),listener));
    }

    @Override
    public void unsubscribe(String path, IServiceListener listener) {
        client.unregisterListener(new ZKListener(ZKPath.SEED_SERVER_PROVIDER.getFullPath(path),listener));
    }

    @Override
    public void resiger(ServiceNode node) {
        client.resigerPersis(getRegistryNodePath(node),Jsons.toJson(node));
    }

    @Override
    public void unresiger(ServiceNode node) {
        client.delete(getRegistryNodePath(node));
    }


    /**
     * 功能描述 服务节点
     * @param:
     * @return:
     * @date: 2019/6/24 15:24
     */
    private String getRegistryNodePath(ServiceNode node){
        String path = node.getServiceName() +  ZKPaths.PATH_SEPARATOR + node.getVersion();
        if (!StringUtils.isBlank(node.getModuleName())){
            path += ZKPaths.PATH_SEPARATOR +  node.getModuleName();
        }
        return ZKPath.SEED_SERVER_PROVIDER.getFullPath(path);
    }
}
