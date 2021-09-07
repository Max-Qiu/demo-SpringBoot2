> 官方示例代码：
GitHub：[https://github.com/baomidou/mybatis-plus-samples](https://github.com/baomidou/mybatis-plus-samples)
Gitee：[https://gitee.com/baomidou/mybatis-plus-samples](https://gitee.com/baomidou/mybatis-plus-samples)

# pom 依赖

```xml
<!-- SpringBoot相关其他依赖略 -->
<!-- Lombok 如果实体使用Lombok，则需要添加 -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
<!-- String工具类 BlockAttackInnerInterceptor拦截器需要用 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
</dependency>
<!-- MySQL驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
<!-- MybatisPlus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.3.3</version>
</dependency>
<!-- JSON 格式化 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.75</version>
</dependency>
<!-- 性能分析 -->
<dependency>
    <groupId>p6spy</groupId>
    <artifactId>p6spy</artifactId>
    <version>3.9.1</version>
</dependency>
```

# yml

> 仅展示常用示例

```yaml
spring:
  datasource:
    #    driver-class-name: com.mysql.cj.jdbc.Driver # 驱动
    #    url: jdbc:mysql://127.0.0.1:3306/smp?useSSL=false&serverTimezone=GMT%2B8&characterEncoding=utf8 # 连接地址
    driver-class-name: com.p6spy.engine.spy.P6SpyDriver # 使用性能分析器（开发时使用）
    url: jdbc:p6spy:mysql://127.0.0.1:3306/smp?useSSL=false&serverTimezone=GMT%2B8&characterEncoding=utf8 # 使用 性能分析器的 连接地址（开发时使用）
    username: root # 用户名
    password: 123 #密码
mybatis-plus:
  mapper-locations: classpath*:/com/maxqiu/demo/mapper/xml/**.xml # xml路径
  global-config:
    db-config:
      id-type: ASSIGN_ID # 全局默认主键策略，默认为雪花ID，若表中设置了自增，则生成的实体自动添加自增ID属性，参考 TestDelete
      logic-delete-field: deleted # 全局逻辑删除的实体字段名，若不配置，则不启用
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
  configuration:
    map-underscore-to-camel-case: true # 驼峰转下划线（默认）
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 日志输出
  type-enums-package: com.maxqiu.demo.enums # 配置扫描通用枚举
```

# 代码示例

## CRUD 增删改查

- TestUserMapper DAO层
- TestUserService Service层
- TestUser Entity层（AR模式）

## 条件构造器

- TestWrappers 条件构造器创建方法
- TestLambdaQueryWrapper 查询条件构造器
- TestLambdaUpdateWrapper 更新条件构造器

## 其他

- 性能分析（仅需要修改配置）
- TestCustomUserMapper 自定义Mapper
- TestPage 分页详细示例
- TestGroupByAndHaving 分组
- TestJoinQuery 多对多关联查询
- TestLogicDelete 逻辑删除
- TestOptimisticLocker 乐观锁
- TestOther
  - testOperationAllTable 防全表操作
  - testAutoFill 自动填充
  - testEnum 枚举字段

> 以下暂无示例，可参考官方示例

- Sequence针对Oracle和SqlServer （懒得折腾Oracle）
- 多数据源 （暂不涉及）
- 动态表名 （看懂了，但是没做过分表）
- 类型处理器 （看懂了，不知道用在什么场景）
- 自定义全局操作 （没看懂）
- 多租户 （没看懂）
- 分离打包 （没看懂）
