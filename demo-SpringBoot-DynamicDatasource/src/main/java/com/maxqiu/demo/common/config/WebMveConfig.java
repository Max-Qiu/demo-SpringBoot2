package com.maxqiu.demo.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.maxqiu.demo.common.interceptor.DynamicDatasourceInterceptor;

/**
 * Web配置
 *
 * @author Max_Qiu
 */
@Configuration
public class WebMveConfig implements WebMvcConfigurer {
    @Autowired
    private DynamicDatasourceInterceptor dynamicDatasourceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 除 system 下的接口，其他接口都配置动态数据源
        registry.addInterceptor(dynamicDatasourceInterceptor).addPathPatterns("/**").excludePathPatterns("/system/**");
    }
}
