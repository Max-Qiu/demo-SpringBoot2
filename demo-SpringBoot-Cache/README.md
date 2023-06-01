> 本文档整理自视频教程：[尚硅谷_Spring Boot整合篇](http://www.atguigu.com/download_detail.shtml?v=38)

环境介绍：本文使用`SpringBoot 2.7.x`，视频教程使用的`1.5.x`版本

---

> 官方文档：

- [Spring Framework Documentation -- Integration -- 8. Cache Abstraction](https://docs.spring.io/spring-framework/docs/5.3.26/reference/html/integration.html#cache)
- [Spring Boot Reference Documentation -- IO -- 1. Caching](https://docs.spring.io/spring-boot/docs/2.7.10/reference/html/io.html#io.caching)

# Spring缓存简介

- 从3.1版开始，Spring框架提供了对将缓存透明添加到现有Spring应用程序的支持。与事务 支持类似，缓存抽象允许以一致的方式使用各种缓存解决方案，而对代码的影响最小。
- 从Spring 4.1开始，通过支持JSR-107注释和更多自定义选项，对缓存抽象进行了显着扩展。

缓存抽象的核心是将缓存应用于Java方法，从而根据缓存中可用的信息减少执行次数。也就是说，每次调用目标方法时，抽象都会应用一种缓存行为，该行为检查是否已为给定参数调用了该方法。如果已调用它，则返回缓存的结果，而不必调用实际的方法。如果尚未调用该方法，则将其调用，并将结果缓存并返回给用户，以便下次调用该方法时，将返回缓存的结果。这样，对于给定的参数集和重用的结果，昂贵的方法(无论是CPU绑定还是io绑定)只能调用一次，而不必实际再次调用该方法。缓存逻辑是透明应用的，不会对调用程序造成任何干扰。

# 快速开始使用

使用 `SpringBoot` 快速搭建

> 第一步：添加依赖

在 `pom.xml` 添加如下依赖 `spring-boot-starter-cache`

```xml
<!-- 缓存组件 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<!-- Web基础环境 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

说明：缓存的子依赖（主要是 `spring-context-support` 和 `spring-boot-starter` ）在大部分其他组件中都依赖了（如 `spring-boot-starter-web` ），保险起见还是要手动添加一下

> 第二步：启用缓存

在启动类上添加 `@EnableCaching` 注解

```java
@EnableCaching
@SpringBootApplication
public class CacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
    }
}
```

> 第三步：添加注解

在需要被缓存的方法上添加 `@Cacheable` 注解

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

1. 第一次访问 `http://127.0.0.1:8080/` ，控制台输出 `执行了 index 方法` ，页面显示 `test`
2. 再次访问 `http://127.0.0.1:8080/` ，控制台无任何输出，页面显示 `test`

# 整合第三方缓存库（以 Redis 为例）

## 支持的缓存库

如 `快速开始使用` 示例，如果不添加任何特定的缓存库， `SpringBoot` 会自动配置一个使用内存中并发映射的简单提供程序（ `Simple` ）。当需要缓存时，此提供程序将创建它。不建议将简单的提供程序用于生产用途。 `SpringBoot` 支持以下缓存库：

- Generic
- JCache (JSR-107) (EhCache 3, Hazelcast, Infinispan, and others)
- EhCache 2.x
- Hazelcast
- Infinispan
- Couchbase
- Redis
- Caffeine
- Simple

## 整合步骤

在`快速开始使用`的基础上

> 第一步：添加依赖

`pom.xml`中添加`redis`依赖

```xml
<!-- Redis数据库依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!-- 连接池依赖 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
<!-- fastjson2 -->
<dependency>
    <groupId>com.alibaba.fastjson2</groupId>
    <artifactId>fastjson2</artifactId>
    <version>2.0.26</version>
</dependency>
<dependency>
    <groupId>com.alibaba.fastjson2</groupId>
    <artifactId>fastjson2-extension-spring5</artifactId>
    <version>2.0.26</version>
</dependency>
```

> 第二步：配置数据库连接

```yml
spring:
  redis:
    host: 127.0.0.1 # 地址
    port: 6379 # 端口
    # 通用配置
    username: # 用户名
    password: 123 # 密码
    database: 0 # 指定数据库序号
    ssl: false # 是否启用SSL
    connect-timeout: 1000 # 连接超时时间（毫秒）
    timeout: 1000 # 操作超时时间（毫秒）
    client-name: # 客户端名称（不知道干嘛用的）
    client-type: lettuce # 驱动类型
    # 连接池配置
    lettuce:
      pool:
        min-idle: 1 # 最小空闲连接（默认0）
        max-idle: 8 # 最大空闲连接（默认8）
        max-active: 16 # 最大连接数（默认8，使用负值表示没有限制）
        max-wait: -1ms # 最大阻塞等待时间（默认-1，负数表示没限制）
```

完整的Redis数据库配置请参考：[SpringBoot2.7.x整合Redis（RedisTemplate操作五大常用数据类型）](https://maxqiu.com/article/detail/102)

> 第三步：修改缓存配置

通过自定义 `RedisCacheManager` ，可以修改键值的序列化方案、存储时间、空值是否缓存等。当某个缓存需要自定义过期时间或其他设置时，可以通过配置多个 `RedisCacheManager` 以及使用 `@Cacheable` 或 `@CacheConfig` 的 `cacheManager` 属性指定对应的 `RedisCacheManager` 即可

```java
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson2.support.spring.data.redis.GenericFastJsonRedisSerializer;

/**
 * Redis缓存配置
 *
 * @author Max_Qiu
 */
@Configuration
public class RedisCacheConfig {
    /**
     * 默认Redis全局配置。（30分钟超时）
     */
    @Primary
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(instanceConfig(30L)).build();
    }

    /**
     * 永不超时
     */
    @Bean
    public RedisCacheManager noExpire(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(0L)).build();
    }

    /**
     * 2小时超时
     */
    @Bean
    public RedisCacheManager expire2h(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(60 * 2L)).build();
    }

    /**
     * 一天超时
     */
    @Bean
    public RedisCacheManager expire1day(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(60 * 24L)).build();
    }

    /**
     * 通用配置
     *
     * @param ttl
     *            超时时间（分钟）
     */
    private RedisCacheConfiguration instanceConfig(Long ttl) {
        // 缓存配置对象
        return RedisCacheConfiguration.defaultCacheConfig()
            // 设置缓存的默认超时时间
            .entryTtl(Duration.ofMinutes(ttl))
            // 如果是空值，不缓存（不建议设置，防止缓存穿透）
            // .disableCachingNullValues()
            // 设置key序列化器
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer(StandardCharsets.UTF_8)))
            // 设置value序列化器（这里使用阿里巴巴的Fastjson格式化工具）
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
    }
}
```

> 第四步：查看缓存

1. 重启服务
2. 访问 `http://127.0.0.1:8080/` ，控制台输出 `执行了 index 方法` ，页面显示 `test`
3. 使用 `redis` 客户端工具连接 `redis` 服务端，查看缓存

推荐：免费的`Windows`桌面客户端：[AnotherRedisDesktopManager](https://github.com/qishibo/AnotherRedisDesktopManager/releases)

# 详细使用教程

## 使用注解操作缓存

对于缓存的使用， `Spring` 的缓存抽象提供了一组注解：

- @EnableCaching：启用缓存
- @Cacheable：触发缓存填充。
- @CacheEvict：触发缓存逐出。
- @CachePut：在不干扰方法执行的情况下更新缓存。
- @Caching：重新组合要在一个方法上应用的多个缓存操作。
- @CacheConfig：在类级别共享一些与缓存相关的常见设置。

### @EnableCaching

单独使用缓存注解并不会自动触发功能，必须先使用 `@EnableCaching` 开启缓存功能。同理，如果开发过程中觉得问题出在缓存上，也可以注释 `@EnableCaching` 关闭缓存后进行调试

> `SpringBoot` 中，在启动类上添加 `@EnableCaching` 开启缓存

```java
@SpringBootApplication
@EnableCaching
public class CacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
    }
}
```

### @Cacheable

顾名思义，使用 `@Cacheable` 用来设置需要缓存结果的方法，以便在后续调用（具有相同参数）时返回缓存中的值，无需实际调用该方法。

#### 自定义名称 `value`

> `@Cacheable` 中使用 `value` 或 `cacheNames` 设置缓存名称，若仅设置缓存名称，可以省略 `value =` 或 `cacheNames =`

示例如下：

```java
/**
 * 简写 缓存名称
 */
// @Cacheable(value = "cache1")
// @Cacheable(cacheNames = "cache1")
@Cacheable("cache1")
public String cache1() {
    System.out.println("执行了 cache1 方法");
    return "1";
}
```

在前面的代码段中，该 `cache1` 方法与名为 `cache1` 的缓存相关联。每次调用该方法时，都会检查缓存，以查看调用是否已经运行，并且不需要重复。虽然在大多数情况下，仅声明一个缓存，但注释可指定多个名称，以便使用多个缓存。在这种情况下，在调用该方法之前会检查每个缓存，如果命中了至少一个缓存，则会返回关联的值。示例如下：

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

- 先调用 `cache1` 返回 1 ，再调用 `cache3` 返回 1<br>说明：**命中一个缓存则立即返回**
- 先调用 `cache2` 返回 2 ，然后调用 `cache3` 返回 2 ，然后调用 `cache1` 返回 1 ，最后调用 `cache3` 返回 1<br>说明：**多个缓存名称时，写在前面的优先级高**
- 先调用 `cache3` 返回 3 ，再调用 `cache1` 或 `cache2` 均返回 3<br>说明：**若多个缓存名称均为空，则多个缓存的值均被写入**

#### 自定义键 `key`

`@Cacheable` 中使用 `key` 设置缓存键，并且在 `key` 中可以使用 `SpEL` 表达式。一般情况下，当方法有参数时，会根据不同的参数缓存不同的值。

> 示例：

```java
/**
 * 使用参数做缓存键
 */
@Cacheable(value = "key", key = "#key")
public String key(String key) {
    System.out.println("执行了 key 方法，key 的值为：" + key);
    return key;
}
```

注：详细 `key` 生成规则见下文

#### 同步缓存 `sync`

在多线程环境中，某些操作可能会为同一个参数并发调用。默认情况下，缓存抽象不会锁定任何内容，并且可能会多次计算相同的值，从而破坏了缓存的目的。对于那些特殊情况，可以使用`sync`属性来设置基础缓存提供程序在计算值时锁定缓存条目。结果，只有一个线程正在忙于计算该值，而其他线程则被阻塞，直到在缓存中更新该条目为止。示例如下

```java
@Cacheable(cacheNames = "sync", sync = true)
public String sync() {
    System.out.println("进入了 sync 方法");
    try {
        // 模拟方法内的值计算
        Thread.sleep(3000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return "sync";
}
```

- 若 `sync` 为 `false` （默认false），则在3秒内多次调用时会多次输出 `进入了 sync 方法`
- 若 `sync` 为 `true` ，则只会有一次输出 `进入了 sync 方法`

#### 条件缓存 `condition` ：根据入参判断是否缓存结果

`condition` 参数通过**给定的参数**判断是否缓存结果，该参数采用一个`SpEL`表达式，该表达式的值等于 `true` 或 `false`。

- `true`：缓存结果
- `false`：不缓存结果

> 例：当参数id的大于10时，缓存结果：

```java
@Cacheable(cacheNames = "condition", condition = "#id > 10")
public String condition(Integer id) {
    System.out.println("进入了 condition 方法");
    return "success";
}
```

- 当 `id>10` 时，条件成立，同一值第一次进入会打印输出，后续进入不打印输出，直接调取缓存
- 当 `id<=10` 时，条件不成立，总是会打印输出

#### 条件缓存 `unless`：根据结果判断是否缓存结果

`unless` 参数通过**返回的结果**判断是否缓存结果，该参数采用一个 `SpEL` 表达式，该表达式的值等于 `true` 或 `false`。

`unless` 英文是**除非**的意思，意思就是**除了这个条件成立都缓存**，又或者**这个条件成立就不缓存**

- `true` ：不缓存结果
- `false` ：缓存结果

> 例：当随机数大于0时，不缓存结果：

```java
@Cacheable(cacheNames = "unless", unless = "#result > 0")
public Integer unless() {
    System.out.println("进入了 unless 方法");
    return new Random().nextInt(10);
}
```

- 先打印输出，当 `结果大于0` 时，条件成立，不缓存结果
- 下次进入再次打印输出，当 `结果小于等于0` 时，条件不成立，缓存结果，后续进入不打印输出，直接调取缓存

#### 自定义 `CacheManager`

指定 `CacheManager`，可以修改指定的配置项，例如缓存时长不同

```java
@Cacheable(cacheNames = "cacheManager", cacheManager = "expire1day")
public String cacheManager() {
    System.out.println("执行了 cacheManager 方法");
    return "1";
}
```

1. 调用 `cacheManager`
2. 使用 `redis` 客户端查看该值的超时时间

### @CachePut

始终调用方法，并刷新缓存

> 例：

```java
@CachePut("cache1")
public String cachePut() {
    System.out.println("执行了 cachePut 方法");
    return "success";
}
```

1. 先调用 `cache1` 返回 `1`
2. 然后调用 `cachePut` 返回 `success`
3. 再调用 `cache1` 返回 `success` （缓存被刷新了）

`@CachePut` 中的参数 `value` 、 `key` 、 `condition` 、 `unless` 使用方法同 `@Cacheable`

> 强烈建议不要在同一方法上同时使用 `@CachePut` 和 `@Cacheable` 注释

### @CacheEvict

删除缓存

> 例：

```java
@CacheEvict("cache1")
public void cacheEvict() {
    System.out.println("执行了 cacheEvict 方法");
}
```

1. 先调用 `cache1` 返回 `success`
3. 然后调用 `cacheEvict1` ，再查看 `redis` 客户端，缓存被删除了
4. 最后调用 `cache1` 返回 `1`，新缓存又进去了

#### 删除所有键 `allEntries`

当 `allEntries` 设置为 `true` 时，删除相同缓存名称下的所有键。默认为 `false`

> 例：

```java
@CacheEvict(cacheNames = "key", allEntries = true)
public void cacheEvictAllEntries() {
    System.out.println("执行了 cacheEvictAllEntries 方法");
}
```

1. 调用几次 `key1` 方法，存入名称相同但键不同的缓存。如： `key::1` 、 `key::2`
2. 调用 `cacheEvictAllEntries` 方法，发现所有的名称为 `key` 的缓存均被删除了

#### 在执行方法前执行缓存清除 `beforeInvocation`

当 `beforeInvocation` 设置为 `true` 时，在方法执行前执行缓存删除。即使方法执行抛出异常

> 例：

```java
@CacheEvict(cacheNames = "key", beforeInvocation = true)
public void cacheEvictBeforeInvocation(String key) {
    System.out.println("执行了 cacheEvictBeforeInvocation 方法");
    @SuppressWarnings({"divzero", "NumericOverflow"})
    int i = 1 / 0;
    System.out.println(i);
    System.out.println("cacheEvict3 执行完毕");
}
```

1. 调用 `key1` 方法，存入一个缓存。如： `key::1`
2. 调用 `cacheEvictBeforeInvocation` 方法，删除相同的缓存，虽然报错，但是缓存已经删除了

### @Caching

有时，需要指定相同类型的多个注释（例如 `@CacheEvict` 或 `@CachePut` ）。例如：因为条件或键表达式在不同的缓存之间是不同的。 `@Caching` 让多个嵌套 `@Cacheable` 、 `@CachePut` 和 `@CacheEvict` 注解来在相同的方法中使用。

下面的示例使用两个 `@CacheEvict` 注释：

```java
@Caching(evict = { @CacheEvict("primary"), @CacheEvict(cacheNames="secondary", key="#p0") })
public Book importBooks(String deposit, Date date)
```

### @CacheConfig

在类级别上定义共同的设置项

> 例： `@CacheConfig` 设置缓存的名称：

```java
@CacheConfig(cacheNames = "cache")
public class BookRepositoryImpl implements BookRepository {
    @Cacheable
    public Book findBook(ISBN isbn) {...}
}
```

`@CacheConfig` 是一个类级别的注释，它允许共享`缓存名称`、自定义 `KeyGenerator` 、自定义 `CacheManager` 和自定义 `CacheResolver` 。将此注释放在类上不会打开任何缓存操作。

## 缓存名生成规则

### 默认的 SimpleKeyGenerator

- 默认情况下是 `org.springframework.cache.interceptor.SimpleKeyGenerator` ，使用 `[cacheNames]::[key]` 作为键
- `[cacheNames]` 在 `@CacheConfig` 和 `@Cacheable` 中必须配置一个， `@Cacheable` 可以覆盖 `@CacheConfig`
- `[key]` 是根据 `@CacheConfig` 或 `@Cacheable` 的 `keyGenerator` 配置进行生成的， `@Cacheable` 可以覆盖 `@CacheConfig`

详细生成规则见如下代码

```java
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maxqiu.demo.request.UserRequest;

@Service
@CacheConfig(cacheNames = "cacheName")
public class CacheKeyService {
    /**
     * 普通方法
     *
     * key: "cacheName::SimpleKey []"
     *
     * 未配置cacheNames，使用 @CacheConfig 的配置
     */
    @Cacheable
    public String normal() {
        System.out.println("执行了normal方法");
        return "normal";
    }

    /**
     * 无参数方法
     *
     * key: "myCacheName::SimpleKey []"
     *
     * 配置了cacheNames，覆盖 @CacheConfig 的配置；无参数，生成空的SimpleKey
     */
    @Cacheable(cacheNames = "myCacheName")
    public String noParameter() {
        System.out.println("执行了normal方法");
        return "noParameter";
    }

    /**
     * 普通参数方法
     *
     * key: "myCacheName::1"
     *
     * "1"是parameter的值
     */
    @Cacheable(cacheNames = "myCacheName")
    public Integer singleParameter(Integer parameter) {
        System.out.println("执行了singleParameter方法：" + parameter);
        return parameter;
    }

    /**
     * 实体参数方法
     *
     * key: "myCacheName::UserRequest(id=1, name=张三)"
     *
     * 调用实体的`toString()`方法作为key（实体必须重写toString()方法，否则出异常）
     */
    @Cacheable(cacheNames = "myCacheName")
    public UserRequest requestParameter(UserRequest request) {
        System.out.println("执行了multiParameter方法：" + request);
        return request;
    }

    /**
     * 多普通参数方法
     *
     * key: "myCacheName::SimpleKey [1,2]"
     *
     * 多个参数组装成数组
     */
    @Cacheable(cacheNames = "myCacheName")
    public Integer multiParameter(Integer parameter1, Integer parameter2) {
        System.out.println("执行了multiParameter方法：" + parameter1 + parameter2);
        return parameter1 + parameter2;
    }

    /**
     * 混合参数方法
     *
     * key: "myCacheName::SimpleKey [UserRequest(id=2, name=李四),张]"
     *
     * 多个参数组装成数组
     */
    @Cacheable(cacheNames = "myCacheName")
    public UserRequest requestParameter2(UserRequest request, String search) {
        System.out.println("执行了multiParameter2方法：" + request + search);
        return request;
    }

    /**
     * 集合参数方法
     *
     * key: "myCacheName::1,2,3"
     *
     * 参数为集合的值
     */
    @Cacheable(cacheNames = "myCacheName")
    public List<Integer> listParameter(List<Integer> ids) {
        System.out.println("执行了listParameter方法：" + ids);
        return ids;
    }
}
```

### 使用 `SpEL` 表达式自定义key

- 一般使用缓存时，希望缓存的名称为 `[serviceName]::[methodName]::[parameterValues]` 的三段式
- 此时 `@CacheConfig` 的 `cacheNames` 配置当前类的名称，完成第一段
- 此时 `@Cacheable` 的 `key` 配置当前方法的名和参数名（ `SpEL` 表达式），完成第二段

详细生成规则见如下代码

```java
package com.maxqiu.demo.service;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maxqiu.demo.request.UserRequest;

@Service
@CacheConfig(cacheNames = "CacheCustomKey")
public class CacheCustomKeyService {
    /**
     * 无参数方法
     *
     * key: "CacheCustomKey::noParameter"
     */
    @Cacheable(key = "#root.methodName")
    public String noParameter() {
        System.out.println("执行了normal方法");
        return "noParameter";
    }

    /**
     * 普通参数方法，参数可以 #p0 #p1 ... 也可以写具体的变量名
     *
     * key: "CacheCustomKey::singleParameter::1"
     *
     * "1"是parameter的值
     */
    // @Cacheable(key = "#root.methodName+'::'+#p0")
    @Cacheable(key = "#root.methodName+'::'+#parameter")
    public Integer singleParameter(Integer parameter) {
        System.out.println("执行了singleParameter方法：" + parameter);
        return parameter;
    }

    /**
     * 实体参数方法
     *
     * key: "CacheCustomKey::requestParameter::UserRequest(id=1, name=张三)"
     *
     * 调用实体的`toString()`方法作为key（实体必须重写toString()方法，否则为对象内存地址，内存地址每次不一样，无法起到缓存作用）
     */
    @Cacheable(key = "#root.methodName+'::'+#request")
    public UserRequest requestParameter(UserRequest request) {
        System.out.println("执行了multiParameter方法：" + request);
        return request;
    }

    /**
     * 多普通参数方法
     *
     * key: "CacheCustomKey::multiParameter::1,2"
     *
     * 多个参数组装成数组
     */
    @Cacheable(key = "#root.methodName+'::'+#p0+','+#p1")
    public Integer multiParameter(Integer parameter1, Integer parameter2) {
        System.out.println("执行了multiParameter方法：" + parameter1 + parameter2);
        return parameter1 + parameter2;
    }

    /**
     * 混合参数方法
     *
     * key: "CacheCustomKey::requestParameter2::UserRequest(id=2, name=李四),李"
     *
     * 多个参数组装成数组
     */
    @Cacheable(key = "#root.methodName+'::'+#p0+','+#p1")
    public UserRequest requestParameter2(UserRequest request, String search) {
        System.out.println("执行了multiParameter2方法：" + request + search);
        return request;
    }

    /**
     * 集合参数方法
     *
     * key: "CacheCustomKey::listParameter::1,2,3"
     *
     * 参数为集合的值
     */
    @Cacheable(key = "#root.methodName+'::'+#p0")
    public List<Integer> listParameter(List<Integer> ids) {
        System.out.println("执行了listParameter方法：" + ids);
        return ids;
    }
}
```

> 缓存可用的 `SpEL` 元数据

拷贝自官方文档

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

> `SpEL`使用示例

`SpEL` 表达式见官方教程 [Spring Expression Language (SpEL)](https://docs.spring.io/spring-framework/docs/5.3.5/reference/html/core.html#expressions) ，这里举几个简单的例子

```java
@Cacheable(cacheNames="books", key="#isbn")
public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)

@Cacheable(cacheNames="books", key="#isbn.rawNumber")
public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)

@Cacheable(cacheNames="books", key="T(someType).hash(#isbn)")
public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)
```

### 使用自定 `KeyGenerator` 生成 `key`

使用 `@Cacheable` 的 `key` 每次都要写 `SpEL` 表达式，很是麻烦。可以使用自定义 `KeyGenerator` 实现 `org.springframework.cache.interceptor.KeyGenerator` 接口，然后在 `@CacheConfig` 和 `@Cacheable` 中配置， `@Cacheable` 可以覆盖 `@CacheConfig`

> 自定义 `KeyGenerator`

```java
package com.maxqiu.demo.config;

import java.lang.reflect.Method;
import java.util.StringJoiner;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

@Component
public class CacheKeyGenerator implements KeyGenerator {
    // key分隔符
    private static final String KEY_SEPARATOR = "::";
    // params分隔符
    private static final String PARAMS_SEPARATOR = ",";

    /**
     * 生成器
     *
     * @param target
     *            目标实例
     * @param method
     *            被调用的方法
     * @param params
     *            方法参数(扩展了任何var-args)
     */
    @Override
    public String generate(Object target, Method method, Object... params) {
        // 键
        StringBuilder builder = new StringBuilder(30);
        // 类名（可选）
        // builder.append(target.getClass().getSimpleName());
        // builder.append(separator);
        // 方法名
        builder.append(method.getName());
        if (params.length == 0) {
            return builder.toString();
        }
        // 参数
        builder.append(KEY_SEPARATOR);
        builder.append(generateKey(params));
        return builder.toString();
    }

    private String generateKey(Object... params) {
        StringJoiner sj = new StringJoiner(PARAMS_SEPARATOR);
        for (Object elem : params) {
            sj.add(String.valueOf(elem));
        }
        return sj.toString();
    }
}
```

详细生成规则见如下代码

```java
package com.maxqiu.demo.service;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maxqiu.demo.request.UserRequest;

@Service
@CacheConfig(cacheNames = "CacheCustomKeyGenerator", keyGenerator = "cacheKeyGenerator")
public class CacheCustomKeyGeneratorService {
    /**
     * 无参数方法
     *
     * key: "CacheCustomKeyGenerator::noParameter"
     */
    @Cacheable
    public String noParameter() {
        System.out.println("执行了normal方法");
        return "noParameter";
    }

    /**
     * 普通参数方法
     *
     * key: "CacheCustomKeyGenerator::singleParameter::1"
     *
     * "1"是parameter的值
     */
    @Cacheable
    public Integer singleParameter(Integer parameter) {
        System.out.println("执行了singleParameter方法：" + parameter);
        return parameter;
    }

    /**
     * 实体参数方法
     *
     * key: "CacheCustomKeyGenerator::requestParameter::UserRequest(id=1, name=张三)"
     *
     * 调用实体的`toString()`方法作为key（实体必须重写toString()方法，否则为对象内存地址，内存地址每次不一样，无法起到缓存作用）
     */
    @Cacheable
    public UserRequest requestParameter(UserRequest request) {
        System.out.println("执行了multiParameter方法：" + request);
        return request;
    }

    /**
     * 多普通参数方法
     *
     * key: "CacheCustomKeyGenerator::multiParameter::1,2"
     *
     * 多个参数组装成数组
     */
    @Cacheable
    public Integer multiParameter(Integer parameter1, Integer parameter2) {
        System.out.println("执行了multiParameter方法：" + parameter1 + parameter2);
        return parameter1 + parameter2;
    }

    /**
     * 混合参数方法
     *
     * key: "CacheCustomKeyGenerator::requestParameter2::UserRequest(id=2, name=李四),李"
     *
     * 多个参数组装成数组
     */
    @Cacheable
    public UserRequest requestParameter2(UserRequest request, String search) {
        System.out.println("执行了multiParameter2方法：" + request + search);
        return request;
    }

    /**
     * 集合参数方法
     *
     * key: "CacheCustomKeyGenerator::listParameter::[1, 2, 3]"
     *
     * 参数为集合的值
     */
    @Cacheable
    public List<Integer> listParameter(List<Integer> ids) {
        System.out.println("执行了listParameter方法：" + ids);
        return ids;
    }
}
```