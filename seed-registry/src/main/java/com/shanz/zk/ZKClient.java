package com.shanz.zk;

import com.shanz.Constants;
import com.shanz.config.ZKConfig;
import com.shanz.log.Logs;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.*;

/**
 * @ClassName:ZKclient
 * @Description: zk链接工具
 * @Author: shanz
 * @Date: 2019/6/21 11:08
 * @Version:1.0
 **/
public class ZKClient {

    private ZKConfig zkConfig;

    private CuratorFramework client;

    private Map<String, String> ephemeralNodes = new LinkedHashMap<>(4);        // 临时节点缓存，当zk故障等问题发生时 监听器重新从内存加载

    private Map<String, String> ephemeralSequentialNodes = new LinkedHashMap<>(1);

    private TreeCache cache;  //本地缓存

    public static final ZKClient I = I();

    private synchronized static ZKClient I() {
        return I == null ? new ZKClient() : I;
    }


    public ZKClient() {
        // todo 配置文件处理
        String hosts = "172.16.43.234:2181,172.16.43.235:2181,172.16.43.86:2181";
        this.zkConfig = new ZKConfig(hosts);
        zkConfig.build();
        init();
        start();
    }

    /**
     * 功能描述 初始化zk
     * @param:
     * @return:
     * @date: 2019/6/21 14:13
     */
    public void init(){
        if (client != null) return;

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory
                .builder()
                .connectString(zkConfig.getHosts())
                .connectionTimeoutMs(zkConfig.getConnectionTimeout())
                .sessionTimeoutMs(zkConfig.getSessionTimeout())
                .retryPolicy(new ExponentialBackoffRetry(zkConfig.getBaseSleepTimeMs(), zkConfig.getMaxRetries(), zkConfig.getMaxSleepMs()))
                .namespace(zkConfig.getNamespace());
        client = builder.build();
        Logs.RSD.info("init zk client, config={}", zkConfig.toString());
    }

    /**
     * 功能描述 启动客户端
     * @param:
     * @return:
     * @date: 2019/6/21 14:24
     */
    public void start(){
        try {
            if (client != null)
                client.start();
            initLocalCache(zkConfig.getWatchPath());
            reloadLocalEphemeral();
        } catch (Exception e) {
            Logs.RSD.error("start:{}", e);
        }
    }
    
    /**
     * 功能描述
     * @param: 本地缓存
     * @return: 
     * @date: 2019/6/21 15:52
     */
    private void initLocalCache(String watchRootPath) throws Exception {
        cache = new TreeCache(client, watchRootPath);
        cache.start();
    }

    /**
     * 功能描述 当zk 重新连接时 重新注册
     * @param:
     * @return:
     * @date: 2019/6/21 16:05
     */
    private void reloadLocalEphemeral(){
        client.getConnectionStateListenable().addListener((t,newState)->{
            if (newState == ConnectionState.RECONNECTED){
                ephemeralNodes.forEach(this::reRegisterEphemeral);
                ephemeralSequentialNodes.forEach(this::reRegisterEphemeralSequential);
            }
        });
    }

    /**
     * 功能描述 获取信息
     * @param:
     * @return:
     * @date: 2019/6/21 16:10
     */
    public String get(String path){
        ChildData data  = cache.getCurrentData(path);
        if (null != data) {
            return null == data.getData() ? null : new String(data.getData(), Constants.UTF_8);
        }
        return getFromZK(path);
    }

    /**
     * 功能描述 获取子节点信息
     * @param:
     * @return:
     * @date: 2019/6/21 16:16
     */
    public List<String> getChildren(String path){
        if (!isExisted(path)) return Collections.emptyList();
        try {
            List<String> values =  client.getChildren().forPath(path);
            values.sort(Comparator.reverseOrder());
            return values;
        } catch (Exception e) {
            Logs.RSD.error("getChildrenKeys:{}", path, e);
            return Collections.emptyList();
        }
    }


    /**
     * 功能描述 从远方zk 拿出数据
     * @param:
     * @return:
     * @date: 2019/6/21 15:27
     */
    private String getFromZK(String path){
        if (isExisted(path)) {
            try {
                return  new String(client.getData().forPath(path),Constants.UTF_8);
            } catch (Exception e) {
                Logs.RSD.error("getFromZK:{}", path, e);
            }
        }
        return null;
    }

    /**
     * 功能描述 注册持久节点
     * @param:
     * @return:
     * @date: 2019/6/21 14:28
     */
    public void resigerPersis(String path,String value){
        try {
            if (isExisted(path)){
                update(path,value);
            }else {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,value.getBytes());
            }
        } catch (Exception e) {
            Logs.RSD.error("resigerPersis:{},{}", path, value, e);
        }
    }

    /**
     * 功能描述 注册临时节点
     * @param:
     * @return:
     * @date: 2019/6/21 15:09
     */
    public void registerEphemeral(final String path, final String value, boolean cacheNode) {
        try {
            if (isExisted(path)){
                delete(path);
            } else {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,value.getBytes());
                if(cacheNode) ephemeralNodes.putIfAbsent(path,value);
            }
        } catch (Exception e){
            Logs.RSD.error("registerEphemeral:{},{}", path, value, e);
        }
    }


    /**
     * 功能描述 重新加载
     * @param:
     * @return:
     * @date: 2019/6/21 16:04
     */
    public void reRegisterEphemeral(final String path, final String value){
        registerEphemeral(path,value,false);
    }

    /**
     * 注册临时顺序数据
     *
     * @param key
     * @param value
     * @param cacheNode 第一次注册时设置为true, 连接断开重新注册时设置为false
     */
    public void registerEphemeralSequential(final String key, final String value, boolean cacheNode) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key, value.getBytes());
            if (cacheNode) ephemeralSequentialNodes.put(key, value);
        } catch (Exception ex) {
            Logs.RSD.error("persistEphemeralSequential:{},{}", key, value, ex);
        }
    }

    /**
     * 功能描述 重新加载
     * @param:
     * @return:
     * @date: 2019/6/21 16:04
     */
    public void reRegisterEphemeralSequential(final String path, final String value){
        registerEphemeralSequential(path,value,false);
    }

    /**
     * 功能描述 删除临时节点
     * @param:
     * @return:
     * @date: 2019/6/21 15:05
     */
    public void delete(String path){
        try {
              client.delete().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述 节点是否存在
     * @param:
     * @return:
     * @date: 2019/6/21 14:35
     */
    private boolean isExisted(String path){
        try {
            return null != client.checkExists().forPath(path);
        } catch (Exception e) {
            Logs.RSD.error("isExisted:{}", path, e);
            return false;
        }
    }

    /**
     * 功能描述  修改节点信息
     * @param:
     * @return:
     * @date: 2019/6/21 15:00
     */
    private void update(String path,String value){
        try {
            client.inTransaction().check().forPath(path).and().setData().forPath(path, value.getBytes(Constants.UTF_8)).and().commit();
        } catch (Exception ex) {
            Logs.RSD.error("update:{},{}", path, value, ex);
        }
    }

    /**
     * 功能描述 增加监听器
     * @param:
     * @return:
     * @date: 2019/6/21 16:27
     */
    public void registerListener(TreeCacheListener listener) {
        cache.getListenable().addListener(listener);
    }

    /**
     * 功能描述 取消监听器s
     * @param:
     * @return:
     * @date: 2019/6/24 10:50
     */
    public void unregisterListener(TreeCacheListener listener){
        cache.getListenable().removeListener(listener);
    }

}
