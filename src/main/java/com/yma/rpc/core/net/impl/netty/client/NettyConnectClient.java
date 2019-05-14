package com.yma.rpc.core.net.impl.netty.client;

import com.yma.rpc.core.invoker.RpcInvokerFactory;
import com.yma.rpc.core.net.connect.AbstractConnect;
import com.yma.rpc.core.net.impl.netty.protocol.impl.NettyDecode;
import com.yma.rpc.core.net.impl.netty.protocol.impl.NettyEncode;
import com.yma.rpc.core.net.param.RpcRequest;
import com.yma.rpc.core.net.param.RpcResponse;
import com.yma.rpc.serializer.AbstractSerializer;
import com.yma.rpc.util.IpUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 15:41:42
 */
@Slf4j
public class NettyConnectClient extends AbstractConnect {

    private EventLoopGroup workGroup;
    private Channel channel;
    private String address;

    @Override
    public void init(String address, RpcInvokerFactory rpcInvokerFactory) throws Exception{
        this.address = address;
        Object[] ipPort = IpUtil.parseIpPort(address);
        String host = (String) ipPort[0];
        int port = (int) ipPort[1];


        Bootstrap client = new Bootstrap();

        this.workGroup = new NioEventLoopGroup();
        AbstractSerializer serializer = rpcInvokerFactory.getRpcConfig().getSerializer();
        client.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new NettyDecode(RpcResponse.class, serializer))
                                .addLast(new NettyEncode(serializer))
                                .addLast(new NettyClientHandler(rpcInvokerFactory));
                    }
                });
        this.channel = client.connect((new InetSocketAddress(host, port)))
                .sync()
                .channel();
    }

    @Override
    public void close() {
        if(Objects.nonNull(workGroup)) {
            workGroup.shutdownGracefully();
        }
        if(Objects.nonNull(workGroup)){
            this.channel.close();
        }
        log.info(">>>>>>The client disconnects at {}",address);
    }

    @Override
    public void send(RpcRequest rpcRequest) throws Exception{
        channel.writeAndFlush(rpcRequest).sync();
    }

    @Override
    public boolean isAlive() {
        return channel.isActive();
    }
}
