package com.yma.rpc.history.v1;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-22 09:37:48
 */
public interface ProxyFactory<P> {
    /**
     * 返回一个代理对象
     * @param target 被代理的对象
     * @return 代理
     */
    P newProxy(Class<P> target);
}
