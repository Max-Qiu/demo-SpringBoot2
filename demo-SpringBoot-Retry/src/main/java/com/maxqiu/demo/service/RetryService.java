package com.maxqiu.demo.service;

import java.net.http.HttpConnectTimeoutException;
import java.time.LocalTime;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 重试类，编写重试方法
 *
 * @author Max_Qiu
 */
@Service
@Slf4j
public class RetryService {
    /**
     * 如果在同一个类中调用重试方法，重试注解不会生效
     */
    public String callInSameClass(int code) throws Exception {
        return retry(code);
    }

    /**
     * 需要执行重试的方法
     */
    @Retryable(
        // include：同value，当执行重试的异常类型（可以多个）
        // include = Exception.class,
        // exclude：要排除的异常类型（可以多个）
        // exclude = {},
        // 当 include 和 exclude 均为空时，所有异常均重试
        // maxAttempts：最大重试次数（包括第一次失败）
        maxAttempts = 5,
        // 重试配置
        backoff = @Backoff(
            // 延迟时间，单位：毫秒
            delay = 1000,
            // 最大延迟时间（默认值为0即不启用，若小于delay值则为3000），单位：毫秒
            maxDelay = 5000,
            // 相对上一次延迟时间的倍数（比如2：第一次1000毫秒，第二次2000毫秒，第三次4000毫秒...）
            multiplier = 2))
    public String retry(Integer code) throws Exception {
        log.info("方法被调用，时间：{}", LocalTime.now());
        // 这里可能会产生非检查异常
        int i = 2 / code;
        if (code == 1) {
            // 此处随意使用了一个检查异常
            throw new HttpConnectTimeoutException("抛出自定义异常信息！");
        }
        log.info("方法调用成功！，结果：{}", i);
        return "SUCCESS";
    }

    /**
     * 使用 @Recover 注解做最终失败处理（可以针对不同的异常定义多个最终失败处理）<br>
     * 第一个参数：需要处理的异常类型<br>
     * 后面的参数：（可选）与重试方法相同顺序和类型的参数<br>
     * 返回值类型：必须与重试方法的类型相同
     */
    @Recover
    public String recover(HttpConnectTimeoutException e, Integer code) {
        log.error("HttpConnectTimeoutException回调方法执行！");
        log.error("异常信息：{}", e.getMessage());
        log.error("参数code:{}", code);
        return "指定异常处理";
    }

    /**
     * 建议定义一个所有 Exception 异常处理，用于处理非检查异常
     */
    @Recover
    public String recover(Exception e) {
        log.error("Exception回调方法执行！");
        log.error("异常信息：{}", e.getMessage());
        return "所有异常处理";
    }
}
