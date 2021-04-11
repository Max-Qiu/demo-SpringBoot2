package com.maxqiu.demo.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Max_Qiu
 */
@Service
public class CacheService {
    /**
     * 简写 缓存名称
     */
    // @Cacheable(value = "cache1")
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
    @Cacheable(value = "key", key = "#key")
    public String key1(String key) {
        System.out.println("执行了 key1 方法，key 的值为：" + key);
        return key;
    }

    /**
     * 只有一个变量时，若使用当前变量作为键，可以省略不写
     */
    @Cacheable(value = "key")
    public String key2(String key) {
        System.out.println("执行了 key2 方法，key 的值为：" + key);
        return key;
    }

    /**
     * 如果给出多个参数，则返回SimpleKey包含所有参数的
     */
    @Cacheable(value = "key")
    public String key3(String username, String password) {
        System.out.println("执行了 key3 方法，username 的值为：" + username + " password 的值为：" + password);
        return username + " " + password;
    }

    @Cacheable(cacheNames = "sync", sync = true)
    public String sync() {
        System.out.println("进入了 sync 方法");
        try {
            // 模拟方法内的值计算
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "sync";
    }

    @Cacheable(cacheNames = "condition", condition = "#id > 10")
    public String condition(Integer id) {
        System.out.println("进入了 condition 方法");
        return "success";
    }

    @Cacheable(cacheNames = "unless", unless = "#result > 3")
    public Integer unless() {
        System.out.println("进入了 unless 方法");
        return (int)(Math.random() * 10);
    }

    @CachePut("cache1")
    public String cachePut() {
        System.out.println("执行了 cachePut 方法");
        return "success";
    }

    @CacheEvict("cache1")
    public void cacheEvict1() {
        System.out.println("执行了 cacheEvict1 方法");
    }

    @CacheEvict(value = "key", allEntries = true)
    public void cacheEvict2() {
        System.out.println("执行了 cacheEvict2 方法");
    }

    @CacheEvict(value = "key", beforeInvocation = true)
    public void cacheEvict3(Integer key) {
        System.out.println("执行了 cacheEvict3 方法");
        int i = key / 0;
        System.out.println("cacheEvict3 执行完毕");
    }

    @Cacheable(value = "cacheManager", cacheManager = "expire1min")
    public String cacheManager() {
        System.out.println("执行了 cache1 方法");
        return "1";
    }
}
