package com.yma.rpc.configuration;

import com.yma.rpc.core.invoker.router.Router;
import com.yma.rpc.core.net.NetType;
import com.yma.rpc.registry.ServiceRegistry;
import com.yma.rpc.serializer.AbstractSerializer;
import lombok.Data;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-10 17:33:30
 */
@Data
public class RpcConfig {
    private String registryCenterAddress;
    private int port;
    private String token;
    private String basePackage;
    private Router router;
    private ServiceRegistry serviceRegistry;
    private NetType netType;
    private AbstractSerializer serializer;


    public static RpcConfig.RpcConfigBuilder builder(){
        return new RpcConfig.RpcConfigBuilder();
    }

    public static class RpcConfigBuilder{
        private String registryCenterAddress;
        private int port;
        private String basePackage;
        private Router router;
        private ServiceRegistry serviceRegistry;
        private NetType netType;
        private AbstractSerializer serializer;

        public RpcConfigBuilder registryCenterAddress(String registryCenterAddress){
            this.registryCenterAddress = registryCenterAddress;
            return this;
        }

        public RpcConfigBuilder port(int port){
            this.port = port;
            return this;
        }

        public RpcConfigBuilder basePackage(String basePackage){
            this.basePackage = basePackage;
            return this;
        }

        public RpcConfigBuilder router(Router router){
            this.router = router;
            return this;
        }

        public RpcConfigBuilder serviceRegistry(ServiceRegistry serviceRegistry){
            this.serviceRegistry = serviceRegistry;
            return this;
        }

        public RpcConfigBuilder netType(NetType netType){
            this.netType = netType;
            return this;
        }

        public RpcConfigBuilder serializer(AbstractSerializer serializer){
            this.serializer = serializer;
            return this;
        }

        public RpcConfig build(){
            if(this.port <= 0){
                this.port = -1;
            }
            return new RpcConfig(registryCenterAddress,
                    port,
                    basePackage,
                    router,
                    serviceRegistry,
                    netType,
                    serializer);
        }
    }

    private RpcConfig(){

    }

    public RpcConfig(String registryCenterAddress,
                     int port,
                     String basePackage,
                     Router router,
                     ServiceRegistry serviceRegistry,
                     NetType netType,
                     AbstractSerializer serializer) {
        this.registryCenterAddress = registryCenterAddress;
        this.port = port;
        this.basePackage = basePackage;
        this.router = router;
        this.serviceRegistry = serviceRegistry;
        this.netType = netType;
        this.serializer = serializer;
    }
}
