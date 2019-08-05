package com.shanz.api.server.node;

/**
 * @ClassName:ServiceNode
 * @Description: 服务注册节点  类似springcloud的instant
 * @Author: shanz
 * @Date: 2019/6/21 16:36
 * @Version:1.0
 **/
public abstract class ServiceNode {

    private String serviceName; // 服务名称

    private String moduleName;  // 服务模块名称

    private String serviceIpAdd;  //服务请求ip

    private int servicePort;    //端口

    private String uri = ""; // 服务模块uri

    private String version;   // 版本号

    private ServiceStatus status = ServiceStatus.UP;   // 服务状态


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public abstract String getServiceId();

    public String getServiceIpAdd() {
        return serviceIpAdd;
    }

    public void setServiceIpAdd(String serviceIpAdd) {
        this.serviceIpAdd = serviceIpAdd;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public String toString() {
        return "ServiceNode{" +
                "serviceName='" + serviceName + '\'' +
                ", serviceId='" + getServiceId() + '\'' +
                ", serviceIpAdd='" + serviceIpAdd + '\'' +
                ", servicePort=" + servicePort +
                ", version='" + version + '\'' +
                ", status=" + status +
                '}';
    }
}
