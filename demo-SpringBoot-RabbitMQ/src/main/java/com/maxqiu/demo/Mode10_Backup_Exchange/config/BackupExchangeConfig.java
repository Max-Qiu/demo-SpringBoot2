package com.maxqiu.demo.Mode10_Backup_Exchange.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 交换机和队列
 *
 * @author Max_Qiu
 */
@Component
public class BackupExchangeConfig {
    /**
     * 声明确认交换机
     */
    @Bean
    public DirectExchange confirmExchange() {
        ExchangeBuilder exchangeBuilder = ExchangeBuilder.directExchange("confirm.exchange").durable(true)
            // 设置该交换机的备份交换机
            .withArgument("alternate-exchange", "backup.exchange");
        return exchangeBuilder.build();
    }

    /**
     * 声明备份交换机
     */
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange("backup.exchange");
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable("confirm.queue").build();
    }

    @Bean
    public Queue backQueue() {
        return QueueBuilder.durable("backup.queue").build();
    }

    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable("warning.queue").build();
    }

    /**
     * 声明绑定关系
     */
    @Bean
    public Binding confirmBinding(Queue confirmQueue, DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with("key1");
    }

    @Bean
    public Binding warningBinding(Queue warningQueue, FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }

    @Bean
    public Binding backupBinding(Queue backQueue, FanoutExchange backupExchange) {
        return BindingBuilder.bind(backQueue).to(backupExchange);
    }
}
