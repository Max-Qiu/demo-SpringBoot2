官方文档：[RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)

> 本文环境

- SpringBoot 2.4.x
- RabbitMQ 3.9.x

# 依赖

`SpringBoot`已经整合了`RabbitMQ`，使用时只需要如下依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.amqp</groupId>
    <artifactId>spring-rabbit-test</artifactId>
    <scope>test</scope>
</dependency>
```

# 配置

配置连接也很简单，只要在`application.yml`添加如下配置即可

```yml
spring:
  rabbitmq:
    host: 127.0.0.1 # 地址
    port: 5672 # 端口
    username: guest # 用户名
    password: guest # 密码
```

# 使用

`RabbitMQ`有七种核心使用方式：

1. `"Hello World!"`入门：最简单的消息发送与接收
2. `Work queues`工作队列：分配任务（竞争消费者模式，即轮询）
3. `Publish/Subscribe`发布/订阅：同时向许多消费者发送信息
4. `Routing`路由：有选择地接收消息
5. `Topics`主题：基于模式(主题)接收消息
6. `RPC`远程调用：请求/应答模式的例子
7. `Publisher Confirms`发布确认：与发布者确认可靠的推送

## Hello World 入门

在下图中：

- `P`是生产者，用于发送消息
- `C`是消费者，用于接收消息
- `红色的框`是队列，是消息缓冲区

![](https://cdn.maxqiu.com/upload/aba0ea218e3a4be3809dcf1c7d88e933.png)

### 声明队列

在发送和接收消息之前，需要先声明队列。消费者只能监听已存在的队列，所以声明队列配置放在消费端

```java
/**
 * 队列配置
 */
@Configuration
public class QueueConfig {
    /**
     * 入门队列名称
     */
    public static final String HELLO_WORLD_QUEUE_NAME = "hello.queue";

    /**
     * 入门队列配置
     */
    @Bean
    public Queue helloWorldQueue() {
        // 创建一个队列，并指定队列名称
        return new Queue(HELLO_WORLD_QUEUE_NAME);
    }
}
```

### 消费者

使用`@RabbitListener`注解创建一个监听器，用于接收消息。

```java
/**
 * 入门消费者
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
```

### 生产者

生产者使用`RabbitTemplate`发送消息

```java
/**
 * 生产者
 */
@RestController
public class IndexController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("hello-world")
    public String helloWorld(String message) {
        rabbitTemplate.convertAndSend(QueueConfig.HELLO_WORLD_QUEUE_NAME, message);
        System.out.println("Sent:" + message);
        return message;
    }
}
```

## Work Queues 工作队列

![](https://cdn.maxqiu.com/upload/6ad6f480729741ea853299098716c034.png)

