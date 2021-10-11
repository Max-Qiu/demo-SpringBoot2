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

> 下文示例代码中仅展示与`RabbitMQ`相关的`import`导包代码

## Hello World 入门

入门示例中，展示了一个生产者和一个消费者的情况。在下图中：

- `P`是生产者，用于发送消息
- `C`是消费者，用于接收消息
- `红色的框`是队列，是消息缓冲区

![](https://cdn.maxqiu.com/upload/aba0ea218e3a4be3809dcf1c7d88e933.png)

### 声明队列

在发送和接收消息之前，需要先声明队列。消费者只能监听已存在的队列，所以声明队列配置放在消费端。

此处声明了一个队列，名称为`hello-world`

```java
import org.springframework.amqp.core.Queue;

/**
 * 队列配置
 */
@Configuration
public class HelloWorldConfig {
    /**
     * 入门队列配置
     */
    @Bean
    public Queue helloWorldQueue() {
        // 创建一个队列，并指定队列名称
        return new Queue("hello-world");
    }
}
```

### 消费者

使用`@RabbitListener`注解创建一个监听器，用于指定监听哪个队列。使用`@RabbitHandler`指定方法接收数据，根据入参类型处理不同类型的数据。下文展示了处理不同类型的消息

```java
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * 入门消费者
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
```

### 生产者

生产者使用`RabbitTemplate`发送消息，在`Controller`、`Service`、或者其他类中使用`@Autowired`注解引入`RabbitTemplate`即可使用。使用`convertAndSend`方法自动将对象转换为消息并发送。下文中展示了发送不同类型的消息

```java
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 生产者
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
}
```

### 结果

访问`/hello-world`调用生产者发送消息，看到控制台打印如下消息

```
~~~~Sent:1
----Received String:1
====Received Integer:1
||||Received Entity:User(id=1, name=TOM)
```


## Work Queues 工作队列

当消费者需要很长时间才能处理一条消息时，可以建立多个消费者同时处理任务。队列会将消息以**轮询**模式分配给消费者

![](https://cdn.maxqiu.com/upload/6ad6f480729741ea853299098716c034.png)

### 配置队列

同入门示例，再建立一个队列

```java
/**
 * 队列配置
 */
@Configuration
public class WorkQueuesConfig {
    /**
     * 工作队列配置
     */
    @Bean
    public Queue workQueue() {
        return new Queue("work-queues");
    }
}
```

### 消费者

创建两个消费者

```java
/**
 * 工作队列消费者
 */
@Component
@RabbitListener(queues = "work-queues")
public class WorkQueuesReceiver1 {
    @RabbitHandler
    public void receive(Integer msg) {
        System.out.println("---Received1:" + msg);
    }
}
```

```java
/**
 * 工作队列消费者
 */
@Component
@RabbitListener(queues = "work-queues")
public class WorkQueuesReceiver2 {
    @RabbitHandler
    public void receive(Integer msg) {
        System.out.println("===Received2:" + msg);
    }
}
```

### 生产者


```
/**
 * 生产者
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
     * 工作队列 生产者
     */
    @GetMapping("work-queues")
    public Integer workQueues() {
        System.out.println("~~~~Sent:" + i);
        rabbitTemplate.convertAndSend("work-queues", i);
        return i++;
    }
}
```

### 结果

多次访问`work-queues`，看到如下结果

```
~~~~Sent:1
---Received1:1
~~~~Sent:2
===Received2:2
~~~~Sent:3
---Received1:3
~~~~Sent:4
===Received2:4
```
