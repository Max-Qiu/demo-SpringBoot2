package com.maxqiu.demo.Mode3_PublishSubscribe.handler;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 工作队列消费者
 *
 * @author Max_Qiu
 */
@Component
public class PublishSubscribeReceiver {
    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receive1(Integer msg) {
        System.out.println("===Received1:" + msg);
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receive2(Integer msg) {
        System.out.println("===Received2:" + msg);
    }
}
