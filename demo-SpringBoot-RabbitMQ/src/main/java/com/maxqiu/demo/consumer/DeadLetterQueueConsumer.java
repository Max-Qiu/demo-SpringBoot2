package com.maxqiu.demo.consumer;

import java.time.LocalDateTime;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Max_Qiu
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {
    @RabbitListener(queues = "QD")
    public void receiveD(Message message) {
        log.info("当前时间：{},收到死信队列信息{}", LocalDateTime.now(), new String(message.getBody()));
    }

    public static final String DELAYED_QUEUE_NAME = "delayed.queue";

    @RabbitListener(queues = DELAYED_QUEUE_NAME)
    public void receiveDelayedQueue(Message message) {
        log.info("当前时间：{},收到延时队列的消息：{}", LocalDateTime.now(), new String(message.getBody()));
    }
}
