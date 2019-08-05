package com.shanz.zk;
import com.shanz.api.server.node.ServiceNode;
import org.apache.curator.utils.ZKPaths;

import static org.apache.curator.utils.ZKPaths.PATH_SEPARATOR;
/**
 * @ClassName:ZKPath
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/6/24 9:07
 * @Version:1.0
 **/
public enum  ZKPath {

    SEED_SERVER_PROVIDER("/META-INF/provider", "注册的服务"),
    SEED_SERVER_CONSUMER("/META-INF/consumer", "消费的服务");

    ZKPath(String root,  String desc) {
        this.root = root;
    }

    private final String root;

    public String getRootPath() {
        return root;
    }

    public String getNodePath(String... tails) {
        String path = getRootPath();
        for (String tail : tails) {
            path += (PATH_SEPARATOR + tail);
        }
        return path;
    }

    //根据从zk中获取的app的值，拼装全路径
    public String getFullPath(String childPaths) {
        return root + PATH_SEPARATOR + childPaths;
    }

    public String getTail(String childPaths) {
        return ZKPaths.getNodeFromPath(childPaths);
    }

}
