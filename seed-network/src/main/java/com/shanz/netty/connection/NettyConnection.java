package com.shanz.netty.connection;

import com.shanz.api.connection.IConnection;
import com.shanz.api.connection.SessionContext;
import com.shanz.api.protocol.Packet;
import com.shanz.security.RsaCipher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @ClassName:NettyConnection
 * @Description: 链接实际类
 * @Author: shanzheng
 * @Date: 2019/7/10 10:38
 * @Version:1.0
 **/
public class NettyConnection implements IConnection {

    private Channel channel;    //netty chanel

    private SessionContext sessionContext;  //session内容

    private long lastWriteTime = System.currentTimeMillis();  //最新的写操作时间 用来判断心跳超时

    private long lastReadTime;  //最新的写操作时间 用来判断心跳超时

    private volatile byte status = STATUS_NEW;

    @Override
    public void init(Channel channel, boolean security) {
        this.channel = channel;
        this.sessionContext = new SessionContext();
        lastReadTime = System.currentTimeMillis();
        status = STATUS_CONNECTED;
        if (security){
            this.sessionContext.changeCipher(RsaCipher.create());
        }

    }

    @Override
    public SessionContext getSessionContext() {
        return sessionContext;
    }

    @Override
    public void setSessionContext(SessionContext context) {
        this.sessionContext = context;
    }

    @Override
    public ChannelFuture send(Packet packet) {
        return send(packet,null);
    }

    @Override
    public ChannelFuture send(Packet packet, ChannelFutureListener listener) {
        if (channel.isActive()){

            ChannelFuture future =  channel.writeAndFlush(packet);

            if (listener != null) {
                future.addListener(listener);
            }

            if (channel.isWritable()) {
                return future;
            }

            if (!future.channel().eventLoop().inEventLoop()) {
                future.awaitUninterruptibly(100);
            }
            return future;

        }else {
            return channel.close();
        }
    }

    @Override
    public String getId() {
        return channel.id().asShortText();
    }

    @Override
    public ChannelFuture close() {
        if (status == STATUS_DISCONNECTED) return null;
        this.status = STATUS_DISCONNECTED;
        return this.channel.close();
    }

    @Override
    public boolean isConnected() {
        return status == STATUS_CONNECTED;
    }

    @Override
    public boolean isReadTimeout() {
        return System.currentTimeMillis() - lastReadTime > sessionContext.heartbeat + 1000;
    }

    @Override
    public boolean isWriteTimeout() {
        return System.currentTimeMillis() - lastWriteTime > sessionContext.heartbeat - 1000;
    }

    @Override
    public void updateLastReadTime() {
        lastReadTime = System.currentTimeMillis();
    }

    @Override
    public void updateLastWriteTime() {
        lastWriteTime = System.currentTimeMillis();
    }

    @Override
    public Channel getChannel() {
        return channel;
    }
}
