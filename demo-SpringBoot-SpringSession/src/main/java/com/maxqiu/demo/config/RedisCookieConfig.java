package com.maxqiu.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * SpringSession的Cookie配置
 *
 * @author Max_Qiu
 */
@Configuration
public class RedisCookieConfig {
    /**
     * 设置Cookie序列化的配置
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        // Session的key，默认：SESSION
        serializer.setCookieName("SESSION");
        // Session的value是否进行Base64编码，推荐关闭，方便浏览器内查看到value值
        serializer.setUseBase64Encoding(false);
        // Session的路径
        serializer.setCookiePath("/");
        // Session的可读域名（默认当前域名）若需要多域名共享Cookie，则需要设置为主域名
        // serializer.setDomainName("demo.com");
        return serializer;
    }
}
