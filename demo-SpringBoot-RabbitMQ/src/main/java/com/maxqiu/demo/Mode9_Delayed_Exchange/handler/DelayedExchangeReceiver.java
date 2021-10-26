package com.maxqiu.demo.Mode9_Delayed_Exchange.handler;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者
 *
 * @author Max_Qiu
 */
@Component
public class DelayedExchangeReceiver {
    @RabbitListener(queues = "delayed.queue")
    public void receiveDelayedQueue(Integer message) {
        System.out.println("当前时间：" + LocalDateTime.now() + "\t收到延时队列的消息：" + message);
    }
}
