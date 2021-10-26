package com.maxqiu.demo.Mode9_Delayed_Exchange.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 交换机和队列
 *
 * @author Max_Qiu
 */
@Configuration
public class DelayedExchangeConfig {
    /**
     * 延迟交换机
     *
     * @return
     */
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        // 自定义交换机的类型
        args.put("x-delayed-type", "direct");
        return new CustomExchange("delayed.exchange", "x-delayed-message", true, false, args);
    }

    /**
     * 队列
     */
    @Bean
    public Queue delayedQueue() {
        return new Queue("delayed.queue");
    }

    /**
     * 绑定关系
     */
    @Bean
    public Binding bindingDelayedQueue(Queue delayedQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with("delayed.routingKey").noargs();
    }
}
