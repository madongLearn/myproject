package com.github.myproject.util;

import com.github.myproject.config.RedLockHelper;
import org.redisson.Redisson;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;


/**
 * Created by IntelliJ IDEA.
 *
 * @author : dongma
 * Date: 2020-01-20
 * Time: 13:14
 * @version : 1.0
 * @since : JDK 1.8
 * Description: redisson 分布式锁管理工具类
 */
public class DistributedLockUtil {
    private static Redisson redisson = RedLockHelper.getRedisson();
    private static final String LOCK_FLAG = "redissonLock:";

    /**
     * 根据name进行上锁操作，redissonLock 阻塞事的，采用的机制发布/订阅
     *
     * @param lockName
     */
    public static boolean lock(String lockName, long waitTime, long leaseTime) {
        String key = LOCK_FLAG + lockName;
        RLock lock = redisson.getLock(key);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 根据name进行解锁操作
     *
     * @param lockName 锁名
     */
    public static void unlock(String lockName) {
        redisson.getLock(LOCK_FLAG + lockName).unlock();
    }
}