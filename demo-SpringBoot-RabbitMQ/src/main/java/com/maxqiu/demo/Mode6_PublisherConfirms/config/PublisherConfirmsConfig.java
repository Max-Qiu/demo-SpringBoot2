package com.maxqiu.demo.Mode6_PublisherConfirms.config;

import org.springframework.amqp.core.Binding;
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
        // return new DirectExchange("direct");
        // 也可以使用Builder模式创建
        return ExchangeBuilder
            // 使用直连交换机
            .directExchange("publisher.confirms.exchange").build();
    }

    /**
     * 声明确认队列
     */
    @Bean
    public Queue publisherConfirmsQueue() {
        // return new AnonymousQueue();
        // 也可以使用Builder模式创建
        return QueueBuilder
            // 使用消息持久化，不使用nonDurable(final String name)，使用随机队列名称
            .nonDurable()
            // 独占的
            .exclusive()
            // 队列自动删除
            .autoDelete().build();
    }

    /**
     * 声明确认队列绑定关系
     */
    @Bean
    public Binding queueBinding(DirectExchange publisherConfirmsExchange, Queue publisherConfirmsQueue) {
        // return BindingBuilder.bind(publisherConfirmsQueue).to(publisherConfirmsExchange).with("key1");
        // 也可以使用 new 方法创建绑定关系
        return new Binding(publisherConfirmsQueue.getName(), Binding.DestinationType.QUEUE,
            publisherConfirmsExchange.getName(), "key1", null);
    }
}
