package com.yma.rpc.registry;

import java.util.List;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 16:18:48
 */
public interface ServiceRegistry {
    List<String> get(String serviceName);

    void refreshData(String serviceName, String address);

    void refreshData(String serviceName, List<String> address);

    void start();

    void destroy();
}
