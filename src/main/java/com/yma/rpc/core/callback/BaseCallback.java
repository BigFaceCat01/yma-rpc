package com.yma.rpc.core.callback;

import com.yma.rpc.core.net.param.RpcResponse;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 11:46:10
 */
public interface BaseCallback {
    /**
     * 异步回调函数
     * @param response 响应结果
     */
    void run(RpcResponse response);
}
