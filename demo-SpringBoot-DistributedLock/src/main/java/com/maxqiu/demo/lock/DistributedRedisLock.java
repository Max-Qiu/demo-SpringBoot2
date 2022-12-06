package com.maxqiu.demo.lock;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * Redis 分布式锁
 *
 * @author Max_Qiu
 */
public class DistributedRedisLock implements Lock {
    private StringRedisTemplate redisTemplate;

    private String lockName;

    private String id;

    private long expire = 30;

    public DistributedRedisLock(StringRedisTemplate redisTemplate, String lockName, String uuid) {
        this.redisTemplate = redisTemplate;
        this.lockName = lockName;
        this.id = uuid + ":" + Thread.currentThread().getId();
        System.out.println(id);
    }

    @Override
    public void lock() {
        this.tryLock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        try {
            this.tryLock(-1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private static final String LOCK = "if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) ==1 "
        + "then redis.call('hincrby', KEYS[1], ARGV[1], 1) redis.call('expire', KEYS[1], ARGV[2]) return 1 else return 0 end";

    /**
     * 加锁方法
     */
    @Override
    public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
        if (time != -1) {
            this.expire = unit.toSeconds(time);
        }
        while (!Boolean.TRUE
            .equals(redisTemplate.execute(new DefaultRedisScript<>(LOCK, Boolean.class), List.of(lockName), id, String.valueOf(expire)))) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        renewExpire();
        return false;
    }

    private static final String UNLOCK = "if redis.call('hexists', KEYS[1], ARGV[1]) == 0 then return nil "
        + "elseif redis.call('hincrby' , KEYS[1], ARGV[1], -1) == 0 then return redis.call('del' , KEYS[1]) else return 0 end";

    /**
     * 解锁方法
     */
    @Override
    public void unlock() {
        Long execute = redisTemplate.execute(new DefaultRedisScript<>(UNLOCK, Long.class), Collections.singletonList(lockName), id);
        if (execute == null) {
            throw new IllegalMonitorStateException("this lock dosn't belong to you");
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    private static final String RENEW =
        "if redis.call('hexists', KEYS[1], ARGV[1]) == 1 then return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end";

    private void renewExpire() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (Boolean.TRUE.equals(redisTemplate.execute(new DefaultRedisScript<>(RENEW, Boolean.class), List.of(lockName), id))) {
                    renewExpire();
                }
            }
        }, this.expire * 1000 / 3);
    }
}
