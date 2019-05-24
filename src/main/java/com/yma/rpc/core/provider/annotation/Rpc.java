package com.yma.rpc.core.provider.annotation;

import java.lang.annotation.*;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 16:17:35
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Rpc {
    /**
     * 指定实现的接口类名
     * @return 接口类
     */
    Class<?> iface();
}
