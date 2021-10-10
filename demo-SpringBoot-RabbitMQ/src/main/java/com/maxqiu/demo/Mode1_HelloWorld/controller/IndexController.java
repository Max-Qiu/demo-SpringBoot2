package com.maxqiu.demo.Mode1_HelloWorld.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.Mode1_HelloWorld.config.QueueConfig;

/**
 * 生产者
 *
 * @author Max_Qiu
 */
@RestController
public class IndexController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("hello-world")
    public String helloWorld(String message) {
        rabbitTemplate.convertAndSend(
            // 发送到哪个队列，不填写该参数时默认为空队列
            QueueConfig.HELLO_WORLD_QUEUE_NAME,
            // 具体消息内容
            message);
        System.out.println("Sent:" + message);
        return message;
    }
}
