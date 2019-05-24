package com.yma.rpc.history.v1.impl;

import com.yma.rpc.history.v1.NetInvocationHandler;
import com.yma.rpc.history.v1.ProxyFactory;

import java.lang.reflect.Proxy;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-22 09:40:55
 */
public class JdkProxyFactory<P> implements ProxyFactory<P> {
    @Override
    @SuppressWarnings("unchecked")
    public P newProxy(Class<P> target) {
        return (P) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                target.getInterfaces(),
                new NetInvocationHandler()
        );
    }
}
