在`Spring Boot 2.x`版本中，默认数据库池技术已从`Tomcat Pool`切换到`HikariCP`

所以，只要引入`jdbc`、`jpa`等框架就会默认引入`HikariCP`

# POM

```xml
<!-- web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!-- 数据库连接驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
<!-- 数据库ORM框架 也可以使用 jpa、MyBatis 等 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>
```

# yml

```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver # 驱动（可以省略，会自动根据url适配）
    url: jdbc:mysql://127.0.0.1:3306/hikari?useSSL=false&serverTimezone=GMT%2B8 # 连接地址
    username: root # 用户名
    password: 123 # 密码
    type: com.zaxxer.hikari.HikariDataSource # 指定数据库连接池（默认会检查到此连接池）
    hikari:
      # 连接池常用配置
      auto-commit: true # 自动提交（默认true）
      connection-timeout: 30000 # 连接超时时间（单位：毫秒）（默认值：30秒）
      idle-timeout: 600000 # 连接在池中闲置的最长时间（单位：毫秒）（默认值：10分钟）
      max-lifetime: 1200000 # 池中连接的最长生命周期（单位：毫秒）（默认值：30分钟）
      maximum-pool-size: 10 # 池达到的最大大小，包括空闲和使用中的连接（默认值：10）
      minimum-idle: 5 # 池中维护的最小空闲连接数（默认值：与maximum-pool-size相同）
```

# 启动

启动时，就可以看到相关日志

```
2021-09-23 20:40:24.723  INFO 26124 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2021-09-23 20:40:24.815  INFO 26124 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
```
