- 官方文档：[RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)
- 尚硅谷视频：[尚硅谷_RabbitMQ](http://www.atguigu.com/download_detail.shtml?v=327)

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

# 入门使用

下文中介绍`RabbitMQ`5种入门使用方法

1. 使用默认交换机类型模式：
    1. `"Hello World!"`入门：最简单的消息发送与接收
    2. `Work queues`工作队列：分配任务（单生产多消费）（轮询模式）
2. 使用指定交换机类型模式：根据交换机类型分为如下三种
    1. `Publish/Subscribe`发布/订阅：同时向许多消费者发送信息
    2. `Routing`路由：有选择地接收消息
    3. `Topics`主题：基于模式(主题)接收消息

## `Hello World`入门

入门示例介绍一个生产者和一个消费者的情况。在下图中：

- `P`是生产者，用于发送消息
- `C`是消费者，用于接收消息
- `红色的框`是队列，是消息缓冲区

![](https://cdn.maxqiu.com/upload/aba0ea218e3a4be3809dcf1c7d88e933.png)

### 队列

在发送和接收消息之前，需要先声明队列。消费者只能监听已存在的队列，所以声明队列配置放在消费端。

此处声明了一个队列，名称为`hello-world`

```java
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
@Component
@RabbitListener(
    // 指定要监听哪些队列（可指定多个）
    queues = "hello-world")
public class HelloWorldReceiver {
    /**
     * 接收字符串
     */
    @RabbitHandler
    public void receive(String msg) {
        System.out.println("----Received String:" + msg);
    }

    /**
     * 接收数字
     */
    @RabbitHandler
    public void receive(Integer msg) {
        System.out.println("====Received Integer:" + msg);
    }

    /**
     * 接收实体
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

当消费者需要很长时间才能处理一条消息时，可以建立多个消费者分配处理任务。队列会将消息以**轮询**模式分配给消费者

![](https://cdn.maxqiu.com/upload/6ad6f480729741ea853299098716c034.png)

> 不公平分发

`RabbitMQ`分发消息采用的轮训分发，但是在某种场景下这种策略并不是很好。比方说有两个消费者在处理任务，其中有个消费者`A`处理任务的速度非常快，而另外一个消费者`B`处理速度却很慢，这个时候还是采用轮训分发的话就会导致处理速度快的这个消费者很大一部分时间处于空闲状态，而处理慢的那个消费者一直在干活。但是`RabbitMQ`并不知道这种情况它依然很公平的进行分发。为了避免这种情况，可以设置参数`spring.rabbitmq.listener.simple.refetch`

```yml
spring:
  rabbitmq:
    listener:
      type: simple # 默认
      simple:
        prefetch: 1 # 每个消费者未确认的消息最大数量
```

默认情况下`prefetch`的值为`250`，即消费者最多同时接收250条消息，并在消费一条或多条之后统一给`RabbitMQ`返回`ack`应答消息

### 队列

同入门示例，再建立一个队列

```java
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
@Component
@RabbitListener(queues = "work-queues")
public class WorkQueuesReceiver1 {
    @RabbitHandler
    public void receive(Integer msg) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("---Received1:" + msg);
    }
}
```

```java
@Component
@RabbitListener(queues = "work-queues")
public class WorkQueuesReceiver2 {
    @RabbitHandler
    public void receive(Integer msg) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("===Received2:" + msg);
    }
}
```

### 生产者

```java
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

> `prefetch=250`

消费者平均分配消息

```
~~~~Sent:1
~~~~Sent:2
~~~~Sent:3
~~~~Sent:4
---Received1:1
---Received1:3
===Received2:2
===Received2:4
```

> `prefetch=1`

消费快的消费者消费更多消息

```
~~~~Sent:1
~~~~Sent:2
~~~~Sent:3
~~~~Sent:4
---Received1:1
---Received1:3
---Received1:4
===Received2:2
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
2. 临时队列`AnonymousQueue`：每当连接到`RabbitMQ`时，需要一个全新的空队列，为此可以创建一个具有随机名称的队列，其次一旦断开了消费者的连接，队列将被自动删除。
3. 绑定`Binding`：其实是交换机和队列之间的桥梁，它告诉交换机和哪个队列进行了绑定。

### 交换机和队列

```java
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
            "fanout",
            // 不指定队列名称
            "", i);
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

