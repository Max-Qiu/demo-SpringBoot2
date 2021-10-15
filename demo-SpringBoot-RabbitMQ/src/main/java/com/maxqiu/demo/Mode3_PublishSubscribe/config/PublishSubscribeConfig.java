package com.maxqiu.demo.Mode3_PublishSubscribe.config;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 队列 与 交换机
 *
 * @author Max_Qiu
 */
@Component
public class PublishSubscribeConfig {
    /**
     * 声明一个扇出类型的交换机
     */
    @Bean
    public FanoutExchange fanout() {
        // 指定交换机名称为：fanout，可自定义
        return new FanoutExchange("fanout");
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue autoDeleteQueue1() {
        // 此处使用匿名、非持久、独占、自动删除的队列
        return new AnonymousQueue();
    }

    /**
     * 声明一个绑定关系，将队列绑定到交换机
     */
    @Bean
    public Binding binding1(FanoutExchange fanout, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1).to(fanout);
    }

    // 下同

    @Bean
    public Queue autoDeleteQueue2() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding binding2(FanoutExchange fanout, Queue autoDeleteQueue2) {
        return BindingBuilder.bind(autoDeleteQueue2).to(fanout);
    }
}
