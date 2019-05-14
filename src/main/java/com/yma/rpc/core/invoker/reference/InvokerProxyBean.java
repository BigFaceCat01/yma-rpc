package com.yma.rpc.core.invoker.reference;

import com.yma.rpc.RpcFactory;
import com.yma.rpc.core.callback.BaseCallback;
import com.yma.rpc.core.invoker.RpcInvokerFactory;
import com.yma.rpc.core.invoker.annotation.RpcApi;
import com.yma.rpc.core.net.param.RpcRequest;
import com.yma.rpc.exception.RpcException;
import com.yma.rpc.util.SnowFlakesUtil;

import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * 用于生产代理bean
 * @author Created by huang xiao bao
 * @date 2019-05-11 11:05:38
 */
public class InvokerProxyBean<T> {
    private String serviceName;
    private Class<?>[] interfaces;
    private RpcRequest rpcRequest;
    private Object proxyBean;
    private RpcFactory rpcFactory;
    private BaseCallback callback;
    private boolean sync;


    public static <T> InvokerProxyBean<T> newProxyBean(Class<T> source, RpcFactory rpcFactory, BaseCallback resultCallBack){
        RpcApi rpcApi = source.getDeclaredAnnotation(RpcApi.class);
        if(Objects.isNull(rpcApi)){
            throw new RpcException(source.getName() + ": @RpcApi not present");
        }
        RpcRequest rpcRequest = RpcRequest.builder()
                .className(source.getName())
                .build();
        rpcFactory.getServiceRegistry().refreshData(rpcApi.serviceName(),rpcApi.address());
        return new InvokerProxyBean<>(
                rpcFactory
                ,rpcApi.serviceName()
                ,rpcRequest,
                resultCallBack,
                Objects.isNull(resultCallBack),
                new Class<?>[]{source});
    }

    public static <T> InvokerProxyBean<T> newProxyBean(Class<T> source,RpcFactory rpcFactory){
        return newProxyBean(source,rpcFactory,null);
    }

    @SuppressWarnings("unchecked")
    public T getObject(){
        if(Objects.nonNull(proxyBean)){
            return (T) proxyBean;
        }
        synchronized (InvokerProxyBean.class) {
            final RpcInvokerFactory rpcInvokerFactory = rpcFactory.getRpcInvokerFactory();
            if(Objects.nonNull(proxyBean)){
                return (T) proxyBean;
            }
            this.proxyBean = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    interfaces,
                    (proxy, method, args) -> {
                        RpcRequest rpcRequest = getRpcRequest();
                        rpcRequest.setRequestId(SnowFlakesUtil.nextId());
                        rpcRequest.setMethodName(method.getName());
                        rpcRequest.setCreateTime(System.currentTimeMillis());
                        rpcRequest.setMethodParam(method.getParameterTypes());
                        rpcRequest.setParams(args);
                        if (sync) {
                            //同步调用
                            return rpcInvokerFactory.syncSend(rpcRequest, serviceName);
                        } else {
                            //异步调用
                            rpcInvokerFactory.asyncSend(rpcRequest, serviceName, callback);
                            return null;
                        }
                    });
            return (T)proxyBean;
        }

    }

    private RpcRequest getRpcRequest() {
        return rpcRequest;
    }

    private InvokerProxyBean(RpcFactory rpcFactory, String serviceName, RpcRequest rpcRequest,BaseCallback callback,boolean sync,Class<?>[] interfaces){
        this.rpcFactory = rpcFactory;
        this.serviceName = serviceName;
        this.rpcRequest = rpcRequest;
        this.callback = callback;
        this.sync = sync;
        this.interfaces = interfaces;
    }
}
