package com.github.myproject.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {

    private static final Logger logger = LoggerFactory.getLogger(ThreadUtils.class);

    public static int cores = Runtime.getRuntime().availableProcessors();

    public static ExecutorService transThreadPool;

    static {
        //0.8为阻塞系数
        transThreadPool = new ThreadPoolExecutor(
                cores, (int) (cores / (1 - 0.8)), 0L,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(2000),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            transThreadPool.shutdown();
            try {
                if (!transThreadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                    transThreadPool.shutdownNow();
                    // 等待任务取消的响应
                    if (!transThreadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                        logger.error("Thread pool can't be shutdown even with interrupting worker threads, which may cause some task inconsistent. Please check the biz logs.");
                    }
                }
            } catch (Exception e) {
                logger.error("shutdown thread pool fail ....", e);
            }
        }));
    }

}
