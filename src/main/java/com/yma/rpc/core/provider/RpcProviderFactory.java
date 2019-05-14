package com.yma.rpc.core.provider;

import com.yma.rpc.configuration.RpcConfig;
import com.yma.rpc.constant.CommonConstants;
import com.yma.rpc.core.net.AbstractServer;
import com.yma.rpc.core.provider.annotation.Rpc;
import com.yma.rpc.core.provider.registry.BeanRegistry;
import com.yma.rpc.util.ClassScanUtil;
import com.yma.rpc.util.IpUtil;
import com.yma.rpc.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-13 14:04:17
 */
@Slf4j
public class RpcProviderFactory {
    private RpcConfig rpcConfig;
    private ThreadPoolExecutor serverExecutor;
    private ConcurrentHashMap<String, Object> providerObjects;
    private BeanRegistry beanRegistry;
    private String address;

    public Object get(String interfaceName) {
        return providerObjects.get(interfaceName);
    }

    public void put(String interfaceName, Object object) {
        providerObjects.put(interfaceName, object);
    }

    public void putAll(Map<String, Object> list) {
        providerObjects.putAll(list);
    }

    public RpcProviderFactory(RpcConfig rpcConfig, BeanRegistry beanRegistry) {
        this.beanRegistry = beanRegistry;
        this.address = IpUtil.buildLocalHost(rpcConfig.getPort());
        this.rpcConfig = rpcConfig;

        init();
    }

    public RpcConfig getRpcConfig() {
        return rpcConfig;
    }

    public void destroy() {
        if (Objects.nonNull(serverExecutor)) {
            serverExecutor.shutdown();
        }
        rpcConfig.getNetType().getServerImpl().close();
        log.info(">>>>>>RpcProviderFactory destroy");
    }

    private void init() {
        this.providerObjects = new ConcurrentHashMap<>(32);
        this.serverExecutor = ThreadPoolUtil.newThreadPool(CommonConstants.SERVER_EXECUTOR_PREFIX, new ArrayBlockingQueue<>(12));

        putAll(beanRegistry.scan(getRpcConfig().getBasePackage()));
        final AbstractServer server = rpcConfig.getNetType().getServerImpl();
        serverExecutor.execute(() -> {
            try {
                server.init(address, this);
            } catch (Exception e) {
                server.close();
                throw new RuntimeException(e);
            }
        });
    }
}
