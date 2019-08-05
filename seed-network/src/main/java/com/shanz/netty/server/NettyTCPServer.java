package com.shanz.netty.server;

import com.google.common.base.Strings;
import com.shanz.api.service.ServiceException;
import com.shanz.netty.codec.PacketDecoder;
import com.shanz.netty.codec.PacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName:NettyTCPServer
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/7/1 15:08
 * @Version:1.0
 **/
public abstract class NettyTCPServer {

    protected final int port; //端口

    protected final String host; //绑定地址

    protected EventLoopGroup bossGroup; // boss线程

    protected EventLoopGroup workerGroup; //工作线程

    protected  enum State {Created,Initialised,Starting,Started,Shutdown}

    protected final AtomicReference<State> serverState = new AtomicReference<>(State.Created);

    public NettyTCPServer(int port, String host) {
        this.port = port;
        this.host = host;
    }

    /**
     * 功能描述 启动
     * @param:
     * @return:
     * @date: 2019/7/1 15:24
     */
    public void start(){
        if (!serverState.compareAndSet(State.Initialised,State.Starting)){
            throw new ServiceException("Server already started or have not init");
        }

        createNioServer();
    }


    private void createServer(EventLoopGroup boss, EventLoopGroup work, ChannelFactory<? extends ServerChannel> channelFactory) {
        this.bossGroup = boss;
        this.workerGroup = work;

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(boss,work);

            b.channelFactory(channelFactory);

            b.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    initPipeline(channel.pipeline());
                }
            });
            
            initOptions(b);

            InetSocketAddress address = Strings.isNullOrEmpty(host) ? new InetSocketAddress(port) : new InetSocketAddress(host, port);

            b.bind(address).addListener(future -> {
                System.out.println("--->" + future.isSuccess());
            });

        } catch (Exception e){

        }

    }

    private void initOptions(ServerBootstrap b) {
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }

    private void initPipeline(ChannelPipeline pipeline) {
        pipeline.addLast("decoder", getDecoder());
        pipeline.addLast("encoder", getEncoder());
        pipeline.addLast("handler", getChannelHandler());

    }


    private void createNioServer() {
        if (bossGroup == null){
            NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(getBossThreadNum(),getBossThreadFactory(),SelectorProvider.provider());
            nioEventLoopGroup.setIoRatio(100);
            bossGroup = nioEventLoopGroup;
        }

        if (workerGroup == null) {
            NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(getWorkThreadNum(), getWorkThreadFactory(), SelectorProvider.provider());
            nioEventLoopGroup.setIoRatio(getIoRate());
            workerGroup = nioEventLoopGroup;
        }

        createServer(bossGroup,workerGroup,NioServerSocketChannel::new);

    }


    private void createEpollServer(){
        if (bossGroup == null) {
            EpollEventLoopGroup epollEventLoopGroup = new EpollEventLoopGroup(getBossThreadNum(), getBossThreadFactory());
            epollEventLoopGroup.setIoRatio(100);
            bossGroup = epollEventLoopGroup;
        }

        if (workerGroup == null) {
            EpollEventLoopGroup epollEventLoopGroup = new EpollEventLoopGroup(getWorkThreadNum(), getWorkThreadFactory());
            epollEventLoopGroup.setIoRatio(getIoRate());
            workerGroup = epollEventLoopGroup;
        }

        createServer(bossGroup, workerGroup, EpollServerSocketChannel::new);
    }

    protected ChannelHandler getDecoder() {
        return new PacketDecoder();
    }

    protected ChannelHandler getEncoder() {
        return PacketEncoder.INSTANCE;//每连上一个链接调用一次, 所有用单利
    }

    public abstract ChannelHandler getChannelHandler();

    protected ThreadFactory getBossThreadFactory() {
        return new DefaultThreadFactory(getBossThreadName());
    }

    protected ThreadFactory getWorkThreadFactory() {
        return new DefaultThreadFactory(getWorkThreadName());
    }

    protected int getBossThreadNum() {
        return 1;
    }

    protected int getWorkThreadNum() {
        return 0;
    }

    protected String getBossThreadName() {
        return "T-boss";
    }

    protected String getWorkThreadName() {
        return "T-worker";
    }

    protected int getIoRate() {
        return 70;
    }

}
