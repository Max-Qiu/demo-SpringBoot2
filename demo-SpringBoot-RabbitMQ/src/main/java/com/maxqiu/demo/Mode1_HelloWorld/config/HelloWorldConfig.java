package com.maxqiu.demo.Mode1_HelloWorld.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 队列
 *
 * @author Max_Qiu
 */
@Component
public class HelloWorldConfig {
    /**
     * 入门队列配置
     */
    @Bean
    public Queue helloWorldQueue() {
        // 创建一个队列，并指定队列名称
        return new Queue("hello-world");
    }
}
