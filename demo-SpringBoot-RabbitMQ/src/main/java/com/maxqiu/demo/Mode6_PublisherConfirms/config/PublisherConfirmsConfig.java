package com.maxqiu.demo.Mode6_PublisherConfirms.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 队列 与 交换机
 *
 * @author Max_Qiu
 */
@Component
public class PublisherConfirmsConfig {
    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange publisherConfirmsExchange() {
        return ExchangeBuilder.directExchange("publisher.confirms.exchange").build();
    }

    /**
     * 声明确认队列
     */
    @Bean
    public Queue publisherConfirmsQueue() {
        return QueueBuilder.durable("publisher.confirms.queue").build();
    }

    /**
     * 声明确认队列绑定关系
     */
    @Bean
    public Binding queueBinding(DirectExchange publisherConfirmsExchange, Queue publisherConfirmsQueue) {
        return BindingBuilder.bind(publisherConfirmsQueue).to(publisherConfirmsExchange).with("key1");
    }
}
