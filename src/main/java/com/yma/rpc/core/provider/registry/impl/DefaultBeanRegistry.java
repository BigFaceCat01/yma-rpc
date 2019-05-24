package com.yma.rpc.core.provider.registry.impl;

import com.yma.rpc.core.provider.annotation.Rpc;
import com.yma.rpc.core.provider.registry.BeanRegistry;
import com.yma.rpc.util.ClassScanUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-14 16:01:10
 */
@Slf4j
public class DefaultBeanRegistry implements BeanRegistry {
    @Override
    public Map<String, Object> scan(String basePackage) {
        Map<String, Object> res = new HashMap<>(32);
        ClassScanUtil.scan(basePackage, (source) -> {
            Rpc rpc = source.getDeclaredAnnotation(Rpc.class);
            if (Objects.isNull(rpc)) {
                return;
            }
            Class iface = rpc.iface();
            String interfaceName;
            interfaceName = iface.getName();
            Object o = null;
            try {
                o = source.newInstance();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            res.put(interfaceName, o);
        });
        return res;
    }
}
