package com.zhongan.icare.message.push.util;

import java.util.concurrent.*;

import static java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

/**
 * Created by zhangxiaojun on 2017/1/25.
 */
public class ThreadUtils
{
    /**
     * 默认的核心线程数
     */
    public static int corePoolSizeDefault = 5;
    /**
     * 最大线程数
     */
    public static int maximumPoolSizeDefault = 30;
    /**
     * 线程存活时间
     */
    public static long keepAliveTimeDefault = 60;
    /**
     * 线程存活时间的单位
     */
    public static TimeUnit timeUnitDefault = TimeUnit.SECONDS;
    /**
     * 阻塞队列的个数默认是100
     */
    public static BlockingQueue<Runnable> workQueueDefault = new ArrayBlockingQueue(100);

    /**
     * 使用默认的参数作为线程池参数
     *
     * @return
     */
    public static ThreadPoolExecutor loadThreadPool()
    {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(corePoolSizeDefault, maximumPoolSizeDefault, keepAliveTimeDefault, timeUnitDefault, workQueueDefault, Executors.defaultThreadFactory(), new CallerRunsPolicy());
        return poolExecutor;
    }

    /**
     * 根据传入的核心线程数和最大线程数创建线程
     *
     * @param corePoolSize
     * @param maximumPoolSize
     * @return
     */
    public static ThreadPoolExecutor loadThreadByParam(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue)
    {
        if (corePoolSize == 0) corePoolSize = corePoolSizeDefault;
        if (maximumPoolSize == 0) corePoolSize = corePoolSizeDefault;
        if (workQueue == null) workQueue = workQueueDefault;
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTimeDefault, timeUnitDefault, workQueue, Executors.defaultThreadFactory(), new CallerRunsPolicy());
        return poolExecutor;
    }

}
