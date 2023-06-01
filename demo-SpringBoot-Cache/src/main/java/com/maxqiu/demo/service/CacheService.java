package com.maxqiu.demo.service;

import java.util.Random;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Max_Qiu
 */
@Service
// 在类的级别配置缓存通用配置
// @CacheConfig(cacheNames = "cache")
public class CacheService {
    /**
     * 简写 缓存名称
     */
    // @Cacheable(value = "cache1")
    // @Cacheable(cacheNames = "cache1")
    @Cacheable("cache1")
    public String cache1() {
        System.out.println("执行了 cache1 方法");
        return "1";
    }

    @Cacheable("cache2")
    public String cache2() {
        System.out.println("执行了 cache2 方法");
        return "2";
    }

    /**
     * 使用多个缓存名称
     */
    @Cacheable({"cache1", "cache2"})
    public String cache3() {
        System.out.println("执行了 cache3 方法");
        return "3";
    }

    /**
     * 使用参数做缓存键
     */
    @Cacheable(cacheNames = "key", key = "#key")
    public String key(String key) {
        System.out.println("执行了 key 方法，key 的值为：" + key);
        return key;
    }

    @Cacheable(cacheNames = "sync", sync = true)
    public String sync() {
        System.out.println("进入了 sync 方法");
        try {
            // 模拟方法内的值计算
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "sync";
    }

    @Cacheable(cacheNames = "condition", condition = "#id > 10")
    public Integer condition(Integer id) {
        System.out.println("进入了 condition 方法");
        return id;
    }

    @Cacheable(cacheNames = "unless", unless = "#result > 0")
    public Integer unless() {
        System.out.println("进入了 unless 方法");
        return new Random().nextInt(10);
    }

    @Cacheable(cacheNames = "cacheManager", cacheManager = "expire1day")
    public String cacheManager() {
        System.out.println("执行了 cacheManager 方法");
        return "1";
    }

    @CachePut("cache1")
    public String cachePut() {
        System.out.println("执行了 cachePut 方法");
        return "success";
    }

    @CacheEvict("cache1")
    public void cacheEvict() {
        System.out.println("执行了 cacheEvict 方法");
    }

    @CacheEvict(cacheNames = "key", allEntries = true)
    public void cacheEvictAllEntries() {
        System.out.println("执行了 cacheEvictAllEntries 方法");
    }

    @CacheEvict(cacheNames = "key", beforeInvocation = true)
    public void cacheEvictBeforeInvocation(String key) {
        System.out.println("执行了 cacheEvictBeforeInvocation 方法");
        @SuppressWarnings({"divzero", "NumericOverflow"})
        int i = 1 / 0;
        System.out.println(i);
        System.out.println("cacheEvict3 执行完毕");
    }
}
