package com.yma.rpc.core.net.impl.netty.client;

import com.yma.rpc.core.invoker.RpcInvokerFactory;
import com.yma.rpc.core.net.param.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 13:50:30
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private RpcInvokerFactory rpcInvokerFactory;

    public NettyClientHandler(RpcInvokerFactory rpcInvokerFactory) {
        this.rpcInvokerFactory = rpcInvokerFactory;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        rpcInvokerFactory.notifyInvokerFuture(msg.getRequestId(),msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("connect error : {}",ctx.name(),cause);
    }
}
