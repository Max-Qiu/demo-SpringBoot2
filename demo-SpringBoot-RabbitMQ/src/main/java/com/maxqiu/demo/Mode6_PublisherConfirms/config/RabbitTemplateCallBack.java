package com.maxqiu.demo.Mode6_PublisherConfirms.config;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 生产者配置
 *
 * @author Max_Qiu
 */
@Component
@Slf4j
public class RabbitTemplateCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    /**
     * 交换机不管是否收到消息的一个回调方法 CorrelationData 消息相关数据 ack 交换机是否收到消息
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到 id 为:{}的消息", id);
        } else {
            log.info("交换机还未收到 id 为:{}消息,由于原因:{}", id, cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("消息 {}\t被交换机 {} 退回\t退回原因：{}, 路由key：{}", new String(returned.getMessage().getBody()),
            returned.getExchange(), returned.getReplyText(), returned.getRoutingKey());
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
    }
}
