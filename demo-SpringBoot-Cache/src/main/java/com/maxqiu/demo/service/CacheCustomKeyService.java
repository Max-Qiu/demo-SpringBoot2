package com.maxqiu.demo.service;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maxqiu.demo.request.UserRequest;

/**
 * 自定义键
 *
 * 一般使用缓存时，希望缓存的名称为 [serviceName]::[methodName]::[parameterValues]的三段式<br>
 * 此时 @CacheConfig 的 cacheNames 配置当前类的名称，完成第一段<br>
 * 此时 @Cacheable 的 key 配置当前方法的名和参数名（SpEL表达式），完成第二段
 *
 * @author Max_Qiu
 */
@Service
@CacheConfig(cacheNames = "CacheCustomKey")
public class CacheCustomKeyService {
    /**
     * 无参数方法
     *
     * key: "CacheCustomKey::noParameter"
     */
    @Cacheable(key = "#root.methodName")
    public String noParameter() {
        System.out.println("执行了normal方法");
        return "noParameter";
    }

    /**
     * 普通参数方法，参数可以 #p0 #p1 ... 也可以写具体的变量名
     *
     * key: "CacheCustomKey::singleParameter::1"
     *
     * "1"是parameter的值
     */
    // @Cacheable(key = "#root.methodName+'::'+#p0")
    @Cacheable(key = "#root.methodName+'::'+#parameter")
    public Integer singleParameter(Integer parameter) {
        System.out.println("执行了singleParameter方法：" + parameter);
        return parameter;
    }

    /**
     * 实体参数方法
     *
     * key: "CacheCustomKey::requestParameter::UserRequest(id=1, name=张三)"
     *
     * 调用实体的`toString()`方法作为key（实体必须重写toString()方法，否则为对象内存地址，内存地址每次不一样，无法起到缓存作用）
     */
    @Cacheable(key = "#root.methodName+'::'+#request")
    public UserRequest requestParameter(UserRequest request) {
        System.out.println("执行了multiParameter方法：" + request);
        return request;
    }

    /**
     * 多普通参数方法
     *
     * key: "CacheCustomKey::multiParameter::1,2"
     *
     * 多个参数组装成数组
     */
    @Cacheable(key = "#root.methodName+'::'+#p0+','+#p1")
    public Integer multiParameter(Integer parameter1, Integer parameter2) {
        System.out.println("执行了multiParameter方法：" + parameter1 + parameter2);
        return parameter1 + parameter2;
    }

    /**
     * 混合参数方法
     *
     * key: "CacheCustomKey::requestParameter2::UserRequest(id=2, name=李四),李"
     *
     * 多个参数组装成数组
     */
    @Cacheable(key = "#root.methodName+'::'+#p0+','+#p1")
    public UserRequest requestParameter2(UserRequest request, String search) {
        System.out.println("执行了multiParameter2方法：" + request + search);
        return request;
    }

    /**
     * 集合参数方法
     *
     * key: "CacheCustomKey::listParameter::1,2,3"
     *
     * 参数为集合的值
     */
    @Cacheable(key = "#root.methodName+'::'+#p0")
    public List<Integer> listParameter(List<Integer> ids) {
        System.out.println("执行了listParameter方法：" + ids);
        return ids;
    }
}
