package com.maxqiu.demo.service;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maxqiu.demo.request.UserRequest;

/**
 * 自定义全局键生成器
 *
 * 使用 @Cacheable 的 key 每次都要写SpEL表达式，很是麻烦
 *
 * 可以使用自定义 KeyGenerator 实现 {@link org.springframework.cache.interceptor.KeyGenerator} 接口<br>
 * 然后在 @CacheConfig 和 @Cacheable 中配置，@Cacheable 可以覆盖 @CacheConfig
 *
 * @author Max_Qiu
 */
@Service
@CacheConfig(cacheNames = "CacheCustomKeyGenerator", keyGenerator = "cacheKeyGenerator")
public class CacheCustomKeyGeneratorService {
    /**
     * 无参数方法
     *
     * key: "CacheCustomKeyGenerator::noParameter"
     */
    @Cacheable
    public String noParameter() {
        System.out.println("执行了normal方法");
        return "noParameter";
    }

    /**
     * 普通参数方法
     *
     * key: "CacheCustomKeyGenerator::singleParameter::1"
     *
     * "1"是parameter的值
     */
    @Cacheable
    public Integer singleParameter(Integer parameter) {
        System.out.println("执行了singleParameter方法：" + parameter);
        return parameter;
    }

    /**
     * 实体参数方法
     *
     * key: "CacheCustomKeyGenerator::requestParameter::UserRequest(id=1, name=张三)"
     *
     * 调用实体的`toString()`方法作为key（实体必须重写toString()方法，否则为对象内存地址，内存地址每次不一样，无法起到缓存作用）
     */
    @Cacheable
    public UserRequest requestParameter(UserRequest request) {
        System.out.println("执行了multiParameter方法：" + request);
        return request;
    }

    /**
     * 多普通参数方法
     *
     * key: "CacheCustomKeyGenerator::multiParameter::1,2"
     *
     * 多个参数组装成数组
     */
    @Cacheable
    public Integer multiParameter(Integer parameter1, Integer parameter2) {
        System.out.println("执行了multiParameter方法：" + parameter1 + parameter2);
        return parameter1 + parameter2;
    }

    /**
     * 混合参数方法
     *
     * key: "CacheCustomKeyGenerator::requestParameter2::UserRequest(id=2, name=李四),李"
     *
     * 多个参数组装成数组
     */
    @Cacheable
    public UserRequest requestParameter2(UserRequest request, String search) {
        System.out.println("执行了multiParameter2方法：" + request + search);
        return request;
    }

    /**
     * 集合参数方法
     *
     * key: "CacheCustomKeyGenerator::listParameter::[1, 2, 3]"
     *
     * 参数为集合的值
     */
    @Cacheable
    public List<Integer> listParameter(List<Integer> ids) {
        System.out.println("执行了listParameter方法：" + ids);
        return ids;
    }
}
