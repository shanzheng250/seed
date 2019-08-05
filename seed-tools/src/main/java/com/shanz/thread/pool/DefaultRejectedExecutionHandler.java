package com.shanz.thread.pool;

import com.shanz.common.JVMUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import static com.shanz.thread.pool.ThreadPoolConfig.REJECTED_POLICY_ABORT;
import static com.shanz.thread.pool.ThreadPoolConfig.REJECTED_POLICY_CALLER_RUNS;

/**
 * @ClassName:ConsumerRejectedExecutionHandler
 * @Description: 用户自定义的拒绝策略 -- 当拒绝时进行dump操作
 * @Author: shanz
 * @Date: 2019/2/15 11:06
 * @Version:1.0
 **/
public class DefaultRejectedExecutionHandler implements RejectedExecutionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(RejectedExecutionHandler.class);


    private ThreadPoolConfig config;  // 线程池配置项

    private volatile boolean isDump = false; // 是否在dump文件

    public DefaultRejectedExecutionHandler(ThreadPoolConfig config) {
        this.config = config;
    }


    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

        /**
         * 功能描述 这里是对拒绝策略的重写discard是do noting
         * discard old 一般不使用就不进行处理，后续若使用discard old则多做一个判断分支
         *
         *
         */
        if (!isDump){
            isDump = true;
            dumpJVMInfo();
        }


        if (REJECTED_POLICY_ABORT == config.getRejectedPolicy()){
            throw new RejectedExecutionException("one task rejected, pool=" + config.getThreadName());
        } else  if (REJECTED_POLICY_CALLER_RUNS == config.getRejectedPolicy()){
            if (!executor.isShutdown()) {
                r.run();
            }
        }




    }


    private void dumpJVMInfo() {
        LOGGER.info("start dump jvm info");
//        JVMUtil.dumpJstack(DUMP_DIR + "/" + config.getThreadName());
        LOGGER.info("end dump jvm info");
    }

}
