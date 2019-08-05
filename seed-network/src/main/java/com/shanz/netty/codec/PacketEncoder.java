package com.shanz.netty.codec;

import com.shanz.api.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName:PacketEncoder
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/7/1 15:59
 * @Version:1.0
 **/
public final class PacketEncoder extends MessageToByteEncoder<Packet> {


    public static final PacketEncoder INSTANCE = new PacketEncoder();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {

    }
}
