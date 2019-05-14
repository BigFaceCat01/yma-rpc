package com.yma.rpc.core.net.impl.netty.client;

import com.yma.rpc.core.invoker.RpcInvokerFactory;
import com.yma.rpc.core.net.AbstractClient;
import com.yma.rpc.core.net.connect.AbstractConnect;
import com.yma.rpc.core.net.param.RpcRequest;
import com.yma.rpc.serializer.AbstractSerializer;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 11:26:39
 */
public class NettyClient extends AbstractClient {

    @Override
    public void send(String address, RpcRequest rpcRequest, RpcInvokerFactory rpcInvokerFactory) throws Exception{
        AbstractConnect.doSend(address,rpcRequest,rpcInvokerFactory);
    }

    @Override
    public void close() {
        AbstractConnect.doClose();
    }
}
