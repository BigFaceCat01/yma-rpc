package com.yma.rpc.core.invoker;

import com.yma.rpc.configuration.RpcConfig;
import com.yma.rpc.constant.CommonConstants;
import com.yma.rpc.core.callback.BaseCallback;
import com.yma.rpc.core.invoker.router.Router;
import com.yma.rpc.core.net.AbstractClient;
import com.yma.rpc.core.net.connect.AbstractConnect;
import com.yma.rpc.core.net.param.RpcFutureResponse;
import com.yma.rpc.core.net.param.RpcRequest;
import com.yma.rpc.core.net.param.RpcResponse;
import com.yma.rpc.registry.ServiceRegistry;
import com.yma.rpc.serializer.AbstractSerializer;
import com.yma.rpc.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 调用工厂
 * @author Created by huang xiao bao
 * @date 2019-05-11 11:04:11
 */
@Slf4j
public class RpcInvokerFactory {
    private RpcConfig rpcConfig;
    /**
     * 用于存储调用，使用请求id作为key，返回结果后会放入到pool中，通过请求id拿回对应的response
     */
    private ConcurrentHashMap<Long, RpcFutureResponse> futureResponsePool;
    /**
     * 回调任务执行线程池
     */
    private volatile ThreadPoolExecutor callbackExecutor;

    public void asyncSend(RpcRequest rpcRequest, String serviceName, BaseCallback callback) {
        String address = rpcConfig.getRouter().router(serviceName, rpcConfig.getServiceRegistry());
        RpcFutureResponse rpcFutureResponse = new RpcFutureResponse();
        try {
            rpcConfig.getNetType().getClientImpl().send(address, rpcRequest, this);
            setInvokerFuture(rpcRequest.getRequestId(), rpcFutureResponse);
            RpcResponse rpcResponse = rpcFutureResponse.get(100, TimeUnit.MILLISECONDS);
            callbackExecutor.execute(() -> callback.run(rpcResponse));
        } catch (TimeoutException e) {
            log.error("invoke time out : {}", rpcRequest, e);
        } catch (Exception e) {
            log.error("invoke error : {}", rpcRequest, e);
        }
    }

    public Object syncSend(RpcRequest rpcRequest, String serviceName) {
        String address = rpcConfig.getRouter().router(serviceName, rpcConfig.getServiceRegistry());
        RpcFutureResponse rpcFutureResponse = new RpcFutureResponse();
        try {
            rpcConfig.getNetType().getClientImpl().send(address, rpcRequest, this);
            setInvokerFuture(rpcRequest.getRequestId(), rpcFutureResponse);
            RpcResponse rpcResponse = rpcFutureResponse.get(100, TimeUnit.MILLISECONDS);
            log.info("调用结果：{}",rpcResponse);
            return rpcResponse.getResult();
        } catch (TimeoutException e) {
            log.error("invoke time out : {}", rpcRequest, e);
        } catch (Exception e) {
            log.error("invoke error : {}", rpcRequest, e);
        }
        return null;
    }


    private void setInvokerFuture(Long requestId, RpcFutureResponse futureResponse) {
        futureResponsePool.put(requestId, futureResponse);
    }

    private void removeInvokerFuture(Long requestId) {
        futureResponsePool.remove(requestId);
    }

    public void notifyInvokerFuture(Long requestId, final RpcResponse rpcResponse) {
        // get
        final RpcFutureResponse futureResponse = futureResponsePool.get(requestId);
        if (futureResponse == null) {
            return;
        }
        futureResponse.setRpcResponse(rpcResponse);
        //remove
        removeInvokerFuture(requestId);

    }

    private void init() {
        this.futureResponsePool = new ConcurrentHashMap<>(32);
        this.callbackExecutor = ThreadPoolUtil.newThreadPool(CommonConstants.INVOKER_EXECUTOR_CALLBACK_PREFIX,new ArrayBlockingQueue<>(64));
    }

    public void destroy(){
        if(Objects.nonNull(callbackExecutor)) {
            callbackExecutor.shutdown();
        }
        rpcConfig.getNetType().getClientImpl().close();
        log.info(">>>>>>RpcInvokerFactory destroy");
    }

    public RpcConfig getRpcConfig() {
        return rpcConfig;
    }

    public RpcInvokerFactory(RpcConfig rpcConfig) {
        this.rpcConfig = rpcConfig;

        init();
    }
}
