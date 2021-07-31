package com.maxqiu.demo.service;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 整合后的 EmailService 测试
 * 
 * @author Max_Qiu
 */
@SpringBootTest
class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    /**
     * 简单邮件测试
     */
    @Test
    void simpleMailMessage() {
        boolean b = emailService.simpleMailMessage("xxx@xxx.com", "主题", "内容");
        System.out.println("发送结果：" + b);
    }

    /**
     * 复杂邮件测试
     */
    @Test
    public void mimeMessage() {
        String text = "<b style='color:red'>今天 7:30 开会</b></br>地点：大会堂</br><img src='cid:image1'>";
        List<EmailService.Inline> inlineList =
            Collections.singletonList(new EmailService.Inline("image1", "C:\\file1.jpg"));
        List<EmailService.Attachment> attachmentList =
            Collections.singletonList(new EmailService.Attachment("customFilename.jpg", "C:\\file2.jpg"));
        boolean b = emailService.mimeMessage("xxx@xx.com", "主题", text, true, inlineList, attachmentList);
        System.out.println("发送结果：" + b);
    }
}