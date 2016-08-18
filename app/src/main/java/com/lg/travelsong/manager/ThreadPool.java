package com.lg.travelsong.manager;

import com.lg.travelsong.utils.MyLogUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 * @author LuoYi on 2016/8/3
 */
public class ThreadPool {
    private static ThreadPool sThreadPool;
    private ThreadPoolExecutor executor;
    public int corePoolSize;//核心线程数
    public int maximumPoolSize;//最大线程数
    public long keepAliveTime;//单位为executor里设置的时间粒度 秒

    private ThreadPool(){
        int cpuCount = Runtime.getRuntime().availableProcessors();//cpu数量
        MyLogUtils.logCatch("ThreadPool-->cpuCount",cpuCount+"");
        corePoolSize = cpuCount * 2 + 1;
        maximumPoolSize = cpuCount * 2 + 1;
        keepAliveTime = 1L;
    }

    /**
     * 单例模式，双重校验锁
     * @return
     */
    public static ThreadPool getInstance(){
        if (sThreadPool == null){
            synchronized (ThreadPool.class){
                if (sThreadPool == null){
                    sThreadPool = new ThreadPool();
                }
            }
        }
        return sThreadPool;
    }

    /**
     * 执行线程
     * @param r Runnable对象
     */
    public void execute(Runnable r){
        //int corePoolSize//核心线程数, int maximumPoolSize//最大线程数, long keepAliveTime//线程休眠时间,
        // TimeUnit unit//时间粒度, BlockingQueue<Runnable> workQueue//线程队列,
        // ThreadFactory threadFactory//线程工程, RejectedExecutionHandler handler//线程异常处理策略
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        executor.execute(r);
    }

    /**
     * 取消执行
     * @param r Runnable对象
     */
    public void cancel(Runnable r){
        if (executor != null) {
            executor.getQueue().remove(r);
        }
    }
}
