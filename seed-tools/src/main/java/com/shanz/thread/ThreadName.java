package com.shanz.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName:ThreadName
 * @Description: 线程名称  为了检测线程运行状态
 * @Author: shanz
 * @Date: 2019/2/15 9:49
 * @Version:1.0
 **/
public final class ThreadName {

    // 项目前缀名称
    public static final String NS = "jessica" ;

    public static final String THREAD_NAME_PREFIX = NS + "-t";

    // 主线程
    public static final String T_BOSS =  NS + "-boss";

    // 工作线程
    public static final String T_WORK =  NS + "-work";

    // 自定义事件
    public static final String T_EVENT_BUS = NS + "-event";

    // 心跳检测线程
    public static final String T_CONN_TIMER = NS + "-conn-check-timer";
}
