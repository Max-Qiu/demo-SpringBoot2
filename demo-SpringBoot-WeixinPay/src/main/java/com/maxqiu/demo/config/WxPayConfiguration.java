package com.maxqiu.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.maxqiu.demo.properties.WxPayProperties;

/**
 * 微信支付自动配置
 *
 * @author Max_Qiu
 */
@Configuration
@ConditionalOnClass(WxPayService.class)
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayConfiguration {
    @Autowired
    private WxPayProperties properties;

    /**
     * 构造微信支付服务对象
     */
    @Bean
    @ConditionalOnMissingBean(WxPayService.class)
    public WxPayService wxPayService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setMchId(properties.getMchId());
        payConfig.setAppId(properties.getAppId());
        payConfig.setCertSerialNo(properties.getCertSerialNo());
        payConfig.setPrivateKeyPath(properties.getPrivateKeyPath());
        payConfig.setPrivateCertPath(properties.getPrivateCertPath());
        payConfig.setApiV3Key(properties.getApiV3Key());
        payConfig.setNotifyUrl(properties.getNotifyUrl());
        WxPayServiceImpl wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}
