package com.yma.rpc.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-13 18:38:44
 */
public final class StringUtil {

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

    /**
     * 判断字符串是否为空
     * @param o 字符串
     * @return 布尔
     */
    public static boolean isEmpty(String o){
        return Objects.isNull(o) || o.trim().length() == 0;
    }

    private StringUtil(){}
}
