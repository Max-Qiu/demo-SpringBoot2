> 官方文档：[Druid Spring Boot Starter](https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter)

吐槽一下，官方文档写的真乱

# 快速使用

> 第一步：添加依赖

```xml
<!-- 数据库驱动略 -->
<!-- 数据库ORM框架略 -->
<!-- Druid 数据库连接池 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.2.6</version>
</dependency>
```

> 第二步：添加配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3307/druid?useSSL=false&serverTimezone=GMT%2B8 # 连接地址
    username: root # 用户名
    password: 123 # 密码
```

> 第三步：启动

查看启动日志，有如下输出：

`2021-04-21 23:00:33.719  INFO 37564 --- [  restartedMain] c.a.d.s.b.a.DruidDataSourceAutoConfigure : Init DruidDataSource`

# 详细配置

`Druid`的所有配置均在`spring.datasource.druid`下面，在`IDEA`中编辑`SpringBoot`配置文件均有提示。

## 必要配置

```yaml
spring:
  datasource:
    druid:
      # 连接池核心配置（使用datasource进行配置）
      #driver-class-name: # 驱动 使用 spring.datasource.driver-class-name（可以省略，会自动根据url适配）
      #url: # 连接地址 使用 spring.datasource.url
      #username: # 用户名 使用 spring.datasource.username
      #password: # 密码 使用 spring.datasource.password
