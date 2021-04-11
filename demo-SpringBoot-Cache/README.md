> 本文档整理自视频教程：[尚硅谷_Spring Boot整合篇](http://www.atguigu.com/download_detail.shtml?v=38)

环境介绍：本文使用`SpringBoot 2.4.4`，视频教程使用的`1.5.x`版本

> 示例代码：
GitHub：[https://github.com/Max-Qiu/demo/tree/main/demo-SpringBoot-Cache](https://github.com/Max-Qiu/demo/tree/main/demo-SpringBoot-Cache)
Gitee：[https://gitee.com/Max-Qiu/demo/tree/main/demo-SpringBoot-Cache](https://gitee.com/Max-Qiu/demo/tree/main/demo-SpringBoot-Cache)

> 官方文档：
[Spring Framework Documentation -- 8. Cache Abstraction](https://docs.spring.io/spring-framework/docs/5.3.5/reference/html/integration.html#cache)
[Spring Boot Reference Documentation -- 13. Caching](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-caching)

# Spring缓存简介

- 从3.1版开始，Spring框架提供了对将缓存透明添加到现有Spring应用程序的支持。与事务 支持类似，缓存抽象允许以一致的方式使用各种缓存解决方案，而对代码的影响最小。
- 从Spring 4.1开始，通过支持JSR-107注释和更多自定义选项，对缓存抽象进行了显着扩展。

缓存抽象的核心是将缓存应用于Java方法，从而根据缓存中可用的信息减少执行次数。也就是说，每次调用目标方法时，抽象都会应用一种缓存行为，该行为检查是否已为给定参数调用了该方法。如果已调用它，则返回缓存的结果，而不必调用实际的方法。如果尚未调用该方法，则将其调用，并将结果缓存并返回给用户，以便下次调用该方法时，将返回缓存的结果。这样，对于给定的参数集和重用的结果，昂贵的方法(无论是CPU绑定还是io绑定)只能调用一次，而不必实际再次调用该方法。缓存逻辑是透明应用的，不会对调用程序造成任何干扰。

# 快速开始使用

使用`SpringBoot`快速搭建，仅需要添加`spring-boot-starter-web`即可

> 第一步：启用缓存

在启动类上添加`@EnableCaching`注解

```java
@SpringBootApplication
@EnableCaching
public class CacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
    }
}
```

> 第二步：添加注解

在需要被缓存的方法上添加`@Cacheable`注解

```java
@Service
public class IndexService {
    @Cacheable("test")
    public String index() {
        System.out.println("执行了 index 方法");
        return "test";
    }
}

@RestController
public class IndexController {
    @Autowired
    private IndexService indexService;

    @GetMapping("/")
    public String index() {
        return indexService.index();
    }
}
```

> 第三步：测试

1. 第一次访问`http://127.0.0.1:8080/`，控制台输出`执行了 index 方法`，页面显示`test`
2. 再次访问`http://127.0.0.1:8080/`，控制台无任何输出，页面依旧显示`test`

# 整合Redis

## 支持的缓存库

如`快速开始使用`示例，如果不添加任何特定的缓存库，`SpringBoot`会自动配置一个使用内存中并发映射的简单提供程序（`Simple`）。当需要缓存时，此提供程序将创建它。不建议将简单的提供程序用于生产用途，但是它对于入门并了解功能非常有用。

> `SpringBoot`会尝试检测以下提供程序：

- Generic
- JCache (JSR-107) (EhCache 3, Hazelcast, Infinispan, and others)
- EhCache 2.x
- Hazelcast
- Infinispan
- Couchbase
- Redis
- Caffeine
- Simple

## 步骤

在`快速开始使用`的基础上

> 第一步：添加依赖

`pom.xml`中添加`redis`依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

> 第二步：配置数据库连接

```yml
spring:
  redis:
    host: 192.168.220.101
    port: 6379
    password: 123
    database: 0
```

> 第三步：查看缓存

1. 重启服务
2. 访问`http://127.0.0.1:8080/`，控制台输出`执行了 index 方法`，页面显示`test`
3. 使用`redis`客户端工具连接`redis`服务端，查看缓存

推荐：免费的`Windows`桌面客户端：[AnotherRedisDesktopManager](https://github.com/qishibo/AnotherRedisDesktopManager/releases)

# 使用注解操作缓存

对于缓存的使用，`Spring`的缓存抽象提供了一组注解：

- @EnableCaching：启用缓存
- @Cacheable：触发缓存填充。
- @CacheEvict：触发缓存逐出。
- @CachePut：在不干扰方法执行的情况下更新缓存。
- @Caching：重新组合要在一个方法上应用的多个缓存操作。
- @CacheConfig：在类级别共享一些与缓存相关的常见设置。

## @EnableCaching

单独使用缓存注解并不会自动触发功能，必须先使用`@EnableCaching`开启缓存功能。同理，如果开发过程中觉得问题出在缓存上，也可以注释`@EnableCaching`关闭缓存后进行调试

> `SpringBoot`中，在启动类上添加`@EnableCaching`开启缓存

```java
@SpringBootApplication
@EnableCaching
public class CacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
    }
}
```

## @Cacheable

顾名思义，使用`@Cacheable`用来设置可缓存的方法，即将结果存储在缓存中的方法，以便在后续调用（具有相同参数）时返回缓存中的值，而无需实际调用该方法。

### 自定义名称`value`

> `@Cacheable`中使用`value`或`cacheNames`设置缓存名称，若仅设置缓存名称，可以省略`value =`或`cacheNames =`

示例如下：

```java
/**
 * 简写 缓存名称
 */
// @Cacheable(value = "cache1")
@Cacheable("cache1")
public String cache1() {
    System.out.println("执行了 cache1 方法");
    return "1";
}
```

在前面的代码段中，该`cache1`方法与名为`cache1`的缓存相关联。每次调用该方法时，都会检查缓存，以查看调用是否已经运行，并且不需要重复。虽然在大多数情况下，仅声明一个缓存，但注释可指定多个名称，以便使用多个缓存。在这种情况下，在调用该方法之前会检查每个缓存，如果命中了至少一个缓存，则会返回关联的值。示例如下：

```java
@Cacheable("cache1")
public String cache1() {
    System.out.println("执行了 cache1 方法");
    return "1";
}

@Cacheable("cache2")
public String cache2() {
    System.out.println("执行了 cache2 方法");
    return "2";
}

/**
 * 使用多个缓存名称
 */
@Cacheable({"cache1", "cache2"})
public String cache3() {
    System.out.println("执行了 cache3 方法");
    return "3";
}
```

- 先调用`cache1`返回 1 ，再调用`cache3`返回 1<br>说明：**命中一个缓存则立即返回**
- 先调用`cache2`返回 2 ，然后调用`cache3`返回 2 ，然后调用`cache1`返回 1 ，最后调用`cache3`返回 1<br>说明：**多个缓存名称时，写在前面的优先级高**
- 先调用`cache3`返回 3 ，再调用`cache1`或`cache2`均返回 3<br>说明：**若多个缓存名称均为空，则多个缓存的值均被写入**

### 自定义键`key`

`@Cacheable`中使用`key`设置缓存键，并且在`key`中可以使用`SpEL`表达式。一般情况下，当方法有参数时，会根据不同的参数缓存不同的值。

> key的默认为如下

- 如果没有给出参数，则使用`SimpleKey.EMPTY`
- 如果仅给出一个参数，则返回该实例。
- 如果给出多个参数，则返回`SimpleKey`包含所有参数的

> 示例：

```java
/**
 * 使用参数做缓存键
 */
@Cacheable(value = "key", key = "#key")
public String key1(String key) {
    System.out.println("执行了 key1 方法，key 的值为：" + key);
    return key;
}

/**
 * 只有一个变量时，若使用当前变量作为键，可以省略不写
 */
@Cacheable(value = "key")
public String key2(String key) {
    System.out.println("执行了 key2 方法，key 的值为：" + key);
    return key;
}

/**
 * 如果给出多个参数，则返回SimpleKey包含所有参数的
 */
@Cacheable(value = "key")
public String key3(String username, String password) {
    System.out.println("执行了 key3 方法，username 的值为：" + username + " password 的值为：" + password);
    return username + " " + password;
}
```

- 如`cache1`方法，未设置`key`且没有参数，使用`cache1::SimpleKey []`作为键
- 如`key1`方法，有参数且设置了`key`，使用`key::1`作为键
- 如`key2`方法，有且仅有一个参数但未设置`key`，使用当前参数作为`key`，即：`key::1`
- 如`key3`方法，有多个参数但为设置`key`，则多个参数均作为`key`，即：`key::SimpleKey [tom,123]`

`SpEL`表达式见官方教程[Spring Expression Language (SpEL)](https://docs.spring.io/spring-framework/docs/5.3.5/reference/html/core.html#expressions)，这里举几个简单的例子

```java
@Cacheable(cacheNames="books", key="#isbn")
public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)

@Cacheable(cacheNames="books", key="#isbn.rawNumber")
public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)

@Cacheable(cacheNames="books", key="T(someType).hash(#isbn)")
public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)
```

### 同步缓存`sync`

在多线程环境中，某些操作可能会为同一个参数并发调用。默认情况下，缓存抽象不会锁定任何内容，并且可能会多次计算相同的值，从而破坏了缓存的目的。对于那些特殊情况，可以使用`sync`属性来设置基础缓存提供程序在计算值时锁定缓存条目。结果，只有一个线程正在忙于计算该值，而其他线程则被阻塞，直到在缓存中更新该条目为止。示例如下

```java
@Cacheable(cacheNames = "sync", sync = true)
public String sync() {
    System.out.println("进入了 sync 方法");
    try {
        // 模拟方法内的值计算
        Thread.sleep(10000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return "sync";
}
```

- 若`sync`为`false`（默认false），则在10秒内多次调用时会多次输出`进入了 sync 方法`
- 若`sync`为`true`，则只会有一次输出`进入了 sync 方法`

### 条件缓存`condition`和`unless`

有时，一种方法可能并不总是适合缓存（例如，它可能取决于给定的参数）。缓存注解通过`condition`参数支持此类方式，该参数采用一个`SpEL`表达式，该表达式的值等于`true`或`false`。如果为`true`，则将缓存该方法。如果不是，它的行为就好像未缓存该方法一样（也就是说，无论缓存中使用什么值或使用了什么参数，每次都会调用该方法）。

> 例：当参数id的大于10时，缓存结果：

```java
@Cacheable(cacheNames = "condition", condition = "#id > 10")
public String condition(Integer id) {
    System.out.println("进入了 condition 方法");
    return "success";
}
```

除了`condition`参数之外，还可以使用`unless`参数来否决将值添加到缓存中。`unless`是对返回值进行判断

> 例：当随机数大于3时，将结果放入缓存

```java
@Cacheable(cacheNames = "unlessResult", unless = "#result > 3")
public Integer unlessResult() {
    System.out.println("进入了 unlessResult 方法");
    return (int)(Math.random() * 10);
}
```

## @CachePut

始终调用方法，并刷新缓存

> 例：

```java
@Cacheable("cache1")
public String cache1() {
    System.out.println("执行了cache1方法");
    return "1";
}

@CachePut("cache1")
public String cachePut() {
    System.out.println("执行了 cachePut 方法");
    return "success";
}
```

1. 先调用`cache1`返回`1`
2. 然后调用`cachePut`返回`success`
3. 再调用`cache1`返回`success`（缓存被刷新了）

`@CachePut`中的参数`value`、`key`、`condition`、`unless`使用方法同`@Cacheable`

> 强烈建议不要在同一方法上同时使用`@CachePut`和`@Cacheable`注释

## @CacheEvict

删除缓存

> 例：

在`@CachePut`示例的基础上增加如下方法

```
@CacheEvict("cache1")
public void cacheEvict1() {
    System.out.println("执行了 cacheEvict1 方法");
}
```

1. 先调用`cachePut`返回`success`
2. 然后调用`cache1`返回`success`
3. 然后调用`cacheEvict1`，再查看`redis`客户端，缓存被删除了
4. 最后调用`cache1`返回`1`，新缓存又进去了

### 删除所有键`allEntries`

当`allEntries`设置为`true`时，删除相同缓存名称下的所有键。默认为`false`

> 例：

```java
@CacheEvict(value = "key", allEntries = true)
public void cacheEvict2() {
    System.out.println("执行了 cacheEvict2 方法");
}
```

1. 调用几次`key1`方法，存入名称相同但键不同的缓存。如：`key::1`、`key::2`
2. 调用`cacheEvict2`方法，发现所有的名称为`key`的缓存均被删除了

### 在执行方法前执行缓存清除`beforeInvocation`

当`beforeInvocation`设置为`true`时，在方法执行前执行缓存删除。即使方法执行抛出异常

> 例：

```java
@CacheEvict(value = "key", beforeInvocation = true)
public void cacheEvict3(Integer key) {
    System.out.println("执行了 cacheEvict3 方法");
    int i = key / 0;
    System.out.println("cacheEvict3 执行完毕");
}
```

1. 调用`key1`方法，存入一个缓存。如：`key::1`
2. 调用`cacheEvict3`方法，删除相同的缓存，虽然报错，但是缓存已经删除了

## @Caching

有时，需要指定相同类型的多个注释（例如`@CacheEvict`或`@CachePut`）。例如：因为条件或键表达式在不同的缓存之间是不同的。`@Caching`让多个嵌套`@Cacheable`、`@CachePut`和`@CacheEvict`注解来在相同的方法中使用。

下面的示例使用两个`@CacheEvict`注释：

```java
@Caching(evict = { @CacheEvict("primary"), @CacheEvict(cacheNames="secondary", key="#p0") })
public Book importBooks(String deposit, Date date)
```

## @CacheConfig

在类级别上定义共同的设置项

> 例：`@CacheConfig`设置缓存的名称：

```java
@CacheConfig("books") 
public class BookRepositoryImpl implements BookRepository {
    @Cacheable
    public Book findBook(ISBN isbn) {...}
}
```

`@CacheConfig`是一个类级别的注释，它允许共享`缓存名称`、自定义`KeyGenerator`、自定义`CacheManager`和自定义`CacheResolver`。将此注释放在类上不会打开任何缓存操作。

# 自定义`RedisCacheManager`

通过自定义`RedisCacheManager`，可以修改序列化工具、存储时间、空值是否缓存等

## 全局自定义

> 例：

```java
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 缓存配置对象
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
            // 设置缓存的默认超时时间：30分钟
            .entryTtl(Duration.ofMinutes(30L))
            // 如果是空值，不缓存
            .disableCachingNullValues()
            // 设置key序列化器
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            // 设置value序列化器
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
            .cacheDefaults(configuration).build();
    }
}
```

## 局部自定义

当某个缓存需要自定义过期时间或其他设置时，可以通过配置多个`RedisCacheManager`以及使用`@Cacheable`的`cacheManager`属性指定对应的`RedisCacheManager`即可

> 第一步：定义多个`RedisCacheManager`

```java
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    /**
     * 默认Redis全局配置。（用不超时）
     * 
     * @param redisConnectionFactory
     * @return
     */
    @Primary
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(instanceConfig(0L)).build();
    }

    /**
     * 1分钟超时
     */
    @Bean
    public RedisCacheManager expire1min(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(60 * 60L)).build();
    }

    /**
     * 2小时超时
     */
    @Bean
    public RedisCacheManager expire2h(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(60 * 60 * 2L)).build();
    }

    /**
     * 一天超时
     */
    @Bean
    public RedisCacheManager expire1day(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(60 * 60 * 24L)).build();
    }

    /**
     * 通用配置
     * 
     * @param ttl
     *            超时时间（秒）
     */
    private RedisCacheConfiguration instanceConfig(Long ttl) {
        // 缓存配置对象
        return RedisCacheConfiguration.defaultCacheConfig()
            // 设置缓存的默认超时时间
            .entryTtl(Duration.ofSeconds(ttl))
            // 如果是空值，不缓存
            .disableCachingNullValues()
            // 设置key序列化器
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            // 设置value序列化器
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
```

> 第二步：指定`RedisCacheManager`

```
@Cacheable(value = "cacheManager", cacheManager = "expire1min")
public String cacheManager() {
    System.out.println("执行了 cache1 方法");
    return "1";
}
```

1. 调用`cacheManager`
2. 使用`redis`客户端查看该值的超时时间

## 缓存可用的SpEL元数据

> 拷贝自官方文档

Name | Location | Description | Example
---|---|---|---
methodName | Root object | The name of the method being invoked | `#root.methodName`
method | Root object | The method being invoked | `#root.method.name`
target | Root object | The target object being invoked | `#root.target`
targetClass | Root object | The class of the target being invoked | `#root.targetClass`
args | Root object | The arguments (as array) used for invoking the target | `#root.args[0]`
caches | Root object | Collection of caches against which the current method is run | `#root.caches[0].name`
Argument name | Evaluation context | Name of any of the method arguments. If the names are not available (perhaps due to having no debug information), the argument names are also available under the `#a<#arg>` where `#arg` stands for the argument index (starting from 0). | `#iban` or `#a0` (you can also use `#p0` or `#p<#arg>` notation as an alias).
result | Evaluation context | The result of the method call (the value to be cached). Only available in `unless` expressions, `cache put` expressions (to compute the `key`), or `cache evict` expressions (when `beforeInvocation` is `false`). For supported wrappers (such as `Optional`), `#result` refers to the actual object, not the wrapper. | `#result`
