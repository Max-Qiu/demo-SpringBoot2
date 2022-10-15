package com.maxqiu.demo.service;

import javax.annotation.Resource;

import com.maxqiu.demo.properties.TestProperties;

/**
 * 服务类
 *
 * @author Max_Qiu
 */
public class TestService {
    @Resource
    private TestProperties testProperties;

    public void test() {
        System.out.println("地址：" + testProperties.getAddress() + "\t秘钥：" + testProperties.getKey());
    }
}
