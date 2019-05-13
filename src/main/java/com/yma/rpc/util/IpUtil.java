package com.yma.rpc.util;

import com.yma.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-11 15:45:44
 */
@Slf4j
public class IpUtil {
    public static Object[] parseIpPort(String address){
        String[] array = address.split(":");

        String host = array[0];
        int port = Integer.parseInt(array[1]);

        return new Object[]{host, port};
    }


    public static String getLocalHost() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress();
            return ip;
        } catch (UnknownHostException e) {
            throw new RpcException(e);
        }
    }

    public static String buildLocalHost(int port){
        return getLocalHost() + ":" + port;
    }

    private IpUtil(){}
}
