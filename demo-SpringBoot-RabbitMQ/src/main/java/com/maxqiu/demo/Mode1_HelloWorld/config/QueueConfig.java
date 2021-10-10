package com.maxqiu.demo.Mode1_HelloWorld.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 队列配置
 *
 * @author Max_Qiu
 */
@Configuration
public class QueueConfig {
    /**
     * 入门队列名称
     */
    public static final String HELLO_WORLD_QUEUE_NAME = "hello.queue";

    /**
     * 入门队列配置
     */
    @Bean
    public Queue helloWorldQueue() {
        // 创建一个队列，并指定队列名称
        return new Queue(HELLO_WORLD_QUEUE_NAME);
    }
}
