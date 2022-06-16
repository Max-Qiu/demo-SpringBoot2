> 官方文档：[https://spring.io/projects/spring-session](https://spring.io/projects/spring-session)

# 简介

Spring Session提供了一个API和实现来管理用户的会话信息，同时也使支持集群会话变得简单，而无需绑定到应用程序容器特定的解决方案。

SpringSession集成了如下环境

- `HttpSession`：常用的SpringMVC模式，例如Tomcat。
- `WebSocket`：WebSocket
- `WebSession` ：Spring WebFlux模式

存储Session的数据源有如下方案

- `Reids`
- `JDBC`
- `MongoDB`
- `geode`

本文以`HttpSession`+`Reids`为例

# 配置

## Maven依赖

```xml
<!-- Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!-- SpringSession Redis 数据源 -->
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
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

## yml配置

主要配置一个`store-type`，Redis相关配置建议参考[SpringBoot2.4.x整合Redis](https://maxqiu.com/article/detail/102)

```yml
server:
  port: 8080
spring:
  session:
    store-type: redis # 使用 Redis 存储 Session
    #redis:
    #  flush-mode: immediate # 会话刷新模式（此处有bug，设置无效，需要在启动类注册上设置）
    #  namespace: demo::session # 用于存储会话的键的命名空间（此处有bug，设置无效，需要在启动类注册上设置）
  # Redis连接相关配置
  redis:
    host: redis # 地址
    port: 6379 # 端口
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

## Application配置

需要在启动类添加`@EnableRedisHttpSession`注解，相关设置也要在该注解内设置，`yml`内的配置无效，应该是bug

```java
@EnableRedisHttpSession(
    // 设置存储会话的键的命名空间
    redisNamespace = "demo::session",
    // 设置会话超时时间
    maxInactiveIntervalInSeconds = 600,
    // Redis会话的刷新模式。
    // ON_SAVE：（默认值）仅在调用SessionRepository.SAVE（Session）时更新备份Redis。在web环境中，发生在HTTP响应提交之前。
    // IMMEDIATE：会话的任何更新都会立即写入Redis实例
    flushMode = FlushMode.IMMEDIATE)
@SpringBootApplication
public class SpringSessionApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringSessionApplication.class, args);
    }
}
```

## Config配置

### Redis序列化设置

注意：该序列化仅针对`Session`的序列化，与`RedisTemplate`的设置不通用

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.alibaba.fastjson2.support.spring.data.redis.GenericFastJsonRedisSerializer;

/**
 * Redis共享会话的序列化设置
 *
 * @author Max_Qiu
 */
@Configuration
public class RedisSessionConfig {
    /**
     * 使用 fastjson 序列号
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericFastJsonRedisSerializer();
    }
}
```

### 浏览器Cookie设置

```java
/**
 * SpringSession的Cookie配置
 */
@Configuration
public class RedisCookieConfig {
    /**
     * 设置Cookie序列化的配置
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        // Session的key，默认：SESSION
        serializer.setCookieName("SESSION");
        // Session的value是否进行Base64编码，推荐关闭，方便浏览器内查看到value值
        serializer.setUseBase64Encoding(false);
        // Session的路径
        serializer.setCookiePath("/");
        // Session的可读域名（默认当前域名）若需要多域名共享Cookie，则需要设置为主域名
        // serializer.setDomainName("demo.com");
        return serializer;
    }
}
```

推荐阅读：

- [cookie设置域名问题，cookie跨域](https://blog.csdn.net/czhphp/article/details/65628977)
- [java之Cookie详解](https://www.cnblogs.com/z941030/p/4742188.html)

# 案例

## Session读写示例

```
/**
 * Session读写
 *
 * @author Max_Qiu
 */
@RestController
public class IndexController {
    @Value("${server.port}")
    private String port;

    @RequestMapping("get")
    public String get(HttpSession session) {
        String name = (String)session.getAttribute("name");
        return "port:" + port + "\tname:" + (name != null ? name : "null");
    }

    @RequestMapping("set")
    public String set(HttpSession session) {
        session.setAttribute("name", "max");
        return "ok";
    }
}
```

## 多服务示例（集群和微服务）

1. 启动服务，再使用`--server.port=8081`启动第二个服务
2. 本地Nginx配置如下，并启动Nginx服务
```
    upstream demo{
        server 127.0.0.1:8080;
        server 127.0.0.1:8081;
    }

    server {
        listen       80;
        location / {
            proxy_pass http://demo/;
            add_header Strict-Transport-Security "max-age=31536000";
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header Host $http_host;
            proxy_set_header HTTP_X_FORWARDED_FOR $remote_addr;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_redirect default;
        }
    }
```
3. 访问`http://127.0.0.1/set`写Session
4. 访问`http://127.0.0.1/get`读Session，并且多次访问可以看到不同服务均可以读取Session中的值
