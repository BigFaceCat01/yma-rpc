package com.yma.rpc.core.net;

import com.yma.rpc.core.net.connect.AbstractConnect;
import com.yma.rpc.core.net.impl.netty.client.NettyClient;
import com.yma.rpc.core.net.impl.netty.client.NettyConnectClient;
import com.yma.rpc.core.net.impl.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 17:52:30
 */
@Slf4j
public enum NetType {
    /**
     * netty 作为网络传输工具
     */
    NETTY;

    private final AbstractServer serverImpl;
    private final AbstractClient clientImpl;
    private final Class<? extends AbstractConnect> connectImpl;

    NetType() {
        this.serverImpl = new NettyServer();
        this.clientImpl = new NettyClient();
        this.connectImpl = NettyConnectClient.class;
    }

    public AbstractServer getServerImpl() {
        return serverImpl;
    }

    public AbstractClient getClientImpl() {
        return clientImpl;
    }

    public Class<? extends AbstractConnect> getConnectImpl() {
        return connectImpl;
    }
}
