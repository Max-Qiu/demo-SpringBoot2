package com.maxqiu.demo.Mode5_Topics.config;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 队列 与 交换机
 *
 * @author Max_Qiu
 */
@Component
public class TopicConfig {
    /**
     * 声明一个主题类型的交换机
     */
    @Bean
    public TopicExchange topic() {
        return new TopicExchange("topic");
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue autoDeleteQueue5() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue autoDeleteQueue6() {
        return new AnonymousQueue();
    }

    /**
     * 声明绑定关系，将队列绑定到交换机，并指定要监听的 routingKey
     */
    @Bean
    public Binding binding5a(TopicExchange topic, Queue autoDeleteQueue5) {
        return BindingBuilder.bind(autoDeleteQueue5).to(topic).with("*.orange.*");
    }

    @Bean
    public Binding binding6a(TopicExchange topic, Queue autoDeleteQueue6) {
        return BindingBuilder.bind(autoDeleteQueue6).to(topic).with("*.*.rabbit");
    }

    @Bean
    public Binding binding6b(TopicExchange topic, Queue autoDeleteQueue6) {
        return BindingBuilder.bind(autoDeleteQueue6).to(topic).with("lazy.#");
    }
}
