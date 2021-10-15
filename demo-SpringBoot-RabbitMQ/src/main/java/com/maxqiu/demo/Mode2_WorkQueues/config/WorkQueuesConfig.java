package com.maxqiu.demo.Mode2_WorkQueues.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 队列
 *
 * @author Max_Qiu
 */
@Component
public class WorkQueuesConfig {
    /**
     * 工作队列配置
     */
    @Bean
    public Queue workQueue() {
        return new Queue("work-queues");
    }
}
