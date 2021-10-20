package com.maxqiu.demo.Mode5_Topics.handler;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者
 *
 * @author Max_Qiu
 */
@Component
public class TopicReceiver {
    @RabbitListener(queues = "#{autoDeleteQueue5.name}")
    public void receive1(String msg) {
        System.out.println("===Received1:" + msg);
    }

    @RabbitListener(queues = "#{autoDeleteQueue6.name}")
    public void receive2(String msg) {
        System.out.println("===Received2:" + msg);
    }
}
