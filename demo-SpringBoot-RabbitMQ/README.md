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

入门示例中，展示了一个生产者和一个消费者的情况。在下图中：

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

## `Routing`路由

本节将添加一些特别的功能：比方说只让某个消费者订阅发布的**部分**消息。例如只把严重错误消息定向存储到日志文件，同时仍然能够在控制台上打印所有日志消息。

> 绑定

绑定是交换器和队列之间的关系。这可以简单地理解为：**队列只对它绑定的交换机的消息感兴趣**。绑定可以采用额外的绑定键参数。`Spring AMQP`使用了一个`fluent API`来让这种关系非常清晰。将交换机和队列传入`BindingBuilder`并简单地用绑定键将队列绑定到交换机，如下所示：

```java
@Bean
public Binding binding1a(DirectExchange direct,
    Queue autoDeleteQueue1) {
    return BindingBuilder.bind(autoDeleteQueue1)
        .to(direct)
        .with("orange");
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

## 主题

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
     *
     * @return
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

## `Publisher Confirms`发布确认

![](https://cdn.maxqiu.com/upload/8e6eb2c864124c729d691099efa79335.png)

如上图，生产者在发送消息时，

# 进阶使用2

- 死信队列
- 延迟队列

