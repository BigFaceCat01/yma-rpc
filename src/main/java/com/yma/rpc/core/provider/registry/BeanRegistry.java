package com.yma.rpc.core.provider.registry;

import java.util.Map;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-14 16:00:31
 */
public interface BeanRegistry {
    /**
     * 扫描类实现
     *
     * @param basePackage 根包
     * @return key->类名 value->实例
     */
    Map<String, Object> scan(String basePackage);
}
