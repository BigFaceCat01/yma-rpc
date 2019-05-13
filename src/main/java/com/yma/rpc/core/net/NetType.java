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
    NETTY(NettyServer.class,NettyClient.class,NettyConnectClient.class);



    private final Class<? extends AbstractServer> serverImpl;
    private final Class<? extends AbstractClient> clientImpl;
    private final Class<? extends AbstractConnect> connectImpl;

    NetType(Class<? extends AbstractServer> serverImpl, Class<? extends AbstractClient> clientImpl, Class<? extends AbstractConnect> connectImpl) {
        this.serverImpl = serverImpl;
        this.clientImpl = clientImpl;
        this.connectImpl = connectImpl;
    }

    public AbstractServer getServerImpl() {
        synchronized (this.serverImpl){
            try {
                return serverImpl.newInstance();
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return null;
            }
        }
    }

    public AbstractClient getClientImpl() {
        synchronized (this.clientImpl){
            try {
                return clientImpl.newInstance();
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return null;
            }
        }
    }

    public Class<? extends AbstractConnect> getConnectImpl() {
        return connectImpl;
    }
}
