package com.maxqiu.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Cookie配置
 *
 * @author Max_Qiu
 */
@Configuration
public class CookieConfig {
    /**
     * 设置Cookie序列化的配置
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        // Session 的 key ，默认：SESSION
        serializer.setCookieName("SESSION");
        // Session 的 value 是否进行 Base64 编码，推荐关闭，方便浏览器内查看到 value 值
        serializer.setUseBase64Encoding(false);
        // Session 的路径
        serializer.setCookiePath("/");
        // Session 的可读域名（默认当前域名）若需要多域名共享 Cookie ，则需要设置为主域名
        // serializer.setDomainName("demo.com");
        return serializer;
    }
}
