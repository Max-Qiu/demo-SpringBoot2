> 参考教程

- [MySQL事务详解](https://maxqiu.com/article/detail/86)
- [Spring Boot事务配置](https://blog.csdn.net/rickiyeat/article/details/62042685)
- [13-SpringBoot之数据库(四)——事务处理：隔离级别与传播行为](https://blog.csdn.net/huangjun0210/article/details/84202333)

# 简介

`SpringBoot` 环境使用事务非常简单，在需要事务的 **类** 或者 **方法** 上面添加 `@Transactional()` 注解即可；注解里面可以进行一些配置，常用配置如下：

- `isolation`：事务隔离级别
- `propagation`：事务传播类型
- `rollbackFor`：定义零个或多个异常类，这些异常类必须是`Throwable`的子类，指示哪些异常类型必须导致事务回滚。

## 隔离级别

即数据库的事务隔离级别，有如下取值：

- `Isolation.DEFAULT`：默认隔离级别
    - `MySQL`：默认隔离级别是 **读已提交**
    - `Oracle`：默认隔离级别是 **可重复读**
- `Isolation.READ_UNCOMMITTED`：读未提交
- `Isolation.READ_COMMITTED`：读已提交
- `Isolation.REPEATABLE_READ`：可重复读
- `Isolation.SERIALIZABLE`：串行化

## 传播行为

- `Propagation.REQUIRED`：**需要事务**，它是默认传播行为。如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个事务。
- `Propagation.SUPPORTS`：**支持事务**。如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
- `Propagation.MANDATORY`：**必须使用事务**。如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
- `Propagation.REQUIRES_NEW`：**创建新事务**。创建一个新的事务；如果当前存在事务，则把当前事务挂起。
- `Propagation.NOT_SUPPORTED`：**不支持事务**。以非事务方式运行；如果当前存在事务，则把当前事务挂起。
- `Propagation.NEVER`：**不支持事务**。以非事务方式运行；如果当前存在事务，则抛出异常。
- `Propagation.NESTED`：**嵌套事务**。如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于 REQUIRED 。

## 异常处理

默认情况下，事务将在`RuntimeException`和`Error`上回滚，但不会在业务异常上回滚，所以需要使用`rollbackFor`指定当抛出哪些业务异常时也回滚。