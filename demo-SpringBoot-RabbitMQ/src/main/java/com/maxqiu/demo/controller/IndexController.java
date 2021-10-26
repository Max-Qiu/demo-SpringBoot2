package com.maxqiu.demo.controller;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.entity.User;

/**
 * 生产者
 *
 * @author Max_Qiu
 */
@RestController
public class IndexController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试用的标记序号
     */
    private static int i = 1;

    /**
     * 入门 生产者
     */
    @GetMapping("hello-world")
    public Integer helloWorld() {
        System.out.println("~~~~Sent:" + i);
        // 发送字符串
        rabbitTemplate.convertAndSend(
            // 发送到哪个队列，不填写该参数时默认为空队列
            "hello-world",
            // 具体消息内容
            i + "");
        // 发送数字
        rabbitTemplate.convertAndSend("hello-world", i);
        // 发送对象
        rabbitTemplate.convertAndSend("hello-world", new User(i, "TOM"));
        return i++;
    }

    /**
     * 工作队列 生产者
     */
    @GetMapping("work-queues")
    public Integer workQueues() {
        System.out.println("~~~~Sent:" + i);
        rabbitTemplate.convertAndSend("work-queues", i);
        return i++;
    }

    /**
     * 发布订阅 生产者
     */
    @GetMapping("publish-subscribe")
    public Integer publishSubscribe() {
        System.out.println("~~~~Sent:" + i);
        rabbitTemplate.convertAndSend(
            // 指定交换机名称
            "fanout", "", i);
        return i++;
    }

    /**
     * 路由 生产者
     */
    @GetMapping("routing")
    public void routing() {
        String[] keys = {"debug", "info", "warning", "error"};
        for (String key : keys) {
            // 发送四种类型的消息日志
            rabbitTemplate.convertAndSend("direct", key, key);
            System.out.println("~~~~Sent:" + key);
        }
    }

    /**
     * 主题 生产者
     */
    @GetMapping("topic")
    public void topic() {
        String[] keys = {"quick.orange.rabbit", "lazy.orange.elephant", "quick.orange.fox", "lazy.brown.fox",
            "lazy.pink.rabbit", "quick.brown.fox", "quick.orange.male.rabbit", "lazy.orange.male.rabbit"};
        for (String key : keys) {
            rabbitTemplate.convertAndSend("topic", key, key);
            System.out.println("~~~~Sent:" + key);
        }
    }

    /**
     * 发布确认生产者
     */
    @GetMapping("publisherConfirms")
    public void publisherConfirms() {
        rabbitTemplate.convertAndSend("publisher.confirms.exchange", "key1", "message", new CorrelationData("1"));
        rabbitTemplate.convertAndSend("publisher.confirms.exchange1", "key1", "message", new CorrelationData("2"));
        rabbitTemplate.convertAndSend("publisher.confirms.exchange", "key2", "message", new CorrelationData("3"));
    }

    /**
     * 消费确认生产者
     */
    @GetMapping("consumerAcknowledgements")
    public Integer consumerAcknowledgements() {
        rabbitTemplate.convertAndSend("consumer.acknowledgements", i);
        return i++;
    }

    /**
     * 死信队列生产者
     */
    @GetMapping("deadQueue")
    public void deadQueue() {
        for (int j = 0; j < 10; j++) {
            rabbitTemplate.convertAndSend("normal.exchange", "key1", j,
                // 设置消息过期时间（单位：毫秒）
                m -> {
                    m.getMessageProperties().setExpiration("10000");
                    return m;
                });
        }
    }
}
