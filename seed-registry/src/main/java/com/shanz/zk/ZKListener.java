package com.shanz.zk;

import com.google.common.base.Strings;
import com.shanz.Jsons;
import com.shanz.api.server.IServiceListener;
import com.shanz.api.server.node.ZKServiceNode;
import com.shanz.log.Logs;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * @ClassName:ZKListener
 * @Description: TODO
 * @Author: shanz
 * @Date: 2019/6/21 16:32
 * @Version:1.0
 **/
public class ZKListener implements TreeCacheListener {

    private String watchPath;   // 监控路径

    private IServiceListener listener;  // 监听器

    public ZKListener(String watchPath, IServiceListener listener) {
        this.watchPath = watchPath;
        this.listener = listener;
    }

    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent event) throws Exception {
        ChildData data = event.getData();
        if (data == null) return;
        String dataPath = data.getPath();
        if (Strings.isNullOrEmpty(dataPath)) return;

        if (dataPath.startsWith(watchPath)){
            switch (event.getType()){
                case INITIALIZED:
                    listener.onServiceAdded(dataPath,Jsons.fromJson(data.getData(),ZKServiceNode.class));
                    break;
                case NODE_ADDED:
                    listener.onServiceAdded(dataPath,Jsons.fromJson(data.getData(),ZKServiceNode.class));
                    break;
                case NODE_REMOVED:
                    listener.onServiceRemoved(dataPath,Jsons.fromJson(data.getData(),ZKServiceNode.class));
                    break;
                case NODE_UPDATED:
                    listener.onServiceUpdated(dataPath,Jsons.fromJson(data.toString(),ZKServiceNode.class));
                    break;
            }
            Logs.RSD.info("ZK node data change={}, nodePath={}, watchPath={}, ns={}");

        }



    }
}
