package com.yma.rpc.exception;

/**
 * @author Created by huang xiao bao
 * @date 2019-04-19 16:58:42
 */
public class RpcException extends RuntimeException {
    public RpcException() {
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }
}
