package com.shanz.thread.pool;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * @ClassName:ThreadPoolConfig
 * @Description: 自定义线程池配置
 * @Author: shanz
 * @Date: 2019/2/15 9:59
 * @Version:1.0
 **/
public class ThreadPoolConfig {
    // 拒绝策略
    public static final int REJECTED_POLICY_ABORT = 0;  // 拒绝并抛出异常

    public static final int REJECTED_POLICY_DISCARD = 1;  // 拒绝

    public static final int REJECTED_POLICY_DISCARD_OLD = 2;   // 拒绝后丢弃队列尾部并执行拒绝线程

    public static final int REJECTED_POLICY_CALLER_RUNS = 3;   // 拒绝后运行

    private String threadName;    // 线程池名称

    private int coreThreadNum;  // 核心线程数

    private int maxThreadNum;    // 最大线程数

    private int keepaliveTime;  // 最大存活时间

    private int queueCapacity;  // 允许缓冲在队列中的任务数 (0:不缓冲、负数：无限大、正数：缓冲的任务数)

    private int rejectedPolicy = REJECTED_POLICY_ABORT; // 默认拒绝策略

    public ThreadPoolConfig(String threadName) {
        this.threadName = threadName;
    }

    public static ThreadPoolConfig buildFixed(String name, int threads) {
        return new ThreadPoolConfig(name)
                .setCoreThreadNum(threads)
                .setMaxThreadNum(threads)
                .setQueueCapacity(1)
                .setKeepaliveTime(0);
    }

    public static ThreadPoolConfig buildCached(String name) {
        return new ThreadPoolConfig(name)
                    .setQueueCapacity(-1)
                    .setKeepaliveTime(60);
    }

    public static ThreadPoolConfig build(String name) {
        return new ThreadPoolConfig(name);
    }


    /**
     * 功能描述 根据设置属性返回对应的队列
     * @param:
     * @return:
     * @date: 2019/2/15 10:27
     */
    public BlockingQueue<Runnable> getQueue(int taskNum){
        if (queueCapacity == 0){
            return new SynchronousQueue();
        }else if (queueCapacity < 0 ){
            return new LinkedBlockingQueue();
        }else {
            return new LinkedBlockingDeque(taskNum);
        }
    }

    public String getThreadName() {
        return threadName;
    }

    public ThreadPoolConfig setThreadName(String threadName) {
        this.threadName = threadName;
        return this;
    }

    public int getCoreThreadNum() {
        return coreThreadNum;
    }

    public ThreadPoolConfig setCoreThreadNum(int coreThreadNum) {
        this.coreThreadNum = coreThreadNum;
        return this;
    }

    public int getMaxThreadNum() {
        return maxThreadNum;
    }

    public ThreadPoolConfig setMaxThreadNum(int maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
        return this;
    }

    public int getKeepaliveTime() {
        return keepaliveTime;
    }

    public ThreadPoolConfig setKeepaliveTime(int keepaliveTime) {
        this.keepaliveTime = keepaliveTime;
        return this;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public ThreadPoolConfig setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
        return this;
    }

    public int getRejectedPolicy() {
        return rejectedPolicy;
    }

    public ThreadPoolConfig setRejectedPolicy(int rejectedPolicy) {
        this.rejectedPolicy = rejectedPolicy;
        return this;
    }
}
