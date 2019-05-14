package com.yma.rpc.util;

import com.yma.rpc.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-14 15:05:31
 */
@Slf4j
public final class ThreadPoolUtil {

    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactoryImpl(CommonConstants.THREAD_FACTORY_PREFIX);

    private static final RejectedExecutionHandler DEFAULT_REJECT_HANDLER = (r, executor) -> {
        ThreadFactoryImpl threadFactory = (ThreadFactoryImpl) executor.getThreadFactory();
        log.error("{} task too many",threadFactory.getPrefix());
    };

    public static ThreadPoolExecutor newThreadPool(int corePoolSize,
                                                   int maximumPoolSize,
                                                   long keepAliveTime,
                                                   TimeUnit unit,
                                                   BlockingQueue<Runnable> workQueue,
                                                   RejectedExecutionHandler rejectedExecutionHandler,
                                                   ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                rejectedExecutionHandler);
    }

    public static ThreadPoolExecutor newThreadPool(String prefix,BlockingQueue<Runnable> workQueue) {
        int cpu = Runtime.getRuntime().availableProcessors();
        return newThreadPool(cpu,
                cpu << 1,
                CommonConstants.THREAD_KEEP_ALIVE,
                CommonConstants.THREAD_KEEP_ALIVE_TIMEUNIT,
                workQueue,
                DEFAULT_REJECT_HANDLER,
                new ThreadFactoryImpl(prefix));
    }

    public static ThreadPoolExecutor newThreadPool(BlockingQueue<Runnable> workQueue) {
        int cpu = Runtime.getRuntime().availableProcessors();
        return newThreadPool(cpu,
                cpu << 1,
                CommonConstants.THREAD_KEEP_ALIVE,
                CommonConstants.THREAD_KEEP_ALIVE_TIMEUNIT,
                workQueue,
                DEFAULT_REJECT_HANDLER,
                DEFAULT_THREAD_FACTORY);
    }

    private static class ThreadFactoryImpl implements ThreadFactory{
        private String prefix;
        private AtomicInteger count;

        public ThreadFactoryImpl(String prefix){
            this.prefix = prefix;
            this.count = new AtomicInteger(1);
        }
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, prefix + count.getAndIncrement());
        }

        public String getPrefix() {
            return prefix;
        }
    }

    private ThreadPoolUtil() {
    }
}
