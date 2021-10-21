package com.maxqiu.demo.Mode4_Routing.handler;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者
 *
 * @author Max_Qiu
 */
@Component
public class RoutingReceiver {
    @RabbitListener(queues = "#{autoDeleteQueue3.name}")
    public void receive1(String msg) {
        System.out.println("===Received1:" + msg);
    }

    @RabbitListener(queues = "#{autoDeleteQueue4.name}")
    public void receive2(String msg) {
        System.out.println("===Received2:" + msg);
    }
}
