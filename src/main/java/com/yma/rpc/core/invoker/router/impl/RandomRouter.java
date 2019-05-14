package com.yma.rpc.core.invoker.router.impl;

import com.yma.rpc.core.invoker.router.Router;
import com.yma.rpc.registry.ServiceRegistry;

import java.util.List;
import java.util.Random;

/**
 * 路由方式为随机
 * @author Created by huang xiao bao
 * @date 2019-05-11 18:10:27
 */
public class RandomRouter implements Router {
    private Random random = new Random();

    @Override
    public String router(String serviceName,ServiceRegistry serviceRegistry) {
        List<String> serviceInstances = serviceRegistry.get(serviceName);
        int size = serviceInstances.size();
        int r = random.nextInt(size);
        return serviceInstances.get(r);
    }
}
