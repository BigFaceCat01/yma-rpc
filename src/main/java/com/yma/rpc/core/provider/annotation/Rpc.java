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
     * 若有多个接口，需指定接口名称，默认取第一个接口
     * @return
     */
    String iface() default "";
}
