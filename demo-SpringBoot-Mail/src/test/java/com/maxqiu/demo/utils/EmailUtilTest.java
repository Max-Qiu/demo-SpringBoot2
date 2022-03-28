package com.maxqiu.demo.utils;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 整合后的 EmailUtil 测试
 *
 * @author Max_Qiu
 */
@SpringBootTest
class EmailUtilTest {
    @Autowired
    private EmailUtil emailUtil;

    /**
     * 简单邮件测试
     */
    @Test
    void simpleMailMessage() {
        boolean b = emailUtil.simpleMailMessage("xxx@xxx.com", "主题", "内容");
        System.out.println("发送结果：" + b);
    }

    /**
     * 复杂邮件测试
     */
    @Test
    public void mimeMessage() {
        String text = "<b style='color:red'>今天 7:30 开会</b></br>地点：大会堂</br><img src='cid:image1'>";
        List<EmailUtil.Inline> inlineList = Collections.singletonList(new EmailUtil.Inline("image1", "C:\\file1.jpg"));
        List<EmailUtil.Attachment> attachmentList = Collections.singletonList(new EmailUtil.Attachment("customFilename.jpg", "C:\\file2.jpg"));
        boolean b = emailUtil.mimeMessage("xxx@xx.com", "主题", text, true, inlineList, attachmentList);
        System.out.println("发送结果：" + b);
    }
}
