package com.maxqiu.demo.Mode6_PublisherConfirms.handler;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者
 *
 * @author Max_Qiu
 */
@Component
public class PublisherConfirmsReceiver {
    @RabbitListener(queues = "#{publisherConfirmsQueue.name}")
    public void receiveMsg(String msg) {
        System.out.println("===Received:" + msg);
    }
}