```

该配置建议直接使用`spring.datasource`进行配置

## 配置状态查看页面

> 官方文档：[配置_StatViewServlet配置](https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_StatViewServlet%E9%85%8D%E7%BD%AE)

```yaml
spring:
  datasource:
    druid:
      # 配置统计页面
      stat-view-servlet:
        enabled: true # 是否开启（默认 false ）
        url-pattern: /druid/* # 统计页面访问路径（默认）
        # 注：生产环境记得打开用户名和密码，并设置复杂密码
        #login-username: admin # 用户名
        #login-password: admin # 密码
        reset-enable: true # 是否允许重置统计数据（默认 true ）
        allow: "" # 允许任意IP访问（默认 127.0.0.1 ）
        #eny: 192.168.1.1 # 禁止访问的IP（优先级高于allow）
```

配置后打开`http://ip:port/druid/`即可访问状态查看页面。如下图：`首页`展示基本信息，如Java版本，加载的jar包

![](https://cdn.maxqiu.com/upload/f33d8463ddc5467c892b2ea4e69895b2.jpg)

- SQL监控：当开启SQL监控时，所有执行过的SQL均会在此页面显示
- SQL防火墙：当开启防火墙时，显示防御统计数据
- Web应用：
- URI监控：
- Session监控：
- Spring监控：

## 核心配置

> 官方文档：
[DruidDataSource配置](https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE)
[DruidDataSource配置属性列表](https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8)

```yaml
spring:
  datasource:
    druid:
      # 连接池基础配置
      initial-size: 5 # 连接池初始大小
      min-idle: 10 # 最小连接数
      max-active: 20 # 最大连接数
      validation-query: select 1 # 连接检查语句
      test-while-idle: true # 是否在连接空闲一段时间后检测其可用性（默认true）
      test-on-borrow: false # 是否在获得连接后检测其可用性（默认false）
      test-on-return: false # 是否在连接放回连接池后检测其可用性（默认false）
      min-evictable-idle-time-millis: 60000 # 一个连接在池中最小生存的时间（单位：毫秒）（默认1800000）
      max-evictable-idle-time-millis: 300000 # 一个连接在池中最大生存的时间（单位：毫秒）（默认25200000）
      keep-alive: true # 保持连接存在（默认false）
      async-init: true # 异步初始化（默认false）当initial-size数量较多时，打开会加快应用启动
      ## 更多其他配置不太重要，略
```

修改之后重新启动应用，打开`状态查看`并切换至`数据源`，可以查看到上述修改（其中，带`*`的属性均可以在配置文件中进行修改）

## 配置Filter

> 官方文档：
[如何配置 Filter](https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter#%E5%A6%82%E4%BD%95%E9%85%8D%E7%BD%AE-filter)
[内置Filter的别名](https://github.com/alibaba/druid/wiki/%E5%86%85%E7%BD%AEFilter%E7%9A%84%E5%88%AB%E5%90%8D)

### 批量配置默认Filter

```yaml
spring:
  datasource:
    druid:
      # 批量配置默认的Filter
      filters: "stat,wall,slf4j"
```

这种方式可以启用相应的内置`Filter`，不过这些Filter都是默认配置。具体可配置的默认`Filter`可以查看`druid-1.2.6.jar!\META-INF\druid-filter.properties`。如下：

```properties
druid.filters.default=com.alibaba.druid.filter.stat.StatFilter
druid.filters.stat=com.alibaba.druid.filter.stat.StatFilter
druid.filters.mergeStat=com.alibaba.druid.filter.stat.MergeStatFilter
druid.filters.counter=com.alibaba.druid.filter.stat.StatFilter
druid.filters.encoding=com.alibaba.druid.filter.encoding.EncodingConvertFilter
druid.filters.log4j=com.alibaba.druid.filter.logging.Log4jFilter
druid.filters.log4j2=com.alibaba.druid.filter.logging.Log4j2Filter
druid.filters.slf4j=com.alibaba.druid.filter.logging.Slf4jLogFilter
druid.filters.commonlogging=com.alibaba.druid.filter.logging.CommonsLogFilter
druid.filters.commonLogging=com.alibaba.druid.filter.logging.CommonsLogFilter
druid.filters.wall=com.alibaba.druid.wall.WallFilter
druid.filters.config=com.alibaba.druid.filter.config.ConfigFilter
druid.filters.haRandomValidator=com.alibaba.druid.pool.ha.selector.RandomDataSourceValidateFilter
```

### 手动配置Filter

#### 配置SQL监控

> 官方文档：[配置_StatFilter](https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_StatFilter)

```yaml
spring:
  datasource:
    druid:
      # 手动配置Filter
      filter:
        # 配置SQL监控
        stat:
          enabled: true # 是否启用（默认关闭，若在filters启用，此处自动开启且不可关闭）
          db-type: mysql # 数据库类型
          merge-sql: true # SQL合并：将不同的where条件的SQL合并为一个统计（默认false）
          log-slow-sql: true # 是否记录慢查询SQL
          slow-sql-millis: 3000 # 慢查询时间（单位：毫秒）（默认）
```

修改之后重新启动应用，打开`状态查看`并切换至`SQL监控`，再随便访问项目的其他路径并保证有SQL被执行，之后可以查看到刚刚执行的SQL，点击任意一条SQL可以查看详细信息

SQL监控项上，执行时间、读取行数、更新行数都有区间分布，将耗时分布成8个区间：

- 0 - 1 耗时0到1毫秒的次数
- 1 - 10 耗时1到10毫秒的次数
- 10 - 100 耗时10到100毫秒的次数
- 100 - 1,000 耗时100到1000毫秒的次数
- 1,000 - 10,000 耗时1到10秒的次数
- 10,000 - 100,000 耗时10到100秒的次数
- 100,000 - 1,000,000 耗时100到1000秒的次数
- 1,000,000 - 耗时1000秒以上的次数

#### 配置SQL防火墙

> 官方文档：[配置 wallfilter](https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE-wallfilter)

```yaml
spring:
  datasource:
    druid:
      # 手动配置Filter
      filter:
        # 配置SQL防火墙
        wall:
          enabled: true # 是否启用（默认关闭，若在filters启用，此处自动开启且不可关闭）
          db-type: mysql # 数据库类型
          # 刚开始引入WallFilter的时候，把logViolation设置为true，而throwException设置为false。就可以观察是否存在违规的情况，同时不影响业务运行。
          log-violation: true # 对被认为是攻击的SQL进行LOG.error输出（默认false）
          throw-exception: false # 对被认为是攻击的SQL抛出SQLException（默认true）
          #provider-white-list: # 手动配置SQL白名单
          config:
            truncate-allow: false # 是否允许truncate语句（默认true）
            drop-table-allow: false # 是否允许修改表（默认true）
            # 更多详细配置参照官方文档：https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE-wallfilter#wallconfig%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E
```

修改之后重新启动应用，打开`状态查看`并切换至`SQL防火墙`，再随便访问项目的其他路径并保证有SQL被执行，之后可以查看到防御统计

#### 配置日志

> 官方文档：[配置_LogFilter](https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_LogFilter)

```yaml
spring:
  datasource:
    druid:
      # 手动配置Filter
      filter:
        # 配置日志
        slf4j:
          enabled: true # 是否启用（默认关闭，若在filters启用，此处自动开启且不可关闭）
          # 大部分默认配置均为true，无需额外配置，详细见：com.alibaba.druid.filter.logging.LogFilter
```

通过查看`com.alibaba.druid.filter.logging.Slf4jLogFilter`的父类`com.alibaba.druid.filter.logging.LogFilter`发现，基本上默认日志都为`true`，所以配置文件中无需修改，默认即可

## 配置Web监控

> 官方文档：[配置_配置WebStatFilter](https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_%E9%85%8D%E7%BD%AEWebStatFilter)

```yaml
spring:
  datasource:
    druid:
      # 配置Web应用、URI监控、Session监控
      web-stat-filter:
        enabled: true # 开启（默认 false）
        url-pattern: /* # 监控的路径（默认）
        exclusions: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.woff2,/druid/*" # 排除监控的路径或文件类型
        session-stat-enable: true # 启用Session监控（默认）
        session-stat-max-count: 1000 # 最大统计的session数量
        profile-enable: true # 监控单个url调用的sql列表
        #principal-session-name: user.username # 用session中的某个值标记作Principal当前SESSIONID
        #principal-cookie-name: nickname # 用cookie中的某个值标记作Principal当前SESSIONID
```

修改之后重新启动应用，打开`状态查看`并切换至`SQL防火墙`，再随便访问项目的其他路径，之后可以查看到`Web应用`、`URI监控`、`Session监控`。

- Web应用：可以查看到请求数、Jdbc执行数等数据
- URI监控：可以看到不同URI的请求统计，点击每个URI，可以查看到该URI调用的SQL
- Session监控：可以看到容器中有哪些Session。（仅代码中调用了session并存/取值才会有记录）

## 配置Spring监控

> 官方文档：[配置_Druid和Spring关联监控配置](https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_Druid%E5%92%8CSpring%E5%85%B3%E8%81%94%E7%9B%91%E6%8E%A7%E9%85%8D%E7%BD%AE)

步骤一：添加监控点

```yaml
spring:
  datasource:
    druid:
      # 配置Spring监控AOP切入点（多个用英文逗号分割）
      aop-patterns: com.maxqiu.demo.*
```

步骤二：POM添加AOP依赖

```
<!-- AOP（Druid监控Spring时需要依赖） -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

修改之后重新启动应用，打开`状态查看`并切换至`Spring监控`，再随便访问项目的其他路径，之后可以查看到`SpringAOP`监控

# 完整配置示例

推荐将`Druid`配置文件独立出来，之后在主配置文件中引入

> application.yml

```yaml
spring:
  profiles:
    include: druid # 加入 Druid 配置文件
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver # 驱动（可以省略，会自动根据url适配）
    url: jdbc:mysql://127.0.0.1:3307/druid?useSSL=false&serverTimezone=GMT%2B8 # 连接地址
    username: root # 用户名
    password: 123 # 密码
logging:
  file:
    name: ./log/log.log
```

> application-druid.yml

```yaml
# 将 Druid 配置文件独立出来，之后在application.yml添加如下代码加入当前配置，方便复用

#spring:
#  profiles:
#    include: druid

spring:
  datasource:
    druid:
      # 连接池核心配置（使用datasource进行配置）
      #driver-class-name: # 驱动 使用 spring.datasource.driver-class-name（可以省略，会自动根据url适配）
      #url: # 连接地址 使用 spring.datasource.url
      #username: # 用户名 使用 spring.datasource.username
      #password: # 密码 使用 spring.datasource.password

      # 配置统计页面
      stat-view-servlet:
        enabled: true # 是否开启（默认 false ）
        url-pattern: /druid/* # 统计页面访问路径（默认）
        # 注：生产环境记得打开用户名和密码的配置，并设置复杂密码
        #login-username: admin # 用户名
        #login-password: admin # 密码
        reset-enable: true # 是否允许重置统计数据（默认 true ）
        allow: "" # 允许任意IP访问（默认 127.0.0.1 ）
        #eny: 192.168.1.1 # 禁止访问的IP（优先级高于allow）

      # 连接池基础配置
      initial-size: 5 # 连接池初始大小
      min-idle: 10 # 最小连接数
      max-active: 20 # 最大连接数
      validation-query: select 1 # 连接检查语句
      test-while-idle: true # 是否在连接空闲一段时间后检测其可用性（默认true）
      test-on-borrow: false # 是否在获得连接后检测其可用性（默认false）
      test-on-return: false # 是否在连接放回连接池后检测其可用性（默认false）
      min-evictable-idle-time-millis: 60000 # 一个连接在池中最小生存的时间（单位：毫秒）（默认1800000）
      max-evictable-idle-time-millis: 300000 # 一个连接在池中最大生存的时间（单位：毫秒）（默认25200000）
      keep-alive: true # 保持连接存在（默认false）
      async-init: true # 异步初始化（默认false）当initial-size数量较多时，打开会加快应用启动
      ## 更多其他配置不太重要，略

      # 批量配置默认的Filter
      #filters: "stat,wall,slf4j"

      # 手动配置Filter
      filter:

        # 配置SQL监控
        stat:
          enabled: true # 是否启用（默认关闭，若在filters启用，此处自动开启且不可关闭）
          db-type: mysql # 数据库类型
          merge-sql: true # SQL合并：将不同的where条件的SQL合并为一个统计（默认false）
          log-slow-sql: true # 是否记录慢查询SQL
          slow-sql-millis: 3000 # 慢查询时间（单位：毫秒）（默认）

        # 配置SQL防火墙
        wall:
          enabled: true # 是否启用（默认关闭，若在filters启用，此处自动开启且不可关闭）
          db-type: mysql # 数据库类型
          # 刚开始引入WallFilter的时候，把logViolation设置为true，而throwException设置为false。就可以观察是否存在违规的情况，同时不影响业务运行。
          log-violation: true # 对被认为是攻击的SQL进行LOG.error输出（默认false）
          throw-exception: false # 对被认为是攻击的SQL抛出SQLException（默认true）
          #provider-white-list: # 手动配置SQL白名单
          config:
            truncate-allow: false # 是否允许truncate语句（默认true）
            drop-table-allow: false # 是否允许修改表（默认true）
            # 更多详细配置参照官方文档：https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE-wallfilter#wallconfig%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E

        # 配置日志
        slf4j:
          enabled: true # 是否启用（默认关闭，若在filters启用，此处自动开启且不可关闭）
          # 大部分默认配置均为true，无需额外配置，详细见：com.alibaba.druid.filter.logging.LogFilter

      # 配置Web应用、URI监控、Session监控
      web-stat-filter:
        enabled: true # 开启（默认 false）
        url-pattern: /* # 监控的路径（默认）
        exclusions: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.woff2,/druid/*" # 排除监控的路径或文件类型
        session-stat-enable: true # 启用Session监控（默认）
        session-stat-max-count: 1000 # 最大统计的session数量
        profile-enable: true # 监控单个url调用的sql列表
        #principal-session-name: user # 用session中的某个值标记作Principal当前SESSIONID
        #principal-cookie-name: nickname # 用cookie中的某个值标记作Principal当前SESSIONID

      # 配置Spring监控AOP切入点（多个用英文逗号分割）
      aop-patterns: com.maxqiu.demo.*
```