## `Routing`路由

本节将添加一些特别的功能：比方说只让某个消费者订阅发布的**部分**消息。例如只把严重错误消息定向存储到日志文件，同时仍然能够在控制台上打印所有日志消息。

> 绑定

绑定是交换器和队列之间的关系。这可以简单地理解为：**队列只对它绑定的交换机的消息感兴趣**。绑定可以采用额外的绑定键参数。`Spring AMQP`使用了一个`fluent API`来让这种关系非常清晰。将交换机和队列传入`BindingBuilder`并简单地用绑定键将队列绑定到交换机，如下所示：

```java
@Bean
public Binding binding(DirectExchange direct, Queue autoDeleteQueue3) {
    return BindingBuilder.bind(autoDeleteQueue3).to(direct).with("error");
}
```

绑定键的含义取决于交换机类型。以前使用的扇形交换，完全忽略了它的价值。

> 直接交换

上一节中的日志系统将所有消息广播给所有消费者，对此我们想做一些改变，例如我们希望将日志消息写入磁盘的程序仅接收严重错误(errros)，而不存储哪些警告(warning)或信息(info)日志消息避免浪费磁盘空间。扇出这种交换类型并不能带来很大的灵活性，它只能进行无意识的广播，在这里将使用`direct`（直连）这种类型来进行替换，这种类型的工作方式是，消息只去到它绑定的`routingKey`队列中去。

