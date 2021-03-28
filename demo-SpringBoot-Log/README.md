> 本文档整理自视频教程：[尚硅谷_SpringBoot](http://www.atguigu.com/download_detail.shtml?v=37)

环境介绍：本文使用`SpringBoot 2.4.4`，视频教程使用的`1.5.x`版本

> 示例代码：
GitHub：[https://github.com/Max-Qiu/demo/tree/main/demo-SpringBoot-Log](https://github.com/Max-Qiu/demo/tree/main/demo-SpringBoot-Log)
Gitee：[https://gitee.com/Max-Qiu/demo/tree/main/demo-SpringBoot-Log](https://gitee.com/Max-Qiu/demo/tree/main/demo-SpringBoot-Log)


# 常用日志框架

日志门面 | 日志实现
--- | ---
JCL(Jakarta  Commons Logging)<br>SLF4J(Simple  Logging Facade for Java)<br>jboss-logging | Log4j<br>JUL(java.util.logging)<br>Log4j2<br>Logback

> 怎么选？

跟着大厂（SpringBoot）走！

- 日志门面：SLF4J
- 日志实现：Logback

# SLF4J使用

> 官方地址：[http://www.slf4j.org/](http://www.slf4j.org/)

在开发时，使用日志记录方法的调用，不应该直接调用日志的实现类，而是调用日志抽象层里面的方法

> 示例

给系统里面导入`slf4j`和`logback`的依赖

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.25</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
</dependency>
```

使用时如下

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Demo {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Demo.class);
        logger.info("Hello World");
    }
}
```

或者再添加`Lombok`依赖

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.18</version>
</dependency>
```

使用如下

```java
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Demo {
    public static void main(String[] args) {
        log.info("Hello World");
    }
}
```

# SpringBoot中使用SLF4J

## 默认使用

`SpringBoot`默认配置好了日志，且使用`Logback`作为默认日志实现框架，`SpringBoot`也可以切换至其他框架（不做介绍，意义不大）

> 简单示例如下：

```java
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void contextLoads() {
        logger.trace("这是trace日志...");
        logger.debug("这是debug日志...");
        logger.info("这是info日志...");
        logger.warn("这是warn日志...");
        logger.error("这是error日志...");
    }
}
```

> 当使用`Lombok`时，可以直接使用注解

```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class ApplicationTests2 {
    @Test
    public void contextLoads() {
        log.trace("这是trace日志...");
        log.debug("这是debug日志...");
        log.info("这是info日志...");
        log.warn("这是warn日志...");
        log.error("这是error日志...");
    }
}
```

## 修改配置

### 通过`application.yml`修改

#### 日志级别

日志框架拥有的日志级别有5个，从低到高分别为：

- TRACE
- DEBUG
- INFO
- WARN
- ERROR

SpringBoot还可以额外设置两个日志级别：

- FATAL
- OFF

通过修改`logging.level`进行设置日志级别，该属性接收一个`map`

> 示例如下：

```yaml
logging:
  level:
    root: info # 修改默认的日志级别
    com.maxqiu.demo: warn # 设置某个包下面的日志级别
```

#### 日志文件位置以及日志文件名

logging.file.path | logging.file.name | 示例  | 描述
--- | --- | --- | ---
*null* | *null* | | 只在控制台输出
指定路径 | *null* | ./log   | 输出到项目所在路径的log文件夹下
*null* | 日志文件名和路径 | ./log/test.log | 输出到项目所在路径的log文件夹下，文件名为test.log 文件中

PS：均指定时，只有`logging.file.name`生效

> 示例如下：

```yaml
logging:
  file:
    path: ./log # 日志文件所在位置（path会被name覆盖）
    name: ./log/test.log # 日志文件名以及路径（默认项目根路径，文件名默认为 spring.log）
```

#### 自定义格式输出格式

> 示例如下：

```yaml
logging:
  pattern:
    console: '%d{yyyy-MM-dd} -- [%thread] -- %-5level -- %logger{50} -- %msg%n' # 控制台输出格式
    file: '%d{yyyy-MM-dd} == [%thread] == %-5level == %logger{50} == %msg%n' # 文件输出格式
```

> 具体含义：

- `%d`：表示日期时间，
- `%thread`：表示线程名，
- `%-5level`：级别从左显示5个字符宽度
- `%logger{50}`：表示logger名字最长50个字符，否则按照句点分割。 
- `%msg`：日志消息，
- `%n`：是换行符

### 通过日志框架原生配置文件进行配置

`logback`的默认配置文件在`spring-boot-2.4.4.jar`包下的`org.springframework.boot.logging.logback`包下的`base.xml`以及引入的其他`xml`。同理，`log4j2`、`JUL`等默认的配置文件也在对应的包下面

如果在类路径下放置各个日志框架默认的配置文件，则`SpringBoot`就不使用默认的配置文件，且`application.yml`的配置会被无效化

> 框架与对应配置文件关系如下：

Logging System | Customization
--- | ---
Logback | `logback-spring.xml`<br>`logback-spring.groovy`<br>`logback.xml`<br>`logback.groovy`
Log4j2 | `log4j2-spring.xml`<br>`log4j2.xml`
JDK (Java Util Logging) | `logging.properties`

PS：以`Logback`为例，若使用`logback.xml`，则直接就被日志框架识别了；若使用`logback-spring.xml`，则日志框架就不直接加载日志的配置项，由`SpringBoot`解析日志配置，可以使用`SpringBoot`的高级`Profile`功能

#### `SpringBoot`的`Profile`功能

```xml
<springProfile name="staging">
    <!-- 可以指定某段配置只在某个环境下生效 -->
</springProfile>
```

> 举例：

```xml
<appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
    <layout class="ch.qos.logback.classic.PatternLayout">
        <springProfile name="dev">
            <!-- 开发环境使用该格式输出 -->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} ---> [%thread] ---> %-5level %logger{50} - %msg%n</pattern>
        </springProfile>
        <springProfile name="!dev">
            <!-- 非开发环境使用其他格式输出 -->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} ==== [%thread] ==== %-5level %logger{50} - %msg%n</pattern>
        </springProfile>
    </layout>
</appender>
```

#### `logback-spring.xml`完整示例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--
scan：当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。
scanPeriod：设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒当scan为true时，此属性生效。默认的时间间隔为1分钟。
debug：当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。
-->
<configuration scan="false" scanPeriod="60 seconds" debug="false">
    <!-- 定义日志的根目录 -->
    <property name="LOG_HOME" value="./log"/>
    <!-- 定义日志文件名称 -->
    <property name="appName" value="springboot"/>
    <!-- ch.qos.logback.core.ConsoleAppender 表示控制台输出 -->
    <appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
        <!--
        日志输出格式：
            %d表示日期时间，
            %thread表示线程名，
            %-5level：级别从左显示5个字符宽度
            %logger{50} 表示logger名字最长50个字符，否则按照句点分割。 
            %msg：日志消息，
            %n是换行符
        -->
        <layout class="ch.qos.logback.classic.PatternLayout">
            <springProfile name="dev">
                <!-- 开发环境使用该格式输出 -->
                <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} ----> [%thread] ---> %-5level %logger{50} - %msg%n</pattern>
            </springProfile>
            <springProfile name="!dev">
                <!-- 非开发环境使用其他格式输出 -->
                <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} ==== [%thread] ==== %-5level %logger{50} - %msg%n</pattern>
            </springProfile>
        </layout>
    </appender>

    <!-- 滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件 -->
    <appender name="appLogAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 指定日志文件的名称 -->
        <file>${LOG_HOME}/${appName}.log</file>
        <!--
        当发生滚动时，决定 RollingFileAppender 的行为，涉及文件移动和重命名
        TimeBasedRollingPolicy： 最常用的滚动策略，它根据时间来制定滚动策略，既负责滚动也负责出发滚动。
        -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--
            滚动时产生的文件的存放位置及文件名称 %d{yyyy-MM-dd}：按天进行日志滚动 
            %i：当文件大小超过maxFileSize时，按照i进行文件滚动
            -->
            <fileNamePattern>${LOG_HOME}/${appName}-%d{yyyy-MM-dd}-%i.log</fileNamePattern>
            <!--
            可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件。假设设置每天滚动，
            且maxHistory是365，则只保存最近365天的文件，删除之前的旧文件。注意，删除旧文件是，
            那些为了归档而创建的目录也会被删除。
            -->
            <MaxHistory>365</MaxHistory>
            <!--
            当日志文件超过maxFileSize指定的大小是，根据上面提到的%i进行日志文件滚动 注意此处配置SizeBasedTriggeringPolicy是无法实现按文件大小进行滚动的，必须配置timeBasedFileNamingAndTriggeringPolicy
            -->
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <!-- 日志输出格式： -->
        <layout class="ch.qos.logback.classic.PatternLayout">
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [ %thread ] - [ %-5level ] [ %logger{50} : %line ] - %msg%n</pattern>
        </layout>
    </appender>

    <!--
        logger主要用于存放日志对象，也可以定义日志类型、级别
        name：表示匹配的logger类型前缀，也就是包的前半部分
        level：要记录的日志级别，包括 TRACE < DEBUG < INFO < WARN < ERROR
        additivity：作用在于children-logger是否使用 rootLogger配置的appender进行输出，
        false：表示只用当前logger的appender-ref，true：
        表示当前logger的appender-ref和rootLogger的appender-ref都有效
    -->
    <!-- hibernate logger -->
    <logger name="com.atguigu" level="debug"/>
    <!-- Spring framework logger -->
    <logger name="org.springframework" level="debug" additivity="false"/>


    <!--
    root与logger是父子关系，没有特别定义则默认为root，任何一个类只会和一个logger对应，
    要么是定义的logger，要么是root，判断的关键在于找到这个logger，然后判断这个logger的appender和level。 
    -->
    <root level="info">
        <appender-ref ref="stdout"/>
        <appender-ref ref="appLogAppender"/>
    </root>
</configuration>
```
