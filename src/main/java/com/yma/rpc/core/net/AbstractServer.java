package com.yma.rpc.core.net;

import com.yma.rpc.core.provider.RpcProviderFactory;
import com.yma.rpc.serializer.AbstractSerializer;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 11:09:20
 */
public abstract class AbstractServer {
    /**
     * 初始化服务器
     * @param address 监听地址
     * @param rpcProviderFactory 调用实现类注册工厂
     * @throws Exception 异常
     */
    public abstract void init(String address, RpcProviderFactory rpcProviderFactory) throws Exception;

    /**
     * 关闭服务器
     */
    public abstract void close();

}
