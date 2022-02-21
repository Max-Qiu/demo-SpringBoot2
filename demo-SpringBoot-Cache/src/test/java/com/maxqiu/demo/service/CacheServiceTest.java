package com.maxqiu.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Max_Qiu
 */
@SpringBootTest
class CacheServiceTest {
    @Autowired
    private CacheService service;

    @Test
    void test() {
        System.out.println(service.cache1());
        System.out.println(service.cache2());
        System.out.println(service.cache3());
        System.out.println(service.key("1"));
        System.out.println(service.sync());
        System.out.println(service.condition(1));
        System.out.println(service.condition(11));
        System.out.println(service.unless());
        System.out.println(service.cachePut());
        service.cacheEvict1();
        service.cacheEvict2();
        try {
            service.cacheEvict3(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(service.cacheManager());
    }
}
