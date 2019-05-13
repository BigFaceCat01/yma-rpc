package com.yma.rpc.core.invoker.router;

import com.yma.rpc.registry.ServiceRegistry;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 17:43:29
 */
public interface Router {
    String router(String serviceName, ServiceRegistry serviceRegistry);
}
