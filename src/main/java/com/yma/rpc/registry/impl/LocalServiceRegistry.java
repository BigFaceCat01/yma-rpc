package com.yma.rpc.registry.impl;


import com.yma.rpc.registry.ServiceRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-13 10:36:21
 */
public class LocalServiceRegistry implements ServiceRegistry {

    private ConcurrentHashMap<String,List<String>> serviceInstances;

    @Override
    public List<String> get(String serviceName) {
        return serviceInstances.get(serviceName);
    }

    @Override
    public void refreshData(String serviceName, String address) {
        List<String> addresses = new ArrayList<>();
        addresses.add(address);
        serviceInstances.put(serviceName,addresses);
    }

    @Override
    public void refreshData(String serviceName, List<String> address) {
        serviceInstances.put(serviceName,address);
    }

    @Override
    public void start() {
        synchronized (LocalServiceRegistry.class){
            serviceInstances = new ConcurrentHashMap<>(32);
        }
    }

    @Override
    public void destroy() {
        synchronized (LocalServiceRegistry.class){
            serviceInstances.clear();
        }
    }
}
