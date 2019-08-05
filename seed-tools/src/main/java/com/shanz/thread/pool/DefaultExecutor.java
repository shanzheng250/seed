package com.shanz.thread.pool;

import java.util.concurrent.*;

/**
 * @ClassName:DefaultThreadPoolExecutor
 * @Description: 自定义线程池
 * @Author: shanz
 * @Date: 2019/2/15 9:55
 * @Version:1.0
 **/
public class DefaultExecutor  extends ThreadPoolExecutor {

    public DefaultExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }
}