![](https://cdn.maxqiu.com/upload/d13412c2b0f04d0ea5fbe937c7937a24.png)

在上面这张图中，可以看到`X`绑定了两个队列，绑定类型是`direct`。队列`Q1`绑定键为`orange`，队列`Q2`绑定键有两个：一个绑定键为`black`，另一个绑定键为`green`。在这种绑定情况下，生产者发布消息到`exchange`上，绑定键为`orange`的消息会被发布到队列`Q1`。绑定键为`black`和`green`的消息会被发布到队列`Q2`，其他消息类型的消息将被丢弃。

> 多重绑定

![](https://cdn.maxqiu.com/upload/1f88e22e726a44e6b715fbfc193ba19c.png)

如果`exchange`的绑定类型是`direct`，但是它绑定的多个队列的`key`如果都相同，在这种情况下虽然绑定类型是`direct`但是它表现的就和`fanout`类似了，就跟广播差不多，如上图所示。

> 综合

![](https://cdn.maxqiu.com/upload/a6cdaf6d6c0146fba7acf7f99ce5a14a.png)

将`直接交换`和`多重绑定`放在一起，如上图所示。

### 交换机和队列

```java
@Component
public class RoutingConfig {
    /**
     * 声明一个直连类型的交换机
     */
    @Bean
    public DirectExchange direct() {
        return new DirectExchange("direct");
    }

    /**
     * 声明队列
     *
     * @return
     */
    @Bean
    public Queue autoDeleteQueue3() {
        return new AnonymousQueue();
    }

    /**
     * 声明一个绑定关系，将队列绑定到交换机，并指定要监听的 routingKey
     */
    @Bean
    public Binding binding3a(DirectExchange direct, Queue autoDeleteQueue3) {
        return BindingBuilder.bind(autoDeleteQueue3).to(direct).with("error");
    }

    // 下同

    @Bean
    public Queue autoDeleteQueue4() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding binding4a(DirectExchange direct, Queue autoDeleteQueue4) {
        return BindingBuilder.bind(autoDeleteQueue4).to(direct).with("info");
    }

    @Bean
    public Binding binding4b(DirectExchange direct, Queue autoDeleteQueue4) {
        return BindingBuilder.bind(autoDeleteQueue4).to(direct).with("warning");
    }

    @Bean
    public Binding binding4c(DirectExchange direct, Queue autoDeleteQueue4) {
        return BindingBuilder.bind(autoDeleteQueue4).to(direct).with("error");
    }
}
```

### 消费者

```java
@Component
public class RoutingReceiver {
    @RabbitListener(queues = "#{autoDeleteQueue3.name}")
    public void receive1(String msg) {
        System.out.println("===Received1:" + msg);
    }

    @RabbitListener(queues = "#{autoDeleteQueue4.name}")
    public void receive2(String msg) {
        System.out.println("===Received2:" + msg);
    }
}
```

### 生产者

```java
@RestController
public class IndexController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

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
}
```

### 结果

多次访问`http://127.0.0.1:8080/routing`，看到如下结果

```
~~~~Sent:debug
~~~~Sent:info
~~~~Sent:warning
~~~~Sent:error
===Received1:error
===Received2:info
===Received2:warning
===Received2:error
```

## `Topic`主题

尽管使用直连交换机改进了系统，但是它仍然存在局限性：它不能基于多个标准进行路由。比如：接收的日志类型有`info.base`和`info.advantage`，某个队列只想接收`info.base`的消息，那这个时候直连就办不到了。这个时候就只能使用`Topic`（主题）类型

> `topic`交换机的`routing_key`编写规则

`topic`交换机的`routing_key`不能随意写，必须满足一定的要求，它必须是一个单词列表，以`.`分隔开。这些单词可以是任意单词，比如说：`stock.usd.nyse`、`nyse.vmw`、`quick.orange.rabbit`这种类型的。当然这个单词列表最多不能超过`255`个字节。

在这个规则列表中，有两个替换符：

- `*`（星号）可以代替一个单词
- `#`（井号）可以替代零个或多个单词

> 案例

![](https://cdn.maxqiu.com/upload/339f47d1fbea4c778f76cd7f781b4fb0.png)

上图是一个队列绑定关系图，他们之间数据接收情况如下：

- `quick.orange.rabbit`被队列`Q1` `Q2`接收到
- `lazy.orange.elephant`被队列`Q1` `Q2`接收到
- `quick.orange.fox`被队列`Q1`接收到
- `lazy.brown.fox`被队列`Q2`接收到
- `lazy.pink.rabbit`虽然满足两个绑定但只被队列`Q2`接收一次
- `quick.brown.fox`不匹配任何绑定不会被任何队列接收到会被丢弃
- `quick.orange.male.rabbit`是四个单词不匹配任何绑定会被丢弃
- `lazy.orange.male.rabbit`是四个单词但匹配`Q2`

当队列绑定关系是下列这种情况时需要引起注意

1. 当一个队列绑定键是`#`，那么这个队列将接收所有数据，就有点像`fanout`了
2. 如果队列绑定键当中没有`#`和`*`出现，那么该队列绑定类型就是`direct`了

### 交换机和队列

```java
@Component
public class TopicConfig {
    /**
     * 声明一个主题类型的交换机
     */
    @Bean
    public TopicExchange topic() {
        return new TopicExchange("topic");
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue autoDeleteQueue5() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue autoDeleteQueue6() {
        return new AnonymousQueue();
    }

    /**
     * 声明绑定关系，将队列绑定到交换机，并指定要监听的 routingKey
     */
    @Bean
    public Binding binding5a(TopicExchange topic, Queue autoDeleteQueue5) {
        return BindingBuilder.bind(autoDeleteQueue5).to(topic).with("*.orange.*");
    }

    @Bean
    public Binding binding6a(TopicExchange topic, Queue autoDeleteQueue6) {
        return BindingBuilder.bind(autoDeleteQueue6).to(topic).with("*.*.rabbit");
    }

    @Bean
    public Binding binding6b(TopicExchange topic, Queue autoDeleteQueue6) {
        return BindingBuilder.bind(autoDeleteQueue6).to(topic).with("lazy.#");
    }
}
```

### 消费者

```java
@Component
public class TopicReceiver {
    @RabbitListener(queues = "#{autoDeleteQueue5.name}")
    public void receive1(String msg) {
        System.out.println("===Received1:" + msg);
    }

    @RabbitListener(queues = "#{autoDeleteQueue6.name}")
    public void receive2(String msg) {
        System.out.println("===Received2:" + msg);
    }
}
```

### 生产者

```java
@RestController
public class IndexController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

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
}
```

### 结果

多次访问`http://127.0.0.1:8080/topic`，看到如下结果

```
~~~~Sent:quick.orange.rabbit
~~~~Sent:lazy.orange.elephant
~~~~Sent:quick.orange.fox
~~~~Sent:lazy.brown.fox
~~~~Sent:lazy.pink.rabbit
~~~~Sent:quick.brown.fox
~~~~Sent:quick.orange.male.rabbit
~~~~Sent:lazy.orange.male.rabbit
===Received1:quick.orange.rabbit
===Received2:quick.orange.rabbit
===Received1:lazy.orange.elephant
===Received2:lazy.orange.elephant
===Received1:quick.orange.fox
===Received2:lazy.brown.fox
===Received2:lazy.pink.rabbit
===Received2:lazy.orange.male.rabbit
```

# 进阶使用1

![](https://cdn.maxqiu.com/upload/8e6eb2c864124c729d691099efa79335.png)

如图，一条消息完整的流程分为：

1. 生产者发布
2. RabbitMQ缓存
3. 消费者消费

以上每一个步骤都会出现消息丢失的情况，所以需要进行消息确认

## `Publisher Confirms`发布确认

生产者在发送消息时，如果发送到错误的交换机，或者没有队列可以处理该消息，生产者应当知道消息未发送成功。需要对生产者进行配置。

### 队列、交换机

无需额外配置。这里额外介绍使用`Builder`创建队列和交换机以及使用`new`创建绑定关系

```java
@Component
public class PublisherConfirmsConfig {
    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange publisherConfirmsExchange() {
        // return new DirectExchange("direct");
        // 也可以使用Builder模式创建
        return ExchangeBuilder
            // 使用直连交换机
            .directExchange("publisher.confirms.exchange").build();
    }

    /**
     * 声明确认队列
     */
    @Bean
    public Queue publisherConfirmsQueue() {
        // return new AnonymousQueue();
        // 也可以使用Builder模式创建
        return QueueBuilder
            // 使用消息持久化，不使用nonDurable(final String name)，使用随机队列名称
            .nonDurable()
            // 队列自动删除
            .autoDelete().build();
    }

    /**
     * 声明确认队列绑定关系
     */
    @Bean
    public Binding queueBinding(DirectExchange publisherConfirmsExchange, Queue publisherConfirmsQueue) {
        // return BindingBuilder.bind(publisherConfirmsQueue).to(publisherConfirmsExchange).with("key1");
        // 也可以使用 new 方法创建绑定关系
        return new Binding(publisherConfirmsQueue.getName(), Binding.DestinationType.QUEUE,
            publisherConfirmsExchange.getName(), "key1", null);
    }
}
```

### 消费者

无需额外配置

```java
@Component
public class PublisherConfirmsReceiver {
    @RabbitListener(queues = "#{publisherConfirmsQueue.name}")
    public void receiveMsg(String msg) {
        System.out.println("===Received:" + msg);
    }
}
```

### 生产者

> 配置：生产者的`yml`需要添加如下配置

```yml
spring:
  rabbitmq:
    publisher-confirm-type: correlated # 设置发布确认模式（针对交换机）
    publisher-returns: true # 设置发布退回（针对队列）
```

> 回调：新建回调类，编写回调方法并注入`RabbitTemplate`

```java
@Component
public class RabbitTemplateCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    /**
     * 交换机是否收到消息的一个回调方法
     *
     * @param correlationData
     *            消息相关数据
     * @param ack
     *            交换机是否收到消息
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            System.out.println("交换机已经收到消息 id 为：" + id);
        } else {
            System.out.println("交换机还未收到消息 id 为：" + id + "，由于原因：" + cause);
        }
    }

    /**
     * 队列未接收到消息的时候的回调方法
     *
     * @param message
     *            返回的数据
     */
    @Override
    public void returnedMessage(ReturnedMessage message) {
        System.out.println("消息：" + new String(message.getMessage().getBody()) + "\t被交换机：" + message.getExchange()
            + "退回\t退回原因：" + message.getReplyText() + "\t路由key：" + message.getRoutingKey());
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 在依赖注入 rabbitTemplate 之后再设置它的回调对象
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
        // 同配置文件中的 spring.rabbitmq.publisher-returns=true
        // rabbitTemplate.setMandatory(true);
    }
}
```

> 发送：发送消息时，额外携带`CorrelationData`对象并设置对象id，方便回调时知道是哪一条消息失败

```java
@RestController
public class IndexController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发布确认生产者
     */
    @GetMapping("publisherConfirms")
    public void publisherConfirms() {
        rabbitTemplate.convertAndSend("publisher.confirms.exchange", "key1", "message", new CorrelationData("1"));
        rabbitTemplate.convertAndSend("publisher.confirms.exchange1", "key1", "message", new CorrelationData("2"));
        rabbitTemplate.convertAndSend("publisher.confirms.exchange", "key2", "message", new CorrelationData("3"));
    }
}
```

### 结果

访问`http://127.0.0.1:8080/publisherConfirms`，看到如下结果

```
交换机已经收到消息 id 为：1
2021-10-23 20:48:49.177 ERROR 19532 --- [ 127.0.0.1:5672] o.s.a.r.c.CachingConnectionFactory       : Shutdown Signal: channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'publisher.confirms.exchange1' in vhost '/', class-id=60, method-id=40)
===Received:message
交换机还未收到消息 id 为：2，由于原因：channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'publisher.confirms.exchange1' in vhost '/', class-id=60, method-id=40)
消息：message	被交换机：publisher.confirms.exchange退回	退回原因：NO_ROUTE	路由key：key2
交换机已经收到消息 id 为：3
```

- 如果发送到了错误的交换机，系统会记录`ERROR`日志，且`confirm`中`ack`为`false`
- 如果发送到了错误的队列，系统不会有记录，配置的`returnedMessage`会收到消息，但是`confirm`中`ack`为`true`

## 消息持久化

当消息发送到队列，但是未被消费时，需要将消息存储在磁盘中，以防止`RabbitMQ`服务宕机造成消息丢失

- `new Queue()`方式：使用有参构造器时设置`boolean durable`参数，`true`为磁盘存储，`false`为内存存储
- `QueueBuilder`方式：使用`QueueBuilder.durable()`设置为磁盘存储，`QueueBuilder.nonDurable()`设置为内存存储

设置消息持久化的队列，在`RabbitMQ`控制面板`Features`中会显示为`D`

![](https://cdn.maxqiu.com/upload/d78ac5fb455a43b1af349f2b4ad842c9.jpg)

## `ConsumerAcknowledgements`消费者确认

> ACK确认模式

通过`spring.rabbitmq.listener.simple.acknowledge-mode`与`spring.rabbitmq.listener.direct.acknowledge-mode`进行设置

1. `none`不确认
    - 默认所有消息消费成功，队列会不断的向消费者推送消息
    - 因为`RabbitMQ`认为所有消息都被消费成功，所以消息存在丢失的危险
2. `auto`自动确认（自动）
    - `Spring`依据消息处理逻辑是否抛出异常自动发送`ack`（无异常）或`nack`（异常）到`server`端。存在丢失消息的可能，如果消费端消费逻辑抛出异常，也就是消费端没有处理成功这条消息，那么就相当于丢失了消息。如果消息已经被处理，但后续代码抛出异常，使用`Spring`进行管理的话消费端业务逻辑会进行回滚，这也同样造成了实际意义的消息丢失
    - 使用自动确认模式时，需要考虑的另一件事是消费者过载
3. `manual`手动确认
    - 手动确认则当消费者调用`ack`、`nack`、`reject`几种方法进行确认，手动确认可以在业务失败后进行一些操作，如果消息未被`ACK`则会发送到下一个消费者
    - 手动确认模式可以使用`prefetch`，限制通道上未完成的（“正在进行中的”）发送的数量
    - 忘记ACK确认<br>忘记通过`basicAck`返回确认信息是常见的错误。这个错误非常严重，将导致消费者客户端退出或者关闭后，消息会被退回RabbitMQ服务器，这会使RabbitMQ服务器内存爆满，而且RabbitMQ也不会主动删除这些被退回的消息。只要程序还在运行，没确认的消息就一直是`Unacked`状态，无法被`RabbitMQ`重新投递。`RabbitMQ`消息消费并没有超时机制，也就是说，程序不重启，消息就永远是`Unacked`状态。处理运维事件时不要忘了这些`Unacked`状态的消息。当程序关闭时（实际只要 消费者 关闭就行），消息会恢复为 Ready 状态。

> 消息应答的方法

1. Channel.basicAck（用于肯定确认）RabbitMQ 已知道该消息并且成功的处理消息，可以将其丢弃了
2. Channel.basicNack（用于否定确认）
3. Channel.basicReject（用于否定确认）与Channel.basicNack相比少一个参数

> `multiple`的`true`和`false`

- `true`代表批量应答`channel`上未应答的消息：比如`channel`上有传送`tag`的消息`5,6,7,8`当前`tag`是`8`那么此时`5-8`的这些还未应答的消息都会被确认收到消息应答
- `false`同上面相比只会应答`tag=8`的消息，`5,6,7`这三个消息依然不会被确认收到消息应答

### 队列

```java
@Component
public class ConsumerAcknowledgementsConfig {
    /**
     * 声明确认队列
     */
    @Bean
    public Queue consumerAcknowledgementsQueue() {
        return new Queue("consumer.acknowledgements");
    }
}
```

### 消费者

> `yml`配置

yml
```
spring:
  rabbitmq:
    listener:
      type: simple # 默认
      simple:
        acknowledge-mode: manual
```

> 手动应答

```java
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
```

### 生产者

```java
@RestController
public class IndexController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试用的标记序号
     */
    private static int i = 1;

    /**
     * 消费确认生产者
     */
    @GetMapping("consumerAcknowledgements")
    public Integer consumerAcknowledgements() {
        rabbitTemplate.convertAndSend("consumer.acknowledgements", i);
        return i++;
    }
}
```

# 进阶使用2

## 死信队列

死信：就是无法被消费的消息。一般来说，生产者将消息投递到交换机或者直接到队列，消费者从队列取出消息进行消费，但某些时候由于特定的原因导致队列中的某些消息无法被消费，这样的消息如果没有后续的处理，就变成了死信，有死信自然就有了死信队列。

应用场景：为了保证订单业务的消息数据不丢失，需要使用到`RabbitMQ`的死信队列机制，当消息消费发生异常时，将消息投入死信队列中；还有比如用户在商城下单成功并点击去支付后在指定时间未支付时自动失效

> 死信来源

- 消息 TTL 过期
- 队列达到最大长度(队列满了，无法再添加数据到 mq 中)
- 消息被拒绝(basic.reject 或 basic.nack)并且 requeue=false.

![](https://cdn.maxqiu.com/upload/8b11fa3812fd47d08bafb4579fad5d5b.png)

### 队列和交换机

```java
@Component
public class DeadExchangeConfig {
    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange("dead.exchange");
    }

    @Bean
    public DirectExchange normalExchange() {
        return new DirectExchange("normal.exchange");
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue deadQueue() {
        return new Queue("dead.queue");
    }

    @Bean
    public Queue normalQueue() {
        // 绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        // 设置死信 exchange 参数 key 是固定值
        params.put("x-dead-letter-exchange", "dead.exchange");
        // 设置死信 routing-key 参数 key 是固定值
        params.put("x-dead-letter-routing-key", "key2");
        // 设置队列长度限制
        params.put("x-max-length", 5);
        return new Queue("normal.queue", true, true, false, params);
    }

    /**
     * 声明队列和交换机绑定关系
     */
    @Bean
    public Binding normalQueueBinding(DirectExchange normalExchange, Queue normalQueue) {
        return BindingBuilder.bind(normalQueue).to(normalExchange).with("key1");
    }

    @Bean
    public Binding deadQueueBinding(DirectExchange deadExchange, Queue deadQueue) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with("key2");
    }
}
```

### 消费者

消费者可以暂时不启用，以观察消息的进入死信交换机和队列

```java
// @Component
public class DeadExchangeReceiver {
    @RabbitListener(queues = "dead.queue")
    public void receiveMsg(String msg) {
        System.out.println("Dead===Received:" + msg);
    }
}
```

### 生产者

发送消息时，需要设置超时时间

```java
@RestController
public class IndexController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

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
}
```

### 结果

访问`http://127.0.0.1:8080/deadQueue`，然后查看`RabbitMQ`控制台

- 5条消息进入普通队列，另外5条消息因为队列长度不够，进入死信队列<br>![](https://cdn.maxqiu.com/upload/805bf158c3c4483296e3ee219c6daba3.jpg)
- 当超过10秒之后，因普通队列无消费者，所有消息进入死信队列<br>![](https://cdn.maxqiu.com/upload/51d54169f92248568fadd9a0498c69ab.jpg)

## 延迟交换机

延时队列就是用来存放需要在指定时间被处理的元素的队列。

> 延迟队列需要安装`rabbitmq_delayed_message_exchange`插件

在这里新增了一个队列`delayed.queue`，一个自定义交换机`delayed.exchange`，绑定关系如下:

![](https://cdn.maxqiu.com/upload/39e562856d1448dba4976444feeed7dd.jpg)

### 交换机和队列

```java
@Component
public class DelayedExchangeConfig {
    /**
     * 延迟交换机
     */
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        // 自定义交换机的类型
        args.put("x-delayed-type", "direct");
        return new CustomExchange("delayed.exchange", "x-delayed-message", true, false, args);
    }

    /**
     * 队列
     */
    @Bean
    public Queue delayedQueue() {
        return new Queue("delayed.queue");
    }

    /**
     * 绑定关系
     */
    @Bean
    public Binding bindingDelayedQueue(Queue delayedQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with("delayed.routingKey").noargs();
    }
}
```

### 消费者

```java
@Component
public class DelayedExchangeReceiver {
    @RabbitListener(queues = "delayed.queue")
    public void receiveDelayedQueue(Integer message) {
        System.out.println("当前时间：" + LocalDateTime.now() + "\t收到延时队列的消息：" + message);
    }
}
```

### 生产者

```java
@RestController
public class IndexController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试用的标记序号
     */
    private static int i = 1;

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
}
```

### 结果

访问`http://127.0.0.1:8080/delayedQueue/5000`，输出如下

```
当前时间：2021-10-26T23:08:20.173754	发送延时队列的消息：1	延时5000毫秒
当前时间：2021-10-26T23:08:25.176630500	收到延时队列的消息：1
```

## 备份交换机

备份交换机可以理解为`RabbitMQ`中交换机的“备胎”，当我们为某一个交换机声明一个对应的备份交换机时，就是为它创建一个备胎，当交换机接收到一条不可路由消息时，将会把这条消息转发到备份交换机中，由备份交换机来进行转发和处理，通常备份交换机的类型为`Fanout`，这样就能把所有消息都投递到与其绑定的队列中，然后我们在备份交换机下绑定一个队列，这样所有那些原交换机无法被路由的消息，就会都进入这个队列了。当然，我们还可以建立一个报警队列，用独立的消费者来进行监测和报警。

![](https://cdn.maxqiu.com/upload/21543846f3de4cc5a4d0e0861a175658.jpg)

### 交换机和队列

```java
@Component
public class BackupExchangeConfig {
    /**
     * 声明确认交换机
     */
    @Bean
    public DirectExchange confirmExchange() {
        ExchangeBuilder exchangeBuilder = ExchangeBuilder.directExchange("confirm.exchange").durable(true)
            // 设置该交换机的备份交换机
            .withArgument("alternate-exchange", "backup.exchange");
        return exchangeBuilder.build();
    }

    /**
     * 声明备份交换机
     */
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange("backup.exchange");
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable("confirm.queue").build();
    }

    @Bean
    public Queue backQueue() {
        return QueueBuilder.durable("backup.queue").build();
    }

    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable("warning.queue").build();
    }

    /**
     * 声明绑定关系
     */
    @Bean
    public Binding confirmBinding(Queue confirmQueue, DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with("key1");
    }

    @Bean
    public Binding warningBinding(Queue warningQueue, FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }

    @Bean
    public Binding backupBinding(Queue backQueue, FanoutExchange backupExchange) {
        return BindingBuilder.bind(backQueue).to(backupExchange);
    }
}
```

### 消费者

```java
@Component
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
```

### 生产者

```java
@RestController
public class IndexController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试用的标记序号
     */
    private static int i = 1;

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
```

### 结果

访问`http://127.0.0.1:8080/delayedExchange`，结果如下：

```
发送消息 id 为：9d993eb3-dcf3-424f-9d1b-bc4a2518bb8c	内容为：2
发送消息 id 为：0e3a2ad3-4a54-4c3e-a866-cb96bc59fd61	内容为：2
收到一般消息2
报警发现不可路由消息：2
```
