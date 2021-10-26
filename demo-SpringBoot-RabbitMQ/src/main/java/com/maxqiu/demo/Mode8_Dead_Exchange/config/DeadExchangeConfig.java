package com.maxqiu.demo.Mode8_Dead_Exchange.config;

import java.util.HashMap;
import java.util.Map;

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
public class DeadExchangeConfig {
    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange("dead.exchange");
    }

    @Bean
    public DirectExchange normalExchange() {
        return new DirectExchange("normal.exchange");
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue deadQueue() {
        return new Queue("dead.queue");
    }

    @Bean
    public Queue normalQueue() {
        // 绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        // 设置死信 exchange 参数 key 是固定值
        params.put("x-dead-letter-exchange", "dead.exchange");
        // 设置死信 routing-key 参数 key 是固定值
        params.put("x-dead-letter-routing-key", "key2");
        // 设置队列长度限制
        params.put("x-max-length", 5);
        return new Queue("normal.queue", true, true, false, params);
    }

    /**
     * 声明队列和交换机绑定关系
     */
    @Bean
    public Binding normalQueueBinding(DirectExchange normalExchange, Queue normalQueue) {
        return BindingBuilder.bind(normalQueue).to(normalExchange).with("key1");
    }

    @Bean
    public Binding deadQueueBinding(DirectExchange deadExchange, Queue deadQueue) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with("key2");
    }
}
