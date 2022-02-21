package com.maxqiu.demo.service;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.maxqiu.demo.request.UserRequest;

/**
 * @author Max_Qiu
 */
@SpringBootTest
class CacheCustomKeyGeneratorServiceTest {
    @Autowired
    private CacheCustomKeyGeneratorService service;

    @Test
    void test() {
        System.out.println(service.noParameter());

        System.out.println(service.singleParameter(1));

        System.out.println(service.requestParameter(new UserRequest(1, "张三")));

        System.out.println(service.multiParameter(1, 2));

        System.out.println(service.requestParameter2(new UserRequest(2, "李四"), "李"));

        System.out.println(service.listParameter(Arrays.asList(1, 2, 3)));
    }
}
