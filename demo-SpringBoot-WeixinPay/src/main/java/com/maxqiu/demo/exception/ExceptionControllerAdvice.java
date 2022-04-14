package com.maxqiu.demo.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.maxqiu.demo.vo.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 集中处理所有异常
 *
 * @author Max_Qiu
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.maxqiu.demo.controller")
public class ExceptionControllerAdvice {
    /**
     * 处理微信支付异常
     */
    @ExceptionHandler(value = WxPayException.class)
    public Result<String> handleException(WxPayException e) {
        log.error("微信支付异常", e);
        return Result.fail(e.getMessage());
    }
}
