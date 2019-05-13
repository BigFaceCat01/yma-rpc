package com.yma.rpc.core.net.connect;

import com.yma.rpc.core.invoker.RpcInvokerFactory;
import com.yma.rpc.core.net.param.RpcRequest;
import com.yma.rpc.serializer.AbstractSerializer;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 11:52:51
 */
public abstract class AbstractConnect {

    private static ConcurrentHashMap<String, AbstractConnect> connectPool;
    private static ConcurrentHashMap<String, Object> lockPool = new ConcurrentHashMap<>(16);

    /**
     * 初始化客户端连接
     * @param address 连接的服务器的地址
     * @param serializer 数据序列化方式
     * @param rpcInvokerFactory 调用工程
     * @throws Exception 异常
     */
    public abstract void init(String address, AbstractSerializer serializer, RpcInvokerFactory rpcInvokerFactory) throws Exception;

    /**
     * 连接关闭实现
     */
    public abstract void close();

    /**
     * 发送一个调用请求
     * @param rpcRequest 请求信息
     * @throws Exception 异常
     */
    public abstract void send(RpcRequest rpcRequest) throws Exception;

    /**
     * 判断连接是否还能使用，不能则会移除
     * @return 布尔
     */
    public abstract boolean isAlive();


    public static void doSend(String address, RpcRequest rpcRequest, AbstractSerializer serializer, Class<? extends AbstractConnect> connectImpl, RpcInvokerFactory rpcInvokerFactory) throws Exception {
        AbstractConnect connect = getConnect(address, serializer, connectImpl,rpcInvokerFactory);
        connect.send(rpcRequest);
    }


    private static AbstractConnect getConnect(String address, AbstractSerializer serializer, Class<? extends AbstractConnect> connectImpl, RpcInvokerFactory rpcInvokerFactory) throws Exception {

        if (Objects.isNull(connectPool)) {
            synchronized (AbstractConnect.class) {
                connectPool = new ConcurrentHashMap<>(16);
            }
        }

        AbstractConnect connect = connectPool.get(address);
        if (Objects.nonNull(connect) && connect.isAlive()) {
            return connect;
        }
        Object lock = lockPool.get(address);
        if (Objects.isNull(lock)) {
            lockPool.putIfAbsent(address, new Object());
            lock = lockPool.get(address);
        }

        synchronized (lock) {
            connect = connectPool.get(address);
            if (Objects.nonNull(connect) && connect.isAlive()) {
                return connect;
            }
            if (Objects.nonNull(connect)) {
                //表示isAlive为false，移除有问题的通道
                connect.close();
                connectPool.remove(address);
            }
            AbstractConnect con = connectImpl.newInstance();
            con.init(address, serializer,rpcInvokerFactory);
            connectPool.put(address, con);
            return con;
        }
    }

}
