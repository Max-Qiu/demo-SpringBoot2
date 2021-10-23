package com.maxqiu.demo.Mode6_PublisherConfirms.config;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生产者配置
 *
 * @author Max_Qiu
 */
@Component
public class RabbitTemplateCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    /**
     * 交换机是否收到消息的一个回调方法
     *
     * @param correlationData
     *            消息相关数据
     * @param ack
     *            交换机是否收到消息
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            System.out.println("交换机已经收到消息 id 为：" + id);
        } else {
            System.out.println("交换机还未收到消息 id 为：" + id + "，由于原因：" + cause);
        }
    }

    /**
     * 队列未接收到消息的时候的回调方法
     *
     * @param message
     */
    @Override
    public void returnedMessage(ReturnedMessage message) {
        System.out.println("消息：" + new String(message.getMessage().getBody()) + "\t被交换机：" + message.getExchange()
            + "退回\t退回原因：" + message.getReplyText() + "\t路由key：" + message.getRoutingKey());
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 在依赖注入 rabbitTemplate 之后再设置它的回调对象
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
        // 同配置文件中的 spring.rabbitmq.publisher-returns=true
        // rabbitTemplate.setMandatory(true);
    }
}
