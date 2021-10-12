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

## `Hello World`入门

入门示例中，展示了一个生产者和一个消费者的情况。在下图中：

- `P`是生产者，用于发送消息
- `C`是消费者，用于接收消息
- `红色的框`是队列，是消息缓冲区

![](https://cdn.maxqiu.com/upload/aba0ea218e3a4be3809dcf1c7d88e933.png)

### 队列

在发送和接收消息之前，需要先声明队列。消费者只能监听已存在的队列，所以声明队列配置放在消费端。

此处声明了一个队列，名称为`hello-world`

```java
/**
 * 队列配置
 */
@Component
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

访问`http://127.0.0.1:8080/hello-world`调用生产者发送消息，看到控制台打印如下消息

```
~~~~Sent:1
----Received String:1
====Received Integer:1
||||Received Entity:User(id=1, name=TOM)
```


## `Work Queues`工作队列

当消费者需要很长时间才能处理一条消息时，可以建立多个消费者同时处理任务。队列会将消息以**轮询**模式分配给消费者

![](https://cdn.maxqiu.com/upload/6ad6f480729741ea853299098716c034.png)

### 队列

同入门示例，再建立一个队列

```java
/**
 * 队列配置
 */
@Component
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

多次访问`http://127.0.0.1:8080/work-queues`，看到如下结果

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

## `Publish/Subscribe`发布/订阅

`RabbitMQ`消息传递模型的核心思想是**生产者从不直接向队列发送任何消息**。实际上，生产者经常甚至根本不知道消息是否会被传送到任何队列。

相反，生产者只能将消息发送到交换机`exchange`，交换机工作的内容非常简单，一方面它接收来自生产者的消息，另一方面将它们推入队列。交换机必须确切知道如何处理收到的消息。是应该把这些消息放到特定队列还是说把他们到许多队列中还是说应该丢弃它们。这就的由交换机的类型来决定。

交换机总共有以下类型：

- 直接`direct`
- 主题`topic`
- 标题`headers`
- 扇出`fanout`

本教程的前面部分生产者并未设置交换机，但仍然能够将消息发送到队列。之前能实现的原因是因为使用的是默认交换，即空字符串`""`，详见`convertAndSend`源码

**发布订阅**图示如下：

![](https://cdn.maxqiu.com/upload/5106c9615e7a477b876c6fdaac73fc1d.png)

1. 扇出`fanout`：发布订阅模式需要使用扇出交换机，扇出交换机非常简单，它将收到的所有消息广播到它绑定的所有队列。
2. 临时队列`AnonymousQueue`：每当连接到`RabbitMQ`时，我们都需要一个全新的空队列，为此可以创建一个具有随机名称的队列，其次一旦断开了消费者的连接，队列将被自动删除。
3. 绑定`Binding`：其实是交换机和队列之间的桥梁，它告诉交换机和哪个队列进行了绑定。

### 交换机和队列

```java
/**
 * 队列配置
 */
@Component
public class PublishSubscribeConfig {
    /**
     * 声明一个扇出类型的交换机
     */
    @Bean
    public FanoutExchange fanout() {
        // 指定交换机名称为：fanout，可自定义
        return new FanoutExchange("fanout");
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue();
    }

    /**
     * 声明一个绑定关系，将队列绑定到交换机
     */
    @Bean
    public Binding binding1(FanoutExchange fanout, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1).to(fanout);
    }

    // 下同

    @Bean
    public Queue autoDeleteQueue2() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding binding2(FanoutExchange fanout, Queue autoDeleteQueue2) {
        return BindingBuilder.bind(autoDeleteQueue2).to(fanout);
    }
}
```

### 消费者

1. `@RabbitListener`注解可以直接作用在方法上，并处理消息，不需要`@RabbitHandler`注解。适用于队列内消息对象类型只有一种时使用
2. 上文中使用`AnonymousQueue`声明随机名称队列，所以注解内使用表达式获取队列名称

```java
/**
 * 工作队列消费者
 */
@Component
public class PublishSubscribeReceiver {
    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receive1(Integer msg) {
        System.out.println("===Received1:" + msg);
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receive2(Integer msg) {
        System.out.println("===Received2:" + msg);
    }
}
```

### 生产者

生产者发送消息时需要指定交换机，但是不能指定队列，所以使用`""`

```java
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
}
```

### 结果

多次访问`http://127.0.0.1:8080/publish-subscribe`，看到如下结果

```
~~~~Sent:1
===Received2:1
===Received1:1
~~~~Sent:2
===Received2:2
===Received1:2
~~~~Sent:3
===Received1:3
===Received2:3
```
