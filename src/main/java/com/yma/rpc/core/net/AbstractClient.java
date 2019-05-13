package com.yma.rpc.core.net;

import com.yma.rpc.core.invoker.RpcInvokerFactory;
import com.yma.rpc.core.net.connect.AbstractConnect;
import com.yma.rpc.core.net.param.RpcRequest;
import com.yma.rpc.serializer.AbstractSerializer;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 11:09:26
 */
public abstract class AbstractClient {
    public abstract void send(String address, RpcRequest rpcRequest, AbstractSerializer serializer, Class<? extends AbstractConnect> connectImpl, RpcInvokerFactory rpcInvokerFactory) throws Exception;
}
