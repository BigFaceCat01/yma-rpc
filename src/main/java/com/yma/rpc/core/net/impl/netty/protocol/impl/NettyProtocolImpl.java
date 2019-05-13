package com.yma.rpc.core.net.impl.netty.protocol.impl;

import com.yma.rpc.core.net.protocol.Protocol;

import java.util.List;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 11:26:09
 */
public class NettyProtocolImpl extends Protocol {
    @Override
    public byte[] encode(Object obj) {
        return new byte[0];
    }

    @Override
    public List<Object> decode(byte[] bytes) {
        return null;
    }
}
