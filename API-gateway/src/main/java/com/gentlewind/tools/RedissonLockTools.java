package com.gentlewind.tools;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class RedissonLockTools {
    private RedissonClient redissonClient;

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }


    public void lock(String lockKey, int timeout, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, timeUnit);
    }


    public void unlock(String lockKey){
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();

    }
}
