package com.yma.rpc.core.net;

import com.yma.rpc.core.provider.RpcProviderFactory;
import com.yma.rpc.serializer.AbstractSerializer;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 11:09:20
 */
public abstract class AbstractServer {

    public abstract void init(String address, AbstractSerializer serializer, RpcProviderFactory rpcProviderFactory) throws Exception;
    public abstract void close();

}
