package com.maxqiu.demo.service;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maxqiu.demo.request.UserRequest;

/**
 * 缓存 key 生成规则，以及默认的 SimpleKeyGenerator 测试
 *
 * 生成规则：<br>
 * 1. 默认情况下是 {@link org.springframework.cache.interceptor.SimpleKeyGenerator}，使用 [cacheNames]::[key] 作为键<br>
 * 2. [cacheNames] 在 @CacheConfig 和 @Cacheable 中必须配置一个，@Cacheable 可以覆盖 @CacheConfig<br>
 * 3. [key] 是根据 @CacheConfig 或 @Cacheable 的 keyGenerator 配置进行生成的，@Cacheable 可以覆盖 @CacheConfig<br>
 *
 * @author Max_Qiu
 */
@Service
@CacheConfig(cacheNames = "cacheName")
public class CacheKeyService {
    /**
     * 普通方法
     *
     * key: "cacheName::SimpleKey []"
     *
     * 未配置cacheNames，使用 @CacheConfig 的配置
     */
    @Cacheable
    public String normal() {
        System.out.println("执行了normal方法");
        return "normal";
    }

    /**
     * 无参数方法
     *
     * key: "myCacheName::SimpleKey []"
     *
     * 配置了cacheNames，覆盖 @CacheConfig 的配置；无参数，生成空的SimpleKey
     */
    @Cacheable(cacheNames = "myCacheName")
    public String noParameter() {
        System.out.println("执行了normal方法");
        return "noParameter";
    }

    /**
     * 普通参数方法
     *
     * key: "myCacheName::1"
     *
     * "1"是parameter的值
     */
    @Cacheable(cacheNames = "myCacheName")
    public Integer singleParameter(Integer parameter) {
        System.out.println("执行了singleParameter方法：" + parameter);
        return parameter;
    }

    /**
     * 实体参数方法
     *
     * key: "myCacheName::UserRequest(id=1, name=张三)"
     *
     * 调用实体的`toString()`方法作为key（实体必须重写toString()方法，否则出异常）
     */
    @Cacheable(cacheNames = "myCacheName")
    public UserRequest requestParameter(UserRequest request) {
        System.out.println("执行了multiParameter方法：" + request);
        return request;
    }

    /**
     * 多普通参数方法
     *
     * key: "myCacheName::SimpleKey [1,2]"
     *
     * 多个参数组装成数组
     */
    @Cacheable(cacheNames = "myCacheName")
    public Integer multiParameter(Integer parameter1, Integer parameter2) {
        System.out.println("执行了multiParameter方法：" + parameter1 + parameter2);
        return parameter1 + parameter2;
    }

    /**
     * 混合参数方法
     *
     * key: "myCacheName::SimpleKey [UserRequest(id=2, name=李四),张]"
     *
     * 多个参数组装成数组
     */
    @Cacheable(cacheNames = "myCacheName")
    public UserRequest requestParameter2(UserRequest request, String search) {
        System.out.println("执行了multiParameter2方法：" + request + search);
        return request;
    }

    /**
     * 集合参数方法
     *
     * key: "myCacheName::1,2,3"
     *
     * 参数为集合的值
     */
    @Cacheable(cacheNames = "myCacheName")
    public List<Integer> listParameter(List<Integer> ids) {
        System.out.println("执行了listParameter方法：" + ids);
        return ids;
    }
}
