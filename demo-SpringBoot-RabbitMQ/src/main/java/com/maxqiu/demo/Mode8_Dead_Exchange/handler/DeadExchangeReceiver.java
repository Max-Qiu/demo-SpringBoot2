package com.maxqiu.demo.Mode8_Dead_Exchange.handler;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * 消费者
 *
 * @author Max_Qiu
 */
// @Component
public class DeadExchangeReceiver {
    @RabbitListener(queues = "dead.queue")
    public void receiveMsg(String msg) {
        System.out.println("Dead===Received:" + msg);
    }
}
