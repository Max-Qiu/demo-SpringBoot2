package com.maxqiu.demo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * JavaMailSender 示例
 */
@SpringBootTest
class ApplicationTests {
    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private TemplateEngine engine;

    /**
     * 简单邮件
     */
    @Test
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

    /**
     * 复杂邮件
     */
    @Test
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

    /**
     * 模板邮件
     */
    @Test
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
        helper.addInline("image1", new File("C:\\file1.jpg")); // 内部附件
        sender.send(mimeMessage);
    }
}
