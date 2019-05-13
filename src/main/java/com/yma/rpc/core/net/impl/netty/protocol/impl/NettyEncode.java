package com.yma.rpc.core.net.impl.netty.protocol.impl;

import com.yma.rpc.serializer.AbstractSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-07 09:17:40
 */
public class NettyEncode extends MessageToByteEncoder<Object> {

    private AbstractSerializer serializer;

    public NettyEncode(AbstractSerializer serializer){
        this.serializer = serializer;
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] data = serializer.serialize(msg);
        out.writeInt(data.length);
        out.writeBytes(data);
        ctx.flush();
    }
}
