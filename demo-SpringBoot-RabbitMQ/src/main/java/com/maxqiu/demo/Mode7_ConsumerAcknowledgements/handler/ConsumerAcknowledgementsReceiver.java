package com.maxqiu.demo.Mode7_ConsumerAcknowledgements.handler;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

/**
 * 消费者
 *
 * @author Max_Qiu
 */
@Component
public class ConsumerAcknowledgementsReceiver {
    @RabbitListener(queues = "consumer.acknowledgements")
    public void receiveMsg(Channel channel, Message message, Integer msg) {
        System.out.println("===Received:start:" + msg);
        try {
            System.out.println("sleeping");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("===Received:end:" + msg);

        // 手动ACK
        // 默认情况下如果一个消息被消费者正确接收则会被从队列中移除
        // 如果一个队列没被任何消费者订阅，那么这个队列中的消息会被缓存
        // 当有消费者订阅时则会立即发送，当消息被消费者正确接收时，就会被从队列中移除
        try {
            // 手动ack应答
            // 告诉服务器收到这条消息已经被消费了，可以在队列中删掉
            // 否则消息服务器以为这条消息没处理掉，后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
            // 丢弃这条消息
            try {
                // 消息重新入队
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
                // 消息丢弃
                // channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                // 多一个批量参数
                // channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
