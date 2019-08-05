package com.shanz.netty.client;

import com.shanz.netty.codec.PacketDecoder;
import com.shanz.netty.codec.PacketEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;

/**
 * @ClassName:NettyTCPClient
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/7/1 16:07
 * @Version:1.0
 **/
public abstract class NettyTCPClient {

    private EventLoopGroup workerGroup;
    private Bootstrap bootstrap;

    protected void start(){
        createNioClient();
        createEpollClient();
    }

    public ChannelFuture connect(String host, int port) {
        return bootstrap.connect(new InetSocketAddress(host, port));
    }

//    public ChannelFuture connect(String host, int port, Listener listener) {
//        return bootstrap.connect(new InetSocketAddress(host, port)).addListener(f -> {
//            if (f.isSuccess()) {
//                if (listener != null) listener.onSuccess(port);
//                LOGGER.info("start netty client success, host={}, port={}", host, port);
//            } else {
//                if (listener != null) listener.onFailure(f.cause());
//                LOGGER.error("start netty client failure, host={}, port={}", host, port, f.cause());
//            }
//        });
//    }

    private void createNioClient() {
        NioEventLoopGroup workerGroup  = new NioEventLoopGroup(getWorkThreadNum(),new DefaultThreadFactory("T-tcp-client"),SelectorProvider.provider());
        workerGroup.setIoRatio(getIoRate());
        createClient(workerGroup, EpollSocketChannel::new);
    }

    private void createEpollClient() {
        EpollEventLoopGroup workerGroup = new EpollEventLoopGroup(
                getWorkThreadNum(), new DefaultThreadFactory("T-tcp-client")
        );
        workerGroup.setIoRatio(getIoRate());
        createClient(workerGroup, EpollSocketChannel::new);
    }


    protected void createClient(EventLoopGroup workerGroup, ChannelFactory<? extends Channel> channelFactory){
        this.workerGroup = workerGroup;
        this.bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)//
                .option(ChannelOption.SO_REUSEADDR, true)//
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)//
                .channelFactory(channelFactory);
        bootstrap.handler(new ChannelInitializer<Channel>() { // (4)
            @Override
            public void initChannel(Channel ch) throws Exception {
                initPipeline(ch.pipeline());
            }
        });
        initOptions(bootstrap);

    }

    protected void initOptions(Bootstrap b) {
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 4000);
        b.option(ChannelOption.TCP_NODELAY, true);
    }


    protected void initPipeline(ChannelPipeline pipeline) {
        pipeline.addLast("decoder", getDecoder());
        pipeline.addLast("encoder", getEncoder());
        pipeline.addLast("handler", getChannelHandler());
    }

    protected ChannelHandler getDecoder() {
        return new PacketDecoder();
    }

    protected ChannelHandler getEncoder() {
        return PacketEncoder.INSTANCE;
    }

    protected int getIoRate() {
        return 50;
    }

    protected int getWorkThreadNum() {
        return 1;
    }

    public abstract ChannelHandler getChannelHandler();
}
