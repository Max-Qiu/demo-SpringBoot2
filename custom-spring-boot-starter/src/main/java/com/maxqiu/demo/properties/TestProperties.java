package com.maxqiu.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义配置文件
 *
 * @author Max_Qiu
 */
@ConfigurationProperties(prefix = "max.test")
public class TestProperties {
    /**
     * 地址
     */
    private String address;

    /**
     * 秘钥
     */
    private String key;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
