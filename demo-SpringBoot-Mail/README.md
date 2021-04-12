> 官方文档：
[Spring Framework Documentation -- Integration -- 6. Email](https://docs.spring.io/spring-framework/docs/5.3.5/reference/html/integration.html#mail)


环境介绍：本文使用`SpringBoot 2.4.4`

> 示例代码：
GitHub：[https://github.com/Max-Qiu/demo/tree/main/demo-SpringBoot-Mail](https://github.com/Max-Qiu/demo/tree/main/demo-SpringBoot-Mail)
Gitee：[https://gitee.com/Max-Qiu/demo/tree/main/demo-SpringBoot-Mail](https://gitee.com/Max-Qiu/demo/tree/main/demo-SpringBoot-Mail)

# 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

# 配置

```yml
spring:
  mail:
    host: smtp.126.com # 邮箱服务器
    username: username@maxqiu.com # 用户名
    password: password # 密码
    port: 465 # 端口
    default-encoding: utf-8 # 编码
    properties:
      mail:
        smtp.ssl.enable: true # 开启 SSL 通道
        mime.splitlongparameters: false # 解决邮件发送附件不正常的问题
```

# 使用

## 简单邮件

```java
public void simpleMailMessage() {
    // 1. 创建一个简单消息
    SimpleMailMessage message = new SimpleMailMessage();
    // 2. 设置
    message.setFrom(from);// 发件人
    message.setTo("test@maxqiu.com"); // 收件人
    // message.setCc(""); // 抄送
    // message.setBcc(""); // 密送
    message.setSubject("开会通知！"); // 主体
    message.setText("今晚7:30开会\n地点：人民大会堂"); // 内容
    // 3. 发送
    sender.send(message);
}
```

## 复杂邮件

```java
public void mimeMessage() throws Exception {
    // 1. 创建一个复杂的消息邮件
    MimeMessage mimeMessage = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
    // 2. 设置
    helper.setFrom(from);
    helper.setTo("test@maxqiu.com");
    helper.setSubject("开会通知！");
    // helper.setCc(""); // 抄送
    // helper.setBcc(""); // 密送
    helper.setText("<b style='color:red'>今天 7:30 开会</b></br>地点：大会堂</br><img src='cid:image1'>", true); // 支持 HTML
    helper.addInline("image1", new File("C:\\file1.jpg")); // 内部附件
    helper.addAttachment("customFilename.jpg", new File("C:\\file2.jpg")); // 外部附件（可自定义名称）
    // 3. 发送
    sender.send(mimeMessage);
}
```

## 模板邮件

以`thymeleaf`为例，也支持`freemaker`等模板引擎

> 第一步：添加依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

> 第二步：修改配置

```yml
spring:
  thymeleaf:
    enabled: true # 开启模板引擎
    encoding: UTF-8 # 设置字符编码
    prefix: classpath:/templates/ # 设置文件路径
    cache: true # 启用缓存（本地测试修改为false）
    suffix: .html # 文件后缀
```

> 第三步：新建模板

新建`template.html`文件，放在`resources/templates`目录下

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>你好</title>
</head>
<body>
<b style='color:red'>今天 <span th:text="${time}">时间</span> 开会</b><br>
地点：<span th:text="${location}">地点</span>
</body>
</html>
```

> 第四步：发送邮件

```java
public void sendTemplateEmail() throws Exception {
    MimeMessage mimeMessage = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
    helper.setFrom(from);
    helper.setTo("test@maxqiu.com");
    // helper.setCc(""); // 抄送
    // helper.setBcc(""); // 密送
    helper.setSubject("开会通知！");
    Map<String, Object> map = new HashMap<>();
    map.put("time", "7:30");
    map.put("location", "大会堂");
    Context context = new Context();
    context.setVariables(map);
    String text = engine.process("template", context);
    helper.setText(text, true);
    sender.send(mimeMessage);
}
```
