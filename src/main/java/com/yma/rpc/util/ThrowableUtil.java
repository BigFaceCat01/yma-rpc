package com.yma.rpc.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-13 18:38:44
 */
public class ThrowableUtil {

    /**
     * 将异常转换为字符串
     *
     * @param e 异常
     * @return 异常字符串
     */
    public static String toString(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

}
