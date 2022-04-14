package com.maxqiu.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 微信支付属性配置类
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {
    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * 设置微信公众号或者小程序等的appid
     */
    private String appId;

    /**
     * 证书序列号
     */
    private String certSerialNo;

    /**
     * apiclient_key.pem 证书文件的绝对路径或者以 classpath: 开头的类路径
     */
    private String privateKeyPath;

    /**
     * apiclient_cert.pem 证书文件的绝对路径或者以 classpath: 开头的类路径
     */
    private String privateCertPath;

    /**
     * apiV3 秘钥值
     */
    private String apiV3Key;

    /**
     * 微信支付回调地址，必须为直接可访问的url，不能携带参数
     */
    private String notifyUrl;
}
