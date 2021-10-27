package com.maxqiu.demo.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            "fanout",
            // 不指定队列名称
            "", i);
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
                correlationData -> {
                    correlationData.getMessageProperties().setExpiration("10000");
                    return correlationData;
                });
        }
    }

    /**
     * 延时队列生产者
     */
    @GetMapping("delayedQueue/{delayTime}")
    public Integer delayedQueue(@PathVariable Integer delayTime) {
        rabbitTemplate.convertAndSend("delayed.exchange", "delayed.routingKey", i,
            // 设置消息延时时间
            correlationData -> {
                correlationData.getMessageProperties().setDelay(delayTime);
                return correlationData;
            });
        System.out.println("当前时间：" + LocalDateTime.now() + "\t发送延时队列的消息：" + i + "\t延时" + delayTime + "毫秒");
        return i++;
    }

    /**
     * 备份交换机生产者
     */
    @GetMapping("backupExchange")
    public Integer backupExchange() {
        // 让消息绑定一个 id 值
        CorrelationData correlationData1 = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("confirm.exchange", "key1", i, correlationData1);
        System.out.println("发送消息 id 为：" + correlationData1.getId() + "\t内容为：" + i);

        CorrelationData correlationData2 = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("confirm.exchange", "key2", i, correlationData2);
        System.out.println("发送消息 id 为：" + correlationData2.getId() + "\t内容为：" + i);

        return i++;
    }
}
