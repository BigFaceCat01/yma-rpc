package com.yma.rpc;


import com.yma.rpc.configuration.RpcConfig;
import com.yma.rpc.core.invoker.RpcInvokerFactory;
import com.yma.rpc.core.provider.RpcProviderFactory;
import com.yma.rpc.core.provider.registry.BeanRegistry;
import com.yma.rpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 10:57:30
 */
@Slf4j
public final class RpcFactory {
    private RpcInvokerFactory rpcInvokerFactory;
    private RpcConfig rpcConfig;
    private RpcProviderFactory rpcProviderFactory;
    private volatile boolean initialize;

    public RpcInvokerFactory getRpcInvokerFactory() {
        return rpcInvokerFactory;
    }

    public RpcConfig getRpcConfig() {
        return rpcConfig;
    }

    public ServiceRegistry getServiceRegistry() {
        return rpcConfig.getServiceRegistry();
    }

    public RpcProviderFactory getRpcProviderFactory() {
        return rpcProviderFactory;
    }

    public RpcFactory(RpcConfig rpcConfig) {
        this.rpcConfig = rpcConfig;
        init();
    }

    private void init() {
        if (initialize) {
            log.info("RpcFactory already exists");
            return;
        }
        synchronized (RpcFactory.class) {
            if (initialize) {
                log.info("RpcFactory already exists");
                return;
            }
            switch (rpcConfig.getRpcRole()) {
                case BOTH:
                    initInvokerFactory();
                    initServiceRegistry();
                    initProviderFactory(rpcConfig.getBeanRegistry());
                    break;
                case CONSUMER:
                    initInvokerFactory();
                    initServiceRegistry();
                    break;
                case PROVIDER:
                    initProviderFactory(rpcConfig.getBeanRegistry());
                    break;
                default:
                    break;
            }
            initialize = true;
        }
    }

    public void destroy() {
        if(!initialize){
            return;
        }
        synchronized (RpcFactory.class){
            if(!initialize){
                return;
            }
            rpcInvokerFactory.destroy();
            rpcProviderFactory.destroy();
            rpcConfig = null;
        }
    }


    private void initInvokerFactory() {
        rpcInvokerFactory = new RpcInvokerFactory(rpcConfig);

    }

    private void initProviderFactory(BeanRegistry beanRegistry) {
        rpcProviderFactory = new RpcProviderFactory(rpcConfig, beanRegistry);
    }

    private void initServiceRegistry() {
        rpcConfig.getServiceRegistry().start();
    }
}
