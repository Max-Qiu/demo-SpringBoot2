package com.maxqiu.demo.Mode4_Routing.config;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 队列 与 交换机
 *
 * @author Max_Qiu
 */
@Component
public class RoutingConfig {
    /**
     * 声明一个直连类型的交换机
     */
    @Bean
    public DirectExchange direct() {
        return new DirectExchange("direct");
    }

    /**
     * 声明队列
     *
     * @return
     */
    @Bean
    public Queue autoDeleteQueue3() {
        return new AnonymousQueue();
    }

    /**
     * 声明一个绑定关系，将队列绑定到交换机，并指定要监听的 routingKey
     */
    @Bean
    public Binding binding3a(DirectExchange direct, Queue autoDeleteQueue3) {
        return BindingBuilder.bind(autoDeleteQueue3).to(direct).with("error");
    }

    // 下同

    @Bean
    public Queue autoDeleteQueue4() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding binding4a(DirectExchange direct, Queue autoDeleteQueue4) {
        return BindingBuilder.bind(autoDeleteQueue4).to(direct).with("info");
    }

    @Bean
    public Binding binding4b(DirectExchange direct, Queue autoDeleteQueue4) {
        return BindingBuilder.bind(autoDeleteQueue4).to(direct).with("warning");
    }

    @Bean
    public Binding binding4c(DirectExchange direct, Queue autoDeleteQueue4) {
        return BindingBuilder.bind(autoDeleteQueue4).to(direct).with("error");
    }
}
