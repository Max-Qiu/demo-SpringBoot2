package com.maxqiu.demo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.maxqiu.demo.properties.TestProperties;
import com.maxqiu.demo.service.TestService;

/**
 * 自动配置
 *
 * @author Max_Qiu
 */
@Configuration
@EnableConfigurationProperties(TestProperties.class)
public class TestAutoConfiguration {

    // 将 service 作为 Bean 进行管理
    @Bean
    // 如果使用者已经自定义了，则无需创建
    @ConditionalOnMissingBean(TestService.class)
    public TestService testService() {
        return new TestService();
    }
}
