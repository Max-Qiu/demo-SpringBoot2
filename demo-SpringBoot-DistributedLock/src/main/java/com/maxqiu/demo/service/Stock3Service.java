package com.maxqiu.demo.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import com.maxqiu.demo.lock.DistributedLockFactory;
import com.maxqiu.demo.lock.DistributedRedisLock;

import cn.hutool.core.lang.UUID;

/**
 * 库存（Redis） 服务类
 *
 * @author Max_Qiu
 */
@Service
public class Stock3Service {
    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 无锁模式，数据库中的库存最终不为0
     */
    public void normal() {
        Integer stock = redisTemplate.opsForValue().get("stock");
        if (stock != null && stock > 0) {
            redisTemplate.opsForValue().set("stock", stock - 1);
        }
    }

    /**
     * Redis 乐观锁，可以解决问题但不推荐使用
     */
    public void watch() {
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(@NotNull RedisOperations operations) throws DataAccessException {
                operations.watch("stock");
                Integer stock = Integer.parseInt(operations.opsForValue().get("stock").toString());
                if (stock != null && stock > 0) {
                    operations.multi();
                    operations.opsForValue().set("stock", stock - 1);
                    List exec = operations.exec();
                    if (exec == null || exec.size() == 0) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        watch();
                    }
                    return exec;
                }
                return null;
            }
        });
    }

    /**
     * 使用 set nx 添加锁
     */
    public void lock() {
        String uuid = UUID.fastUUID().toString();
        // 添加锁时同时设置超时时间，防止死锁
        while (!Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS))) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Integer stock = redisTemplate.opsForValue().get("stock");
            if (stock != null && stock > 0) {
                redisTemplate.opsForValue().set("stock", stock - 1);
            }
        } finally {
            // 1. 删除锁时先判断，防止误删
            // 2. 判断锁和释放锁使用 lua 脚本，保证原子性
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            stringRedisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), List.of("lock"), uuid);
        }
    }

    @Resource
    private DistributedLockFactory distributedLockFactory;

    /**
     * 可重入锁
     */
    public void reentrantLock() {
        DistributedRedisLock redisLock = distributedLockFactory.getRedisLock("lock");
        redisLock.lock();;
        try {
            Integer stock = redisTemplate.opsForValue().get("stock");
            if (stock != null && stock > 0) {
                redisTemplate.opsForValue().set("stock", stock - 1);
            }
            // test();
        } finally {
            redisLock.unlock();
        }
    }

    private void test() {
        DistributedRedisLock lock = this.distributedLockFactory.getRedisLock("lock");
        lock.lock();
        try {
            System.out.println("测试可重入锁");
        } finally {
            lock.unlock();
        }
    }
}
