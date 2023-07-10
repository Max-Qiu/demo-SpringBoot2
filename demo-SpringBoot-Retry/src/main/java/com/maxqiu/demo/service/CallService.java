package com.maxqiu.demo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * 调用重试方法的类
 *
 * @author Max_Qiu
 */
@Service
public class CallService {
    @Resource
    private RetryService retryService;

    /**
     * 如果在另一个类中调用重试方法，可以生效
     */
    public String callInOtherClass(int code) throws Exception {
        return retryService.retry(code);
    }
}
