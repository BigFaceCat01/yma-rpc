package com.yma.rpc.core.net.param;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-07 10:48:47
 */
@Data
@Builder
public class RpcRequest implements Serializable {
    private Long requestId;
    private String className;
    private String methodName;
    private Long createTime;
    private Class<?>[] methodParam;
    private Object[] params;
}
