package com.yma.rpc.configuration;

import com.yma.rpc.constant.CommonConstants;
import com.yma.rpc.constant.RpcRole;
import com.yma.rpc.core.invoker.router.Router;
import com.yma.rpc.core.invoker.router.impl.RandomRouter;
import com.yma.rpc.core.net.NetType;
import com.yma.rpc.core.provider.RpcProviderFactory;
import com.yma.rpc.core.provider.registry.BeanRegistry;
import com.yma.rpc.core.provider.registry.impl.DefaultBeanRegistry;
import com.yma.rpc.exception.RpcException;
import com.yma.rpc.registry.ServiceRegistry;
import com.yma.rpc.registry.impl.LocalServiceRegistry;
import com.yma.rpc.serializer.AbstractSerializer;
import com.yma.rpc.serializer.impl.HessianSerializer;
import com.yma.rpc.util.StringUtil;
import lombok.Data;

import java.util.Objects;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-10 17:33:30
 */
@Data
public class RpcConfig {
    private String registryCenterAddress;
    private int port;
    private RpcRole rpcRole;
    private String token;
    private String basePackage;
    private Router router;
    private BeanRegistry beanRegistry;
    private int timeout;
    private ServiceRegistry serviceRegistry;
    private NetType netType;
    private AbstractSerializer serializer;


    public static RpcConfig.RpcConfigBuilder builder() {
        return new RpcConfig.RpcConfigBuilder();
    }

    public static class RpcConfigBuilder {
        private String registryCenterAddress;
        private BeanRegistry beanRegistry;
        private int port;
        private RpcRole rpcRole;
        private int timeout;
        private String basePackage;
        private Router router;
        private ServiceRegistry serviceRegistry;
        private NetType netType;
        private AbstractSerializer serializer;

        public RpcConfigBuilder registryCenterAddress(String registryCenterAddress) {
            this.registryCenterAddress = registryCenterAddress;
            return this;
        }

        public RpcConfigBuilder port(int port) {
            this.port = port;
            return this;
        }

        public RpcConfigBuilder basePackage(String basePackage) {
            this.basePackage = basePackage;
            return this;
        }

        public RpcConfigBuilder router(Router router) {
            this.router = router;
            return this;
        }

        public RpcConfigBuilder serviceRegistry(ServiceRegistry serviceRegistry) {
            this.serviceRegistry = serviceRegistry;
            return this;
        }

        public RpcConfigBuilder netType(NetType netType) {
            this.netType = netType;
            return this;
        }

        public RpcConfigBuilder serializer(AbstractSerializer serializer) {
            this.serializer = serializer;
            return this;
        }

        public RpcConfigBuilder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public RpcConfigBuilder scan(BeanRegistry beanRegistry) {
            this.beanRegistry = beanRegistry;
            return this;
        }

        public RpcConfigBuilder rpcRole(RpcRole rpcRole) {
            this.rpcRole = rpcRole;
            return this;
        }

        public RpcConfig build() throws RpcException {
            checkConfig();

            return new RpcConfig(registryCenterAddress,
                    port,
                    rpcRole,
                    timeout,
                    beanRegistry,
                    basePackage,
                    router,
                    serviceRegistry,
                    netType,
                    serializer);

        }

        private void checkConfig() throws RpcException {
            if (StringUtil.isEmpty(basePackage)) {
                throw new RpcException("basePackage must not null");
            }
            if (rpcRole == RpcRole.PROVIDER || rpcRole == RpcRole.BOTH) {
                if (port <= 0) {
                    throw new RpcException("role of provider must have a port");
                }
            }
            if (Objects.isNull(router)) {
                router = new RandomRouter();
            }
            if (Objects.isNull(serviceRegistry)) {
                serviceRegistry = new LocalServiceRegistry();
            }
            if (Objects.isNull(serializer)) {
                serializer = new HessianSerializer();
            }
            if (Objects.isNull(netType)) {
                netType = NetType.NETTY;
            }
            if (Objects.isNull(beanRegistry)) {
                beanRegistry = new DefaultBeanRegistry();
            }
            if (timeout <= 0) {
                timeout = CommonConstants.TIMEOUT_MILLISECONDS;
            }
            if (Objects.isNull(rpcRole)) {
                throw new RpcException("role must not null");
            }
        }
    }

    private RpcConfig(String registryCenterAddress,
                      int port,
                      RpcRole rpcRole,
                      int timeout,
                      BeanRegistry beanRegistry,
                      String basePackage,
                      Router router,
                      ServiceRegistry serviceRegistry,
                      NetType netType,
                      AbstractSerializer serializer) {
        this.registryCenterAddress = registryCenterAddress;
        this.port = port;
        this.rpcRole = rpcRole;
        this.beanRegistry = beanRegistry;
        this.basePackage = basePackage;
        this.router = router;
        this.serviceRegistry = serviceRegistry;
        this.netType = netType;
        this.serializer = serializer;
        this.timeout = timeout;
    }
}
