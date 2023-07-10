package com.maxqiu.demo;

import javax.annotation.Resource;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.maxqiu.demo.service.CallService;
import com.maxqiu.demo.service.RetryService;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试
 *
 * @author Max_Qiu
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RetryServiceTest {
    @Resource
    private RetryService retryService;

    /**
     * 调用
     */
    @Test
    @Order(1)
    void retryTest() {
        try {
            log.info("调用方法，参数：{}", 0);
            log.info("返回结果：{}\n", retryService.retry(0));
            log.info("调用方法，参数：{}", 1);
            log.info("返回结果：{}\n", retryService.retry(1));
            log.info("调用方法，参数：{}", 2);
            log.info("返回结果：{}\n", retryService.retry(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 相同类中调用
     */
    @Test
    @Order(2)
    void callInSameClassTest() {
        try {
            log.info("调用方法，参数：{}", 0);
            log.info("返回结果：{}\n", retryService.callInSameClass(0));
            log.info("调用方法，参数：{}", 1);
            log.info("返回结果：{}\n", retryService.callInSameClass(1));
            log.info("调用方法，参数：{}", 2);
            log.info("返回结果：{}\n", retryService.callInSameClass(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Resource
    private CallService callService;

    /**
     * 不同类中调用
     */
    @Test
    @Order(3)
    void callInOtherClassTest() {
        try {
            log.info("调用方法，参数：{}", 0);
            log.info("返回结果：{}\n", callService.callInOtherClass(0));
            log.info("调用方法，参数：{}", 1);
            log.info("返回结果：{}\n", callService.callInOtherClass(1));
            log.info("调用方法，参数：{}", 2);
            log.info("返回结果：{}\n", callService.callInOtherClass(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
