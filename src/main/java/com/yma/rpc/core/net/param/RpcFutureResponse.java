package com.yma.rpc.core.net.param;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-07 15:53:27
 */
@Slf4j
public class RpcFutureResponse implements Future<RpcResponse> {

    private Object lock;
    private RpcResponse rpcResponse;
    private boolean done = false;

    public RpcFutureResponse() {
        this.lock = new Object();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public RpcResponse get() throws InterruptedException, ExecutionException {
        try {
            return get(-1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("request timeout for requestId = {}", rpcResponse.getRequestId());
            return rpcResponse;
        }
    }

    public void setRpcResponse(RpcResponse rpcResponse) {
        this.rpcResponse = rpcResponse;
        synchronized (lock) {
            this.done = true;
            lock.notifyAll();
        }
    }

    @Override
    public RpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (isDone()) {
            return rpcResponse;
        }
        synchronized (lock) {
            if (timeout < 0) {
                lock.wait();
            } else {
                long t = TimeUnit.MILLISECONDS == unit ? timeout : TimeUnit.MILLISECONDS.convert(timeout, unit);
                lock.wait(t);
            }
        }
        if (!isDone()) {
            throw new RuntimeException("xxl-rpc, request timeout at:" + System.currentTimeMillis() + ", request:" + rpcResponse.toString());
        }
        return rpcResponse;
    }
}
