package com.maxqiu.demo.Mode1_HelloWorld.handler;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.maxqiu.demo.entity.User;

/**
 * 消费者
 *
 * @author Max_Qiu
 */
@Component
@RabbitListener(
    // 指定要监听哪些队列（可指定多个）
    queues = "hello-world")
public class HelloWorldReceiver {
    /**
     * 接收字符串
     *
     * @param msg
     */
    @RabbitHandler
    public void receive(String msg) {
        System.out.println("----Received String:" + msg);
    }

    /**
     * 接收数字
     *
     * @param msg
     */
    @RabbitHandler
    public void receive(Integer msg) {
        System.out.println("====Received Integer:" + msg);
    }

    /**
     * 接收实体
     *
     * @param msg
     */
    @RabbitHandler
    public void receive(User msg) {
        System.out.println("||||Received Entity:" + msg);
    }
}
