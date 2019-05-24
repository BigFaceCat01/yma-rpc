package com.yma.rpc.history.v1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-22 09:45:17
 */
public class NetInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //1、获得接口名称，方法名，方法参数，传入方法参数，服务ip
        //2、发送网络请求
        //3、获得响应
        return null;
    }

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Object o = new Object();
            list.add(o);
            o = null;
        }
        System.out.println();
    }
}
