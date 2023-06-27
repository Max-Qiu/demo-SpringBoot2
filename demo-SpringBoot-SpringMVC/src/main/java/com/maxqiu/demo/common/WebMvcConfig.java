package com.maxqiu.demo.common;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC配置类
 *
 * @author Max_Qiu
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 将自定义解析器注入bean
     */
    @Bean
    public CurrUserArgumentResolver currUserArgumentResolver() {
        return new CurrUserArgumentResolver();
    }

    /**
     * 添加自定义解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currUserArgumentResolver());
    }
}
