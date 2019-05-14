package com.yma.rpc.constant;

import java.util.concurrent.TimeUnit;

/**
 * @author create by huang xiao bao
 * @date 2019-04-20 17:07:39
 */
public final class CommonConstants {
    /**
     * 30分钟对应的毫秒数
     */
    public static final String DOT = ".";
    public static final String SEPARATOR = "/";
    public static final String PROTOCOL_FILE = "file";
    public static final String PROTOCOL_JAR = "jar";
    public static final String DOT_CLASS = ".class";
    public static final String EMPTY_STRING = "";
    public static final String THREAD_FACTORY_PREFIX = "Thread pool-thread";
    public static final String INVOKER_EXECUTOR_CALLBACK_PREFIX = "Callback execute pool-thread";
    public static final String SERVER_EXECUTOR_PREFIX = "Server executor pool-thread";
    public static final int INT_BYTE = 4;
    public static final int TIMEOUT_MILLISECONDS = 3000;
    public static final int THREAD_KEEP_ALIVE = 3;
    public static final TimeUnit THREAD_KEEP_ALIVE_TIMEUNIT = TimeUnit.MINUTES;

}
