package com.maxqiu.demo.Mode10_Backup_Exchange.handler;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 消费者
 *
 * @author Max_Qiu
 */
@Component
@Slf4j
public class BackupExchangeReceiver {
    @RabbitListener(queues = "confirm.queue")
    public void receiveConfirmMsg(Integer message) {
        System.out.println("收到一般消息" + message);
    }

    @RabbitListener(queues = "warning.queue")
    public void receiveWarningMsg(Integer message) {
        System.out.println("报警发现不可路由消息：" + message);
    }
}
