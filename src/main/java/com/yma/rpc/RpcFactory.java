package com.yma.rpc;


import com.yma.rpc.configuration.RpcConfig;
import com.yma.rpc.core.invoker.RpcInvokerFactory;
import com.yma.rpc.core.provider.RpcProviderFactory;
import com.yma.rpc.core.provider.annotation.Rpc;
import com.yma.rpc.registry.ServiceRegistry;
import com.yma.rpc.util.ClassScanUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 10:57:30
 */
@Slf4j
public class RpcFactory {
    private RpcInvokerFactory rpcInvokerFactory;
    private RpcConfig rpcConfig;
    private RpcProviderFactory rpcProviderFactory;
    private volatile boolean initialize;



    public static RpcFactory.RpcFactoryBuilder builder(){
        return new RpcFactory.RpcFactoryBuilder();
    }

    private void init(){
        if(initialize){
            return;
        }
        synchronized (RpcFactory.class) {
            if (initialize) {
                return;
            }
            initInvokerFactory();

            initServiceRegistry();

            if(rpcConfig.getPort() != -1) {
                initProviderFactory();
            }

            initialize = true;
        }
    }


    private void initInvokerFactory(){
        rpcInvokerFactory = new RpcInvokerFactory(rpcConfig);

    }

    private void initProviderFactory(){
        rpcProviderFactory = new RpcProviderFactory(rpcConfig,()->{
            Map<String,Object> res = new HashMap<>(32);
            ClassScanUtil.scan(rpcConfig.getBasePackage(),(source)->{
                Rpc rpc = source.getDeclaredAnnotation(Rpc.class);
                if(Objects.isNull(rpc)){
                    return;
                }
                String iface = rpc.iface();
                String interfaceName;
                if("".equals(iface.trim())){
                    interfaceName = source.getInterfaces()[0].getName();
                }else {
                    interfaceName = rpc.iface();
                }
                Object o = null;
                try {
                    o = source.newInstance();
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
                res.put(interfaceName, o);
            });
            return res;
        });
    }

    private void initServiceRegistry(){
        rpcConfig.getServiceRegistry().start();
    }

    public static class RpcFactoryBuilder{
        private RpcConfig rpcConfig;

        public RpcFactoryBuilder config(RpcConfig rpcConfig){
            this.rpcConfig = rpcConfig;
            return this;
        }

        public RpcFactory build(){
            return new RpcFactory(this.rpcConfig);
        }
    }


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

    private RpcFactory(RpcConfig rpcConfig){
        this.rpcConfig = rpcConfig;
        init();
    }
}
