package com.maxqiu.demo;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
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
class RetryServiceTest {
    @Resource
    private RetryService retryService;

    /**
     * 调用
     */
    @Test
    void retryTest() {
        try {
            log.info("调用方法，参数：{}", 1);
            log.info("返回结果：{}\n", retryService.retry(1, "张三"));
            log.info("返回结果：{}\n", retryService.retry(0, "张三"));
            log.info("返回结果：{}\n", retryService.retry(2, "张三"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 相同类中调用
     */
    @Test
    void callInSameClassTest() {
        try {
            log.info("调用方法，参数：{}", 1);
            log.info("返回结果：{}\n", retryService.callInSameClass(1, "张三"));
            log.info("返回结果：{}\n", retryService.callInSameClass(0, "张三"));
            log.info("返回结果：{}\n", retryService.callInSameClass(2, "张三"));
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
    void callInOtherClassTest() {
        try {
            log.info("调用方法，参数：{}", 1);
            log.info("返回结果：{}\n", callService.callInOtherClass(1, "张三"));
            log.info("返回结果：{}\n", callService.callInOtherClass(0, "张三"));
            log.info("返回结果：{}\n", callService.callInOtherClass(2, "张三"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
