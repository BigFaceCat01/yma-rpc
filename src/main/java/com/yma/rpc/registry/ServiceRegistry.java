package com.yma.rpc.registry;

import java.util.List;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 16:18:48
 */
public interface ServiceRegistry {
    /**
     * 通过服务名称获得服务监听地址
     * @param serviceName 服务名
     * @return
     */
    List<String> get(String serviceName);

    /**
     * 刷新地址信息
     * @param serviceName 服务名称
     * @param address 地址
     */
    void refreshData(String serviceName, String address);

    /**
     * 刷新地址信息
     * @param serviceName 服务名称
     * @param address 地址列表
     */
    void refreshData(String serviceName, List<String> address);

    /**
     * 启动服务注册
     */
    void start();

    /**
     * 销毁服务注册
     */
    void destroy();
}
