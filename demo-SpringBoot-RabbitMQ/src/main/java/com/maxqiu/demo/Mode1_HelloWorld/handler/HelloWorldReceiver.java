package com.maxqiu.demo.Mode1_HelloWorld.handler;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.maxqiu.demo.Mode1_HelloWorld.config.QueueConfig;

/**
 * 入门消费者
 *
 * @author Max_Qiu
 */
@Component
@RabbitListener(
    // 指定要监听哪些队列（可指定多个）
    queues = QueueConfig.HELLO_WORLD_QUEUE_NAME)
public class HelloWorldReceiver {
    @RabbitHandler
    public void receive(String msg) {
        System.out.println("Received:" + msg);
    }
}
