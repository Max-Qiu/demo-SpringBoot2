package com.maxqiu.demo.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Max_Qiu
 */
@Service
public class IndexService {
    @Cacheable("test")
    public String index() {
        System.out.println("执行了 index 方法");
        return "test";
    }
}
