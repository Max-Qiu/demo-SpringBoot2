> 官方文档：

- [Spring Framework Documentation -- Integration -- 6. Email](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#mail)
- [Spring Boot Reference Documentation -- IO -- 4. Sending Email](https://docs.spring.io/spring-boot/docs/current/reference/html/io.html#io.email)

环境介绍：本文使用`SpringBoot 2.6.x`

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
    host: smtp.xxx.com # 邮箱服务器
    username: xxx@xxx.com # 用户名
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

## Util 服务示例

```java
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件 工具
 *
 * @author Max_Qiu
 */
@Component
public class EmailUtil {
    /**
     * 邮件发送
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发件人
     */
    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private TemplateEngine engine;

    /**
     * 发送简单邮件
     *
     * @param toList
     *            接收人列表
     * @param ccList
     *            抄送人列表
     * @param bccList
     *            密送人列表
     * @param subject
     *            主题
     * @param text
     *            内容
     */
    public boolean simpleMailMessage(List<String> toList, List<String> ccList, List<String> bccList, String subject, String text) {
        // 1. 创建一个简单消息
        SimpleMailMessage message = new SimpleMailMessage();
        // 2. 设置
        // 发件人
        message.setFrom(from);
        // 收件人
        if (toList == null || toList.size() == 0) {
            return false;
        } else if (toList.size() == 1) {
            message.setTo(toList.get(0));
        } else {
            String[] strings = new String[toList.size()];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = toList.get(i);
            }
            message.setTo(strings);
        }
        // 抄送
        if (ccList != null) {
            if (ccList.size() == 1) {
                message.setCc(ccList.get(0));
            } else if (ccList.size() != 0) {
                String[] strings = new String[ccList.size()];
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = ccList.get(i);
                }
                message.setCc(strings);
            }
        }
        // 密送
        if (bccList != null) {
            if (bccList.size() == 1) {
                message.setCc(bccList.get(0));
            } else if (bccList.size() != 0) {
                String[] strings = new String[bccList.size()];
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = bccList.get(i);
                }
                message.setBcc(strings);
            }
        }
        // 主体
        message.setSubject(subject);
        // 内容
        message.setText(text);
        // 3. 发送
        try {
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 发送简单邮件
     *
     * @param toList
     *            接收人列表
     * @param subject
     *            主题
     * @param text
     *            内容
     */
    public boolean simpleMailMessage(List<String> toList, String subject, String text) {
        return simpleMailMessage(toList, null, null, subject, text);
    }

    /**
     * 发送简单邮件
     *
     * @param to
     *            接收人
     * @param subject
     *            主题
     * @param text
     *            内容
     */
    public boolean simpleMailMessage(String to, String subject, String text) {
        return simpleMailMessage(Collections.singletonList(to), subject, text);
    }

    /**
     * HTML内附件实体
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Inline {
        /**
         * 内容内的ID
         */
        private String id;
        /**
         * 文件路径
         */
        private String filePath;
    }

    /**
     * 邮件附件实体
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attachment {
        /**
         * 自定义名称
         */
        private String fileName;
        /**
         * 文件路径
         */
        private String filePath;
    }

    /**
     * 复杂邮件
     *
     * @param toList
     *            接收人列表
     * @param ccList
     *            抄送人列表
     * @param bccList
     *            密送人列表
     * @param subject
     *            主题
     * @param text
     *            内容
     * @param textIsHtml
     *            内容是否为HTML
     * @param inlineList
     *            HTML内的附件
     * @param attachmentList
     *            邮件的附件
     */
    public boolean mimeMessage(List<String> toList, List<String> ccList, List<String> bccList, String subject, String text, boolean textIsHtml,
        List<Inline> inlineList, List<Attachment> attachmentList) {
        // 1. 创建一个复杂的消息邮件
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            // 2. 设置
            helper.setFrom(from);
            // 收件人
            if (toList == null || toList.size() == 0) {
                return false;
            } else if (toList.size() == 1) {
                helper.setTo(toList.get(0));
            } else {
                String[] strings = new String[toList.size()];
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = toList.get(i);
                }
                helper.setTo(strings);
            }
            // 抄送
            if (ccList != null) {
                if (ccList.size() == 1) {
                    helper.setCc(ccList.get(0));
                } else if (ccList.size() != 0) {
                    String[] strings = new String[ccList.size()];
                    for (int i = 0; i < strings.length; i++) {
                        strings[i] = ccList.get(i);
                    }
                    helper.setCc(strings);
                }
            }
            // 密送
            if (bccList != null) {
                if (bccList.size() == 1) {
                    helper.setCc(bccList.get(0));
                } else if (bccList.size() != 0) {
                    String[] strings = new String[bccList.size()];
                    for (int i = 0; i < strings.length; i++) {
                        strings[i] = bccList.get(i);
                    }
                    helper.setBcc(strings);
                }
            }
            // 主体
            helper.setSubject(subject);
            // 内容
            helper.setText(text, textIsHtml);
            // 内部附件
            if (inlineList != null && inlineList.size() != 0) {
                for (Inline inline : inlineList) {
                    helper.addInline(inline.getId(), new File(inline.getFilePath()));
                }
            }
            // 外部附件
            if (attachmentList != null && attachmentList.size() != 0) {
                for (Attachment attachment : attachmentList) {
                    helper.addAttachment(attachment.getFileName(), new File(attachment.getFilePath()));
                }
            }
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 复杂邮件
     *
     * @param toList
     *            接收人列表
     * @param subject
     *            主题
     * @param text
     *            内容
     * @param textIsHtml
     *            内容是否为HTML
     * @param inlineList
     *            HTML内的附件
     * @param attachmentList
     *            邮件的附件
     */
    public boolean mimeMessage(List<String> toList, String subject, String text, boolean textIsHtml, List<Inline> inlineList,
        List<Attachment> attachmentList) {
        return mimeMessage(toList, null, null, subject, text, textIsHtml, inlineList, attachmentList);
    }

    /**
     * 复杂邮件
     *
     * @param to
     *            接收人
     * @param subject
     *            主题
     * @param text
     *            内容
     * @param textIsHtml
     *            内容是否为HTML
     * @param inlineList
     *            HTML内的附件
     * @param attachmentList
     *            邮件的附件
     */
    public boolean mimeMessage(String to, String subject, String text, boolean textIsHtml, List<Inline> inlineList, List<Attachment> attachmentList) {
        return mimeMessage(Collections.singletonList(to), subject, text, textIsHtml, inlineList, attachmentList);
    }

    /**
     * 模板邮件
     *
     * @param toList
     *            接收人列表
     * @param ccList
     *            抄送人列表
     * @param bccList
     *            密送人列表
     * @param subject
     *            主题
     * @param template
     *            模板
     * @param map
     *            模板中的内容
     * @param inlineList
     *            模板内的附件
     * @param attachmentList
     *            邮件的附件
     */
    public boolean templateMessage(List<String> toList, List<String> ccList, List<String> bccList, String subject, String template,
        Map<String, Object> map, List<Inline> inlineList, List<Attachment> attachmentList) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            // 收件人
            if (toList == null || toList.size() == 0) {
                return false;
            } else if (toList.size() == 1) {
                helper.setTo(toList.get(0));
            } else {
                String[] strings = new String[toList.size()];
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = toList.get(i);
                }
                helper.setTo(strings);
            }
            // 抄送
            if (ccList != null) {
                if (ccList.size() == 1) {
                    helper.setCc(ccList.get(0));
                } else if (ccList.size() != 0) {
                    String[] strings = new String[ccList.size()];
                    for (int i = 0; i < strings.length; i++) {
                        strings[i] = ccList.get(i);
                    }
                    helper.setCc(strings);
                }
            }
            // 密送
            if (bccList != null) {
                if (bccList.size() == 1) {
                    helper.setCc(bccList.get(0));
                } else if (bccList.size() != 0) {
                    String[] strings = new String[bccList.size()];
                    for (int i = 0; i < strings.length; i++) {
                        strings[i] = bccList.get(i);
                    }
                    helper.setBcc(strings);
                }
            }
            // 主体
            helper.setSubject(subject);
            Context context = new Context();
            context.setVariables(map);
            helper.setText(engine.process(template, context), true);
            // 内部附件
            if (inlineList != null && inlineList.size() != 0) {
                for (Inline inline : inlineList) {
                    helper.addInline(inline.getId(), new File(inline.getFilePath()));
                }
            }
            // 外部附件
            if (attachmentList != null && attachmentList.size() != 0) {
                for (Attachment attachment : attachmentList) {
                    helper.addAttachment(attachment.getFileName(), new File(attachment.getFilePath()));
                }
            }
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
```

# 常用邮箱服务地址

- `SMTP` → `发`
- `POP3/IMAP` → `收`

## 阿里

官方文档：[企业邮箱的POP3、SMTP、IMAP地址是什么？](http://mailhelp.mxhichina.com/smartmail/detail.vm?knoId=5871700)

> 新地址：

协议 | 服务器地址 | 服务器端口号（常规） | 服务器端口号（加密）
---|---|---|---
SMTP | smtp.qiye.aliyun.com | 25 | 465
IMAP | imap.qiye.aliyun.com | 143 | 993
POP3 | pop.qiye.aliyun.com | 110 | 995

> 旧地址

协议 | 服务器地址 | 服务器端口号（常规） | 服务器端口号（加密）
---|---|---|---
SMTP | smtp.mxhichina.com | 25 | 465
IMAP | imap.mxhichina.com | 143 | 993
POP3 | pop3.mxhichina.com | 110 | 995

## 腾讯

官方文档：[如何设置IMAP、POP3/SMTP及其SSL加密方式？](https://service.exmail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=1000585)

协议 | 服务器地址 | 服务器端口号（加密）
---|---|---
SMTP | smtp.exmail.qq.com | 465
IMAP | imap.exmail.qq.com | 993
POP3 | pop.exmail.qq.com | 995

## 126

官方文档：[126免费邮客户端设置POP3和SMTP地址](http://help.163.com/09/1221/09/5R20C4CM00753VB9.html?servCode=6020375)

协议 | 服务器地址 |  服务器端口号（常规） | 服务器端口号（加密）
---|---|---|---
SMTP | smtp.126.com | 25 | 465/994
IMAP | imap.126.com | 143 | 993
POP3 | pop.126.com | 110 | 995
