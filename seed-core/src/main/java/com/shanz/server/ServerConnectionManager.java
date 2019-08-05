package com.shanz.server;

import com.shanz.api.connection.IConnection;
import com.shanz.api.connection.IConnectionHolder;
import com.shanz.api.connection.IConnectionManager;
import com.shanz.config.CoreConfig;
import com.shanz.log.Logs;
import com.shanz.netty.connection.NettyConnection;
import com.shanz.thread.NamedThreadFactory;
import com.shanz.thread.ThreadName;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName:ServerConnectionManager
 * @Description: 链接管理器
 * @Author: shanzheng
 * @Date: 2019/7/9 13:36
 * @Version:1.0
 **/
public class ServerConnectionManager implements IConnectionManager {

    private ConnectionHolderFactory factory;  // 链接holder工厂

    private ConcurrentHashMap<ChannelId,IConnectionHolder> holders = new ConcurrentHashMap<>();     //链接保存内存

    private boolean heartbeat; // 是否启用心跳

    private final IConnectionHolder DEFAULT = new SimpleConnectionHolder(null); // 默认链接

    private HashedWheelTimer timer; //时间轮


    private ServerConnectionManager(boolean heartbeat) {
        this.heartbeat = heartbeat;
        factory = heartbeat ? HeartBeatTimer::new : SimpleConnectionHolder::new;
    }

    @Override
    public void init() {

        if (heartbeat){
            CoreConfig coreConfig = CoreConfig.buildConfig();
            long tickDuration = TimeUnit.SECONDS.toMillis(1);//1s 每秒钟走一步，一个心跳周期内大致走一圈
            int ticksPerWheel = (int) (coreConfig.getMaxHeartbeat() / tickDuration);
            this.timer = new HashedWheelTimer(
                    new NamedThreadFactory(ThreadName.T_CONN_TIMER),
                    tickDuration, TimeUnit.MILLISECONDS, ticksPerWheel
            );
        }
    }

    @Override
    public IConnection get(Channel channel) {
        return holders.getOrDefault(channel.id(),DEFAULT).get();
    }

    @Override
    public IConnection removeAndClose(Channel channel) {
        IConnectionHolder holder =   holders.remove(channel.id());

        if (holder != null) {
            IConnection connection = holder.get();
            holder.close();
            return connection;
        }

        //add default
        IConnection connection = new NettyConnection();
        connection.init(channel, false);
        connection.close();
        return connection;
    }

    @Override
    public void add(IConnection connection) {
        holders.putIfAbsent(connection.getChannel().id(),factory.create(connection));
    }

    @Override
    public int getConnNum() {
        return holders.size();
    }


    @Override
    public void destroy() {
        if (timer!=null){
            timer.stop();
        }
        holders.values().forEach(IConnectionHolder::close);
        holders.clear();
    }


    @FunctionalInterface
    public interface ConnectionHolderFactory{
        IConnectionHolder create(IConnection connection);
    }

    /**
     * 功能描述 心跳链接
     * @param:
     * @return:
     * @date: 2019/7/10 9:41
     */
    public class HeartBeatTimer implements IConnectionHolder,TimerTask {

        private IConnection connection;

        private int timeoutTimes = 0; //超时次数


        private HeartBeatTimer(IConnection connection) {
            this.connection = connection;
            startTimeout(); // 定时心跳任务
        }

        /**
         * 功能描述 放入时间轮
         * @param:
         * @return:
         * @date: 2019/7/10 10:26
         */
        void startTimeout(){
            if (Objects.nonNull(connection) && connection.isConnected()){
                int timeout = connection.getSessionContext().heartbeat;
                timer.newTimeout(this,timeout,TimeUnit.MILLISECONDS);
            }
        }


        @Override
        public void run(Timeout timeout) throws Exception {
            if (Objects.nonNull(connection) && connection.isConnected()){
                if (connection.isReadTimeout()) {
                    if (++timeoutTimes > CoreConfig.buildConfig().getHeartbeatTimeOutTimes()) {
                        connection.close();
                        Logs.HB.warn("client heartbeat timeout times={}, do close conn={}", timeoutTimes, connection);
                        return;
                    } else {
                        Logs.HB.info("client heartbeat timeout times={}, connection={}", timeoutTimes, connection);
                    }
                } else {
                    timeoutTimes = 0;
                }
            }
            startTimeout();
        }

        @Override
        public IConnection get() {
            return null;
        }

        @Override
        public void close() {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }
    }


    /**
     * 功能描述 心跳链接
     * @param:
     * @return:
     * @date: 2019/7/10 9:50
     */
    public class  SimpleConnectionHolder implements IConnectionHolder{

        private IConnection connection;

        private SimpleConnectionHolder(IConnection connection) {
            this.connection = connection;
        }

        @Override
        public IConnection get() {
            return connection;
        }

        @Override
        public void close() {
            if (Objects.nonNull(connection)){
                connection.close();
                connection = null;
            }
        }
    }

}
