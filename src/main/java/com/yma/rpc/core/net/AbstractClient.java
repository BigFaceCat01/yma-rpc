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
    /**
     * 发送一个请求
     * @param address 请求地址
     * @param rpcRequest 请求信息
     * @param rpcInvokerFactory 调用工程
     * @throws Exception 异常
     */
    public abstract void send(String address, RpcRequest rpcRequest, RpcInvokerFactory rpcInvokerFactory) throws Exception;

    /**
     * 关闭客户端
     */
    public abstract void close();
}
