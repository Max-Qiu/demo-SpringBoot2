> 官方文档：[Spring Data Redis](https://spring.io/projects/spring-data-redis)

# 简介

之前一篇文章[SpringBoot2.6.x缓存介绍以及整合Redis](https://maxqiu.com/article/detail/91)仅介绍了如何使用Redis作为缓存。

如果想要直接操作`Redis`，`SpringBoot`提供了`RedisTemplate`类用来直接操作数据

# 配置

## POM依赖

`spring-boot-starter-data-redis`使用的Redis客户端已经从`jedis`更改为`lettuce`

```xml
<!-- Redis -->
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
    <version>2.0.7</version>
</dependency>
<dependency>
    <groupId>com.alibaba.fastjson2</groupId>
    <artifactId>fastjson2-extension</artifactId>
    <version>2.0.7</version>
</dependency>
```

## yml配置文件

根据Redis服务的模式不同，对应的配置文件也不同

### 单机模式

```yml
spring:
  redis:
    ## 单机模式
    host: 192.168.220.101 # 地址
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

### 哨兵模式（主从复制）

```yml
spring:
  redis:
    ## 哨兵模式（主从复制）
    sentinel:
      master: master # 哨兵的sentinel.conf配置文件中的主节点名称
      password: 123 # 哨兵的密码
      nodes: 192.168.220.101:26379,192.168.220.102:26379,192.168.220.103:26379 # 哨兵节点

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

### 集群模式

```yml
spring:
  redis:
    ## 集群模式
    cluster:
      nodes: 192.168.220.101:6379,192.168.220.102:6379,192.168.220.103:6379,192.168.220.101:6380,192.168.220.102:6380,192.168.220.103:6380

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
      cluster:
        refresh:
          period: 1000 # 集群模式！需要设置群集拓扑刷新周期（毫秒）
```

## RedisConfig配置文件自定义`RedisTemplate`

默认情况下，`org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration`会生成一个默认的`RedisTemplate`，且序列化时使用`JdkSerializationRedisSerializer`，详见`org.springframework.data.redis.core.RedisTemplate`

然而`Jdk`的序列化在Redis中可读性不好，我们需要自定义`Json`格式的序列化转换，这里我们使用阿里巴巴的`Fastjson`

`RedisAutoConfiguration`同时还提供了一个`StringRedisTemplate`，详见`org.springframework.data.redis.core.StringRedisTemplate`

```java
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson2.support.spring.data.redis.GenericFastJsonRedisSerializer;

/**
 * Redis操作配置
 *
 * @author Max_Qiu
 */
@Configuration
public class RedisTemplateConfig {
    /**
     * 自定义RedisTemplate，使用fastjson格式化value
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        // key序列化
        template.setKeySerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
        // value序列化
        template.setValueSerializer(new GenericFastJsonRedisSerializer());
        // hash序列化
        template.setHashKeySerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
        template.setHashValueSerializer(new GenericFastJsonRedisSerializer());
        // 启用事务支持
        template.setEnableTransactionSupport(true);
        return template;
    }
}
```

# 使用

一般情况下，有如下两个常用对象

```
/**
 * RedisTemplate操作通用对象
 */
@Autowired
private RedisTemplate<String, Object> redisTemplate;

/**
 * 操作String类型的对象
 */
@Autowired
private StringRedisTemplate stringRedisTemplate;
```

而`RedisTemplate`又可以通过`opsForXxx`生成对应的类型的操作对象，例如

```java
// RedisTemplate 可以获取对应数据类型的 XxxOperations
ValueOperations<String, Object> objectValueOperations = redisTemplate.opsForValue();
ListOperations<String, Object> objectListOperations = redisTemplate.opsForList();
SetOperations<String, Object> objectSetOperations = redisTemplate.opsForSet();
HashOperations<String, String, Object> objectHashOperations = redisTemplate.opsForHash();
ZSetOperations<String, Object> objectZSetOperations = redisTemplate.opsForZSet();
```

简单点，若后面要获取`XxxOperations`可以直接使用`@Resource`注解获取，且获取时可以将`Object`对象根据值类型换成具体的对象。注意，值是`String`类型时`name`需要使用`stringRedisTemplate`

```java
/**
 * 值是String类型的字符串
 *
 * 自定义的RedisTemplate在格式化String时会多出引号
 *
 * 需要使用Spring内置的StringRedisTemplate
 */
@Resource(name = "stringRedisTemplate")
private ValueOperations<String, String> stringValueOperations;

/**
 * 值是对象类型的字符串
 *
 * 使用自定义的RedisTemplate，将值格式化成JSON
 */
@Resource(name = "redisTemplate")
private ValueOperations<String, User> userValueOperations;

/**
 * 值是数字类型的字符串
 *
 * 自定义的RedisTemplate可以格式化Integer
 */
@Resource(name = "redisTemplate")
private ValueOperations<String, Integer> integerValueOperations;
```

## `RedisTemplate` 基础操作

```java
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * Keys 键相关
 */
@SpringBootTest
public class BaseOperationsTest {
    /**
     * RedisTemplate操作通用对象
     */
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 操作String类型的对象
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void test() {
        // RedisTemplate 可以获取对应数据类型的 XxxOperations
        ValueOperations<String, Object> objectValueOperations = redisTemplate.opsForValue();
        ListOperations<String, Object> objectListOperations = redisTemplate.opsForList();
        SetOperations<String, Object> objectSetOperations = redisTemplate.opsForSet();
        HashOperations<String, String, Object> objectHashOperations = redisTemplate.opsForHash();
        ZSetOperations<String, Object> objectZSetOperations = redisTemplate.opsForZSet();

        // 准备数据
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        stringValueOperations.set("key1", "hello1");
        stringValueOperations.set("key2", "hello2");
        stringValueOperations.set("key3", "hello3");
        stringValueOperations.set("key4", "hello4");

        // KEYS 查找键
        Set<String> keys = redisTemplate.keys("*");
        assertNotNull(keys);
        assertEquals(4, keys.size());

        // EXISTS 键是否存在
        Boolean hasKey = redisTemplate.hasKey("key1");
        assertTrue(hasKey);
        Long existingKeys = redisTemplate.countExistingKeys(Arrays.asList("key1", "key2"));
        assertEquals(2, existingKeys);

        // TYPE 查看键的类型
        assertEquals(DataType.STRING, redisTemplate.type("key1"));

        // DEL 删除键
        Boolean deleteFalse = redisTemplate.delete("key");
        assertFalse(deleteFalse);
        Boolean deleteSuccess = redisTemplate.delete("key1");
        assertTrue(deleteSuccess);
        Long batchDelete = redisTemplate.delete(Arrays.asList("key", "key2"));
        assertEquals(1, batchDelete);

        // UNLINK 删除键（异步）
        Boolean unlink = redisTemplate.unlink("key3");
        assertTrue(unlink);
        Long batchUnlink = redisTemplate.unlink(Arrays.asList("key1", "key2"));
        assertEquals(0, batchUnlink);

        Long expire = redisTemplate.getExpire("key4");
        // TTL 查看键剩余生成秒
        assertEquals(-1, expire);

        // EXPIRE 设置键到期秒
        redisTemplate.expire("key4", 10, TimeUnit.SECONDS);
        redisTemplate.expire("key4", Duration.ofSeconds(10));
        assertEquals(10, redisTemplate.getExpire("key4"));

        // PERSIST key 设置键永不过期
        redisTemplate.persist("key4");
        assertEquals(-1, redisTemplate.getExpire("key4"));

        // 删除数据
        assertTrue(redisTemplate.delete("key4"));
    }
}
```

## `ValueOperations` String 字符串

```java
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.maxqiu.demo.entity.User;

/**
 * String 字符串
 */
@SpringBootTest
public class StringOperationsTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 值是String类型的字符串
     *
     * 自定义的RedisTemplate在格式化String时会多出引号
     *
     * 需要使用Spring内置的StringRedisTemplate
     */
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> stringValueOperations;

    /**
     * 值是对象类型的字符串
     *
     * 使用自定义的RedisTemplate，将值格式化成JSON
     */
    @Resource(name = "redisTemplate")
    private ValueOperations<String, User> userValueOperations;

    /**
     * 值是数字类型的字符串
     *
     * 自定义的RedisTemplate可以格式化Integer
     */
    @Resource(name = "redisTemplate")
    private ValueOperations<String, Integer> integerValueOperations;

    /**
     * GET
     */
    @Test
    @Order(1)
    void get() {
        // 获取不存在的键
        assertNull(stringValueOperations.get("nonexistent"));

        // 获取String类型的数据
        stringValueOperations.set("key", "hello");
        assertEquals("hello", stringValueOperations.get("key"));

        // 获取对象类型的数据
        User user = new User(1, "tom", new BigDecimal(18));
        userValueOperations.set("user", user);
        assertEquals(user, userValueOperations.get("user"));

        integerValueOperations.set("num", 1);
        assertEquals(1, integerValueOperations.get("num"));

        redisTemplate.delete(Arrays.asList("user", "key", "num"));
    }

    /**
     * SET
     * 
     * SETEX
     * 
     * SETNX
     * 
     * GETSET
     */
    @Test
    @Order(2)
    void set() {
        /// SET 设置键与值
        // 简单设置值
        stringValueOperations.set("key1", "hello");
        assertEquals("hello", stringValueOperations.get("key1"));

        // 如果键存在才设置值
        Boolean flag1 = stringValueOperations.setIfPresent("key1", "world");
        assertTrue(flag1);
        assertEquals("world", stringValueOperations.get("key1"));
        Boolean flag2 = stringValueOperations.setIfPresent("key2", "world");
        assertFalse(flag2);
        assertNull(stringValueOperations.get("key2"));

        // 同上，并设置超时时间
        Boolean flag3 = stringValueOperations.setIfPresent("key1", "hello", Duration.ofSeconds(10));
        assertTrue(flag3);
        // stringOperations.setIfPresent("key2", "world", 10, TimeUnit.SECONDS);
        assertEquals(10, redisTemplate.getExpire("key1"));

        // SETEX 设置键与值并设置超时时间
        stringValueOperations.set("key2", "world", Duration.ofSeconds(10));
        // stringOperations.set("key2", "world", 10, TimeUnit.SECONDS);
        assertEquals(10, redisTemplate.getExpire("key2"));

        // SETNX 键不存在则设置键与值
        Boolean flag4 = stringValueOperations.setIfAbsent("key3", "hello");
        assertTrue(flag4);
        assertEquals("hello", stringValueOperations.get("key3"));

        // SET ... NX 键不存在则设置键与值（同时设置时间）
        Boolean flag5 = stringValueOperations.setIfAbsent("key4", "world", Duration.ofSeconds(10));
        // stringOperations.setIfAbsent("key4", "world", 10, TimeUnit.SECONDS);
        assertTrue(flag5);
        assertEquals("world", stringValueOperations.get("key4"));

        // GETSET 获取值并设置一个新值
        String s = stringValueOperations.getAndSet("key4", "redis");
        assertEquals(s, "world");
        assertEquals("redis", stringValueOperations.get("key4"));

        assertEquals(4, redisTemplate.delete(Arrays.asList("key1", "key2", "key3", "key4")));
    }

    @Test
    @Order(3)
    void other() {
        // APPEND 拼接字符串
        Integer append1 = stringValueOperations.append("key", "hello");
        assertEquals(5, append1);
        Integer append2 = stringValueOperations.append("key", " world");
        assertEquals(11, append2);

        // STRLEN 获取字符串长度
        Long size = stringValueOperations.size("key");
        assertEquals(11, size);

        // GETRANGE 截取字符串
        String a1 = stringValueOperations.get("key", 0, 3);
        assertEquals("hell", a1);
        String a2 = stringValueOperations.get("key", -3, -1);
        assertEquals("rld", a2);
        String a3 = stringValueOperations.get("key", 0, -1);
        assertEquals("hello world", a3);
        String a4 = stringValueOperations.get("key", 20, 100);
        assertEquals("", a4);

        // SETRANGE 修改字符串
        stringValueOperations.set("key", "redis", 6);
        assertEquals("hello redis", stringValueOperations.get("key"));

        // MSET 批量设置值
        stringValueOperations.multiSet(Map.of("key1", "v1", "key2", "v2"));
        assertTrue(redisTemplate.hasKey("key1"));

        // MGET 批量获取值
        List<String> list = stringValueOperations.multiGet(Arrays.asList("key1", "key2"));
        assertNotNull(list);
        assertEquals(2, list.size());

        // MSETNX 批量设置值（仅当键不存在）
        Boolean flag = stringValueOperations.multiSetIfAbsent(Map.of("key1", "v1", "key3", "v3"));
        assertFalse(flag);

        integerValueOperations.set("num", 1);

        // INCR 加一
        Long num1 = integerValueOperations.increment("num");
        assertEquals(2, num1);
        // DECR 减一
        Long num2 = integerValueOperations.decrement("num");
        assertEquals(1, num2);

        // INCRBY 加N
        Long num3 = integerValueOperations.increment("num", 15);
        assertEquals(16, num3);
        // DECRBY 减N
        Long num4 = integerValueOperations.decrement("num", 6);
        assertEquals(10, num4);

        redisTemplate.delete(Arrays.asList("key", "key1", "key2", "num"));
    }
}
```

## `ListOperations` List 列表

```java
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.maxqiu.demo.entity.User;

/**
 * List 列表
 */
@SpringBootTest
public class ListOperationsTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 值是String类型的列表
     * 
     * 自定义的RedisTemplate在格式化String时会多出引号
     * 
     * 需要使用Spring内置的StringRedisTemplate
     */
    @Resource(name = "stringRedisTemplate")
    private ListOperations<String, String> stringListOperations;

    /**
     * 值是对象类型的列表
     * 
     * 使用自定义的RedisTemplate，将值格式化成JSON
     */
    @Resource(name = "redisTemplate")
    private ListOperations<String, User> userListOperations;

    /**
     * 值是数字类型的列表
     *
     * 自定义的RedisTemplate可以格式化Integer
     */
    @Resource(name = "redisTemplate")
    private ListOperations<String, Integer> integerListOperations;

    /**
     * LRANGE 按指定范围查看列表的值
     */
    @Test
    @Order(1)
    void range() {
        // 插入元素
        stringListOperations.rightPushAll("list", "a", "b", "c", "d");

        List<String> list = stringListOperations.range("list", 0, -1);
        assertNotNull(list);
        assertEquals(4, list.size());
        assertEquals("a", list.get(0));

        redisTemplate.delete("list");
    }

    /**
     * RPUSH 在列表右侧插值
     * 
     * RPUSHX 仅key存在时在列表右侧插值
     * 
     * LPUSH 在列表左侧插值
     * 
     * LPUSHX 仅key存在时在列表左侧插值
     */
    @Test
    @Order(2)
    void push() {
        // RPUSH 在列表右侧插入单个元素
        stringListOperations.rightPush("list1", "a");
        assertEquals(1, stringListOperations.size("list1"));
        // RPUSH 在列表右侧插入多个元素
        stringListOperations.rightPushAll("list1", "b", "c", "d");
        // stringListOperations.rightPushAll("list1", Arrays.asList("b", "c", "d"));
        assertEquals(4, stringListOperations.size("list1"));

        // RPUSHX 仅key存在时在列表右侧插值
        stringListOperations.rightPushIfPresent("list1", "e");
        assertEquals(5, stringListOperations.size("list1"));

        // LPUSH 在列表左侧插入单个元素
        stringListOperations.leftPush("list2", "a");
        assertEquals(1, stringListOperations.size("list2"));
        // LPUSH 在列表左侧插入多个元素
        stringListOperations.leftPushAll("list2", "b", "c");
        // stringListOperations.leftPushAll("list2", Arrays.asList("d", "e"));
        assertEquals(3, stringListOperations.size("list2"));

        // LPUSHX 仅key存在时在列表左侧插值
        stringListOperations.leftPushIfPresent("list2", "d");
        assertEquals(4, stringListOperations.size("list2"));

        // LINSERT 插入元素
        // 在指定元素左侧插入
        stringListOperations.leftPush("list1", "b", "hello");
        assertEquals("hello", stringListOperations.index("list1", 1));
        // 在指定元素右侧插入
        stringListOperations.rightPush("list2", "c", "world");
        assertEquals("world", stringListOperations.index("list2", 2));

        // 插入的类型需要相同，对应类型使用对应的Operations
        // 例如：int类型
        integerListOperations.rightPushAll("nums", 1, 2, 3);
        assertEquals(3, integerListOperations.size("nums"));

        // 例如：User类型
        User user = new User(1, "tom", new BigDecimal("165.3"));
        userListOperations.rightPush("users", user);
        User user2 = userListOperations.rightPop("users");
        assertEquals(user, user2);

        // 清空数据
        redisTemplate.delete(Arrays.asList("list1", "list2", "nums", "users"));
    }

    /**
     * RPOP 从列表末尾获取元素并删除
     * 
     * LPOP 从列表头部获取元素并删除
     */
    @Test
    @Order(3)
    void pop() {
        stringListOperations.rightPushAll("list", "a", "b", "c", "d");

        // RPOP 从列表末尾获取元素并删除
        String s1 = stringListOperations.rightPop("list");
        assertEquals("d", s1);

        // LPOP 从列表头部获取元素并删除
        String s2 = stringListOperations.leftPop("list");
        assertEquals("a", s2);

        // 清空数据
        redisTemplate.delete("list");
    }

    @Test
    @Order(4)
    void other() {
        Long size1 = stringListOperations.rightPushAll("list", "a", "b", "c", "d", "e");

        // LLEN 列表长度
        Long size2 = stringListOperations.size("list");
        assertEquals(size1, size2);

        // LINDEX 获取指定索引的值
        String first = stringListOperations.index("list", 0);
        assertEquals("a", first);

        // LSET 修改指定索引的值
        stringListOperations.set("list", 2, "world");
        assertEquals("world", stringListOperations.index("list", 2));

        // LREM 删除指定元素
        Long remove = stringListOperations.remove("list", 1, "b");
        assertEquals(1, remove);

        // LTRIM 裁剪列表
        stringListOperations.trim("list", 1, 2);
        assertEquals(2, stringListOperations.size("list"));

        // RPOPLPUSH 从source列表右边吐出一个值，插到destination列表左边
        String move = stringListOperations.rightPopAndLeftPush("list", "list2");
        assertEquals("d", move);
        assertEquals(1, stringListOperations.size("list"));
        assertEquals(1, stringListOperations.size("list2"));

        // 清空数据
        redisTemplate.delete(Arrays.asList("list", "list2"));
    }
}
```

## `SetOperations` Set 集合

```java
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import com.maxqiu.demo.entity.User;

/**
 * Set 集合
 */
@SpringBootTest
public class SetOperationsTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 值是String类型的集合
     *
     * 自定义的RedisTemplate在格式化String时会多出引号
     *
     * 需要使用Spring内置的StringRedisTemplate
     */
    @Resource(name = "stringRedisTemplate")
    private SetOperations<String, String> stringSetOperations;

    /**
     * 值是对象类型的集合
     *
     * 使用自定义的RedisTemplate，将值格式化成JSON
     */
    @Resource(name = "redisTemplate")
    private SetOperations<String, User> userSetOperations;

    /**
     * 值是数字类型的集合
     *
     * 自定义的RedisTemplate可以格式化Integer
     */
    @Resource(name = "redisTemplate")
    private SetOperations<String, Integer> integerSetOperations;

    /**
     * 测试基本操作
     */
    @Test
    @Order(1)
    void normal() {
        // SADD 添加元素到集合中
        Long add = stringSetOperations.add("set", "a", "b", "c", "d", "e");
        assertEquals(5, add);

        // SCARD 集合大小
        Long size = stringSetOperations.size("set");
        assertEquals(add, size);

        // SMEMBERS 列出集合中的元素
        Set<String> set = stringSetOperations.members("set");
        assertNotNull(set);
        assertEquals(5, set.size());

        // SISMEMBER 存在指定的元素
        Boolean member = stringSetOperations.isMember("set", "a");
        assertTrue(member);

        // SREM 删除指定元素
        Long remove = stringSetOperations.remove("set", "a");
        assertEquals(1, remove);

        // SMOVE 移动指定元素
        Boolean move = stringSetOperations.move("set", "b", "set2");
        assertTrue(move);

        // SRANDMEMBER 随机取出一个或多个元素
        // 取出一个元素
        String set1 = stringSetOperations.randomMember("set");
        System.out.println(set1);
        // 取出多个元素且不重复
        Set<String> set3 = stringSetOperations.distinctRandomMembers("set", 2);
        assertEquals(2, set3.size());
        // 取出多个元素但可能有重复
        List<String> list = stringSetOperations.randomMembers("set", 2);
        assertEquals(2, list.size());

        // SPOP 随机取出一个或多个元素并删除
        String set2 = stringSetOperations.pop("set");
        System.out.println(set2);
        List<String> list2 = stringSetOperations.pop("set", 2);
        assertNotNull(list2);
        assertEquals(2, list2.size());

        // 删除数据
        assertFalse(redisTemplate.hasKey("set"));
        redisTemplate.delete("set2");
    }

    /**
     * 测试不同类型
     */
    @Test
    @Order(2)
    void type() {
        // 对象类型
        Long add = userSetOperations.add("users", new User(1, "tom", new BigDecimal(185)),
            new User(2, "tom", new BigDecimal(183)));
        Long size = userSetOperations.size("users");
        assertEquals(add, size);

        // 数字类型
        Long nums = integerSetOperations.add("nums", 1, 2, 3, 3);
        assertEquals(3, nums);

        // 删除数据
        redisTemplate.delete(Arrays.asList("users", "nums"));
    }

    /**
     * SINTER 交集
     * 
     * SUNION 并集
     * 
     * SDIFF 差集
     */
    @Test
    @Order(3)
    void aggregate() {
        // 准备数据
        stringSetOperations.add("set1", "a", "b", "c");
        stringSetOperations.add("set2", "a", "d", "f");

        // SINTER 交集
        Set<String> intersect = stringSetOperations.intersect("set1", "set2");
        // stringSetOperations.intersect(Arrays.asList("set1", "set2"));
        // stringSetOperations.intersect("set1", Arrays.asList("set2"));
        assertEquals(1, intersect.size());
        assertTrue(intersect.contains("a"));

        // SINTERSTORE 计算交集并存入目标集合
        Long intersectAndStore = stringSetOperations.intersectAndStore("set1", "set2", "set3");
        assertEquals(1, intersectAndStore);

        // SUNION 并集
        Set<String> union = stringSetOperations.union("set1", "set2");
        assertEquals(5, union.size());

        // SUNIONSTORE 计算并集并存入目标集合
        Long unionAndStore = stringSetOperations.unionAndStore("set1", "set2", "set4");
        assertEquals(5, unionAndStore);

        // SDIFF 差集
        Set<String> difference = stringSetOperations.difference("set1", "set2");
        assertEquals(2, difference.size());
        assertTrue(difference.contains("b"));

        // SDIFFSTORE 计算差集并存入目标集合
        Long differenceAndStore = stringSetOperations.differenceAndStore("set1", "set2", "set5");
        assertEquals(2, differenceAndStore);

        // 删除数据
        redisTemplate.delete(Arrays.asList("set1", "set2", "set3", "set4", "set5"));
    }
}
```

## `HashOperations` Hash 哈希散列

```java
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Hash 哈希散列
 */
@SpringBootTest
public class HashOperationsTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "stringRedisTemplate")
    private HashOperations<String, String, String> stringHashOperations;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Integer> integerHashOperations;

    /**
     * HSET 给哈希散列添加字段和值
     * 
     * HSETNX 给哈希散列添加字段和值（仅字段不存在）
     */
    @Test
    @Order(1)
    void set() {
        // HSET 给哈希散列添加字段和值
        // 单个
        stringHashOperations.put("user", "id", "1");
        stringHashOperations.put("user", "name", "tom");
        // 多个
        stringHashOperations.putAll("user", Map.of("email", "tom@126.com", "age", "18"));

        // HSETNX 给哈希散列添加字段和值（仅字段不存在）
        Boolean flag1 = stringHashOperations.putIfAbsent("user", "address", "jiangsu");
        assertTrue(flag1);
        Boolean flag2 = stringHashOperations.putIfAbsent("user", "address", "jiangsu");
        assertFalse(flag2);

        // 删除数据
        redisTemplate.delete("user");
    }

    /**
     * HGET 获取哈希散列的指定字段的值
     * 
     * HMGET 获取哈希散列的多个字段的值
     * 
     * HGETALL 获取哈希散列的所有字段与值
     * 
     * HKEYS 获取哈希散列的所有字段
     * 
     * HVALS 获取哈希散列的所有值
     */
    @Test
    @Order(2)
    void get() {
        // 准备数据
        stringHashOperations.putAll("user", Map.of("id", "1", "name", "tom", "email", "tom@126.com", "age", "18"));

        // HGET 获取哈希散列的指定字段的值
        String name = stringHashOperations.get("user", "name");
        assertEquals("tom", name);

        // HMGET 获取哈希散列的多个字段的值
        List<String> list = stringHashOperations.multiGet("user", Arrays.asList("name", "age", "email", "address"));
        for (String s : list) {
            System.out.println(s);
        }

        // HGETALL 获取哈希散列的所有字段与值
        Map<String, String> map = stringHashOperations.entries("user");
        for (String s : map.keySet()) {
            System.out.println(s + "  " + map.get(s));
        }

        // HKEYS 获取哈希散列的所有字段
        Set<String> keys = stringHashOperations.keys("user");
        for (String key : keys) {
            System.out.println(key);
        }

        // HVALS 获取哈希散列的所有值
        List<String> values = stringHashOperations.values("user");
        for (String value : values) {
            System.out.println(value);
        }

        // 删除数据
        redisTemplate.delete("user");
    }

    @Test
    @Order(3)
    void other() {
        // 准备数据
        stringHashOperations.putAll("user", Map.of("id", "1", "name", "tom", "email", "tom@126.com", "age", "18"));

        // HEXISTS 哈希散列是否存在指定字段
        Boolean hasName = stringHashOperations.hasKey("user", "name");
        assertTrue(hasName);

        // HDEL 删除哈希散列的指定字段
        Long delete = stringHashOperations.delete("user", "id", "email");
        assertEquals(2, delete);

        // HLEN 哈希散列的长度
        Long size = stringHashOperations.size("user");
        assertEquals(2, size);

        // HSTRLEN 哈希散列的字段的长度
        Long length = stringHashOperations.lengthOfValue("user", "name");
        assertEquals(3, length);

        // HINCRBY 增减哈希散列中指定字段的值
        Long increment = integerHashOperations.increment("user", "age", 1);
        assertEquals(19, increment);

        // 删除数据
        redisTemplate.delete("user");
    }
}
```

## `ZSetOperations` Zset 有序集合（Sorted Sets）

```java
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * ZSet 有序集合（Sorted Sets）
 */
@SpringBootTest
public class ZSetOperationsTest {
    @Autowired
    private RedisOperations<String, Object> redisOperations;

    /**
     * 值是String类型的有序集合
     *
     * 自定义的RedisTemplate在格式化String时会多出引号
     *
     * 需要使用Spring内置的StringRedisTemplate
     */
    @Resource(name = "stringRedisTemplate")
    private ZSetOperations<String, String> stringZSetOperations;

    /**
     * ZADD 将一个或多个member及source添加到有序集合中
     */
    @Test
    @Order(1)
    void add() {
        Boolean flag = stringZSetOperations.add("zset", "maths", 99);
        assertTrue(flag);

        Set<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
        set.add(new DefaultTypedTuple<>("chinese", 90.0));
        set.add(new DefaultTypedTuple<>("english", 60.0));
        Long add = stringZSetOperations.add("zset", set);
        assertEquals(2, add);

        // 删除数据
        redisOperations.delete("zset");
    }

    /**
     * 根据指定规则排序取出
     */
    @Test
    @Order(2)
    void get() {
        // 准备数据
        Set<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
        set.add(new DefaultTypedTuple<>("chinese", 115.0));
        set.add(new DefaultTypedTuple<>("maths", 135.5));
        set.add(new DefaultTypedTuple<>("english", 110.5));
        set.add(new DefaultTypedTuple<>("physics", 88.0));
        set.add(new DefaultTypedTuple<>("chemistry", 65.0));
        stringZSetOperations.add("zset", set);

        /// 正序
        // ZRANGE 返回排序集合中的指定范围的元素（正序，从小到大）
        Set<String> zset = stringZSetOperations.range("zset", 0, -1);
        System.out.println(zset);

        // ZRANGEBYSCORE 返回指定分数内的元素（正序，从小到大）
        Set<String> zset3 = stringZSetOperations.rangeByScore("zset", 10, 100);
        System.out.println(zset3);

        // ZRANGEBYSCORE 返回指定分数内的元素（正序，从小到大）（LIMIT 分页）
        Set<String> zset5 = stringZSetOperations.rangeByScore("zset", 0, 200, 1, 2);
        System.out.println(zset5);

        // ZRANGE xxxWithScores 返回时包含分数（正序，从小到大）其他同
        Set<ZSetOperations.TypedTuple<String>> zset2 = stringZSetOperations.rangeWithScores("zset", 0, -1);
        if (zset2 != null) {
            for (ZSetOperations.TypedTuple<String> stringTypedTuple : zset2) {
                System.out.println(stringTypedTuple.getValue() + " " + stringTypedTuple.getScore());
            }
        }

        /// 倒序
        // ZREVRANGE 返回排序集合中的指定范围的元素（倒序，从大到小）
        stringZSetOperations.reverseRange("zset", 0, -1);
        // ZREVRANGEBYSCORE 返回指定分数内的元素（倒序，从大到小）
        stringZSetOperations.reverseRangeByScore("zset", 10, 100);
        // ZREVRANGEBYSCORE 返回指定分数内的元素（倒序，从大到小）（LIMIT 分页）
        stringZSetOperations.reverseRangeByScore("zset", 0, 200, 1, 2);
        // ZREVRANGE xxxWithScores 返回时包含分数（倒序，从大到小）其他同
        stringZSetOperations.reverseRangeWithScores("zset", 0, -1);

        // 删除数据
        redisOperations.delete("zset");
    }

    @Test
    @Order(3)
    void other() {
        // 准备数据
        Set<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
        set.add(new DefaultTypedTuple<>("chinese", 115.0));
        set.add(new DefaultTypedTuple<>("maths", 135.5));
        set.add(new DefaultTypedTuple<>("english", 110.5));
        set.add(new DefaultTypedTuple<>("physics", 88.0));
        set.add(new DefaultTypedTuple<>("chemistry", 65.0));
        stringZSetOperations.add("zset", set);

        // ZSCORE 返回排序集中元素的分数
        Double score = stringZSetOperations.score("zset", "chinese");
        assertEquals(115.0, score);

        // 查询元素在有序集合中的排名
        // ZRANK
        Long rank = stringZSetOperations.rank("zset", "chinese");
        assertEquals(3, rank);
        // ZREVRANK
        Long reverseRank = stringZSetOperations.reverseRank("zset", "chinese");
        assertEquals(1, reverseRank);

        // ZCARD key 返回排序集合的元素数量
        Long size = stringZSetOperations.size("zset");
        assertEquals(5, size);

        // ZCOUNT 统计指定范围内的元素数量
        Long count = stringZSetOperations.count("zset", 0, 100);
        assertEquals(2, count);

        // ZINCRBY 给指定元素增减分数
        Double incrementScore = stringZSetOperations.incrementScore("zset", "english", 18.5);
        assertEquals(129.0, incrementScore);

        // ZREM 删除元素
        Long remove = stringZSetOperations.remove("zset", "chinese", "english");
        assertEquals(2, remove);

        // 删除数据
        redisOperations.delete("zset");
    }
}
```

## `Transactions` 事务

```java
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;

/**
 * 事务
 */
@SpringBootTest
public class TransactionsTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 值是数字类型的字符串
     *
     * 自定义的RedisTemplate可以格式化Integer
     */
    @Resource(name = "redisTemplate")
    private ValueOperations<String, Integer> valueOperations;

    @Test
    void test() {
        valueOperations.set("a", 100);
        valueOperations.set("b", 100);
        // 乐观锁
        // redisOperations.watch("a");
        List<Long> execute = redisTemplate.execute(new SessionCallback<>() {
            @Override
            public List<Long> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                ValueOperations<String, Long> valueOperations = operations.opsForValue();
                valueOperations.increment("a", 10);
                valueOperations.decrement("b", 10);
                return operations.exec();
            }
        });
        Assertions.assertEquals(110, execute.get(0));
        Assertions.assertEquals(90, execute.get(1));

        // 删除数据
        redisTemplate.delete(Arrays.asList("a", "b"));
    }
}
```
