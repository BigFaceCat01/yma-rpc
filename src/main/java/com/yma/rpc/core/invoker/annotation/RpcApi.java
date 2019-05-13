package com.yma.rpc.core.invoker.annotation;

import java.lang.annotation.*;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 16:10:43
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface RpcApi {
    String serviceName();

    /**
     * 服务提供者ip+port  如：127.0.0.1:8080
     * @return
     */
    String address() default "";
}
