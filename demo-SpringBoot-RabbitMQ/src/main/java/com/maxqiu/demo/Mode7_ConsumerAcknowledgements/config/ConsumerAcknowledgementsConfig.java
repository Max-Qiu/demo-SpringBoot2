package com.maxqiu.demo.Mode7_ConsumerAcknowledgements.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 队列
 *
 * @author Max_Qiu
 */
@Component
public class ConsumerAcknowledgementsConfig {
    /**
     * 声明确认队列
     */
    @Bean
    public Queue consumerAcknowledgementsQueue() {
        return new Queue("consumer.acknowledgements");
    }
}
