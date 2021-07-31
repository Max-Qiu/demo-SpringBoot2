package com.maxqiu.demo.service;

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
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件 服务
 *
 * @author Max_Qiu
 */
@Service
public class EmailService {
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
    public boolean simpleMailMessage(List<String> toList, List<String> ccList, List<String> bccList, String subject,
        String text) {
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
    public boolean mimeMessage(List<String> toList, List<String> ccList, List<String> bccList, String subject,
        String text, boolean textIsHtml, List<Inline> inlineList, List<Attachment> attachmentList) {
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
            for (Inline inline : inlineList) {
                helper.addInline(inline.getId(), new File(inline.getFilePath()));
            }
            // 外部附件
            for (Attachment attachment : attachmentList) {
                helper.addAttachment(attachment.getFileName(), new File(attachment.getFilePath()));
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
    public boolean mimeMessage(List<String> toList, String subject, String text, boolean textIsHtml,
        List<Inline> inlineList, List<Attachment> attachmentList) {
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
    public boolean mimeMessage(String to, String subject, String text, boolean textIsHtml, List<Inline> inlineList,
        List<Attachment> attachmentList) {
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
    public boolean templateMessage(List<String> toList, List<String> ccList, List<String> bccList, String subject,
        String template, Map<String, Object> map, List<Inline> inlineList, List<Attachment> attachmentList) {
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
            for (Inline inline : inlineList) {
                helper.addInline(inline.getId(), new File(inline.getFilePath()));
            }
            // 外部附件
            for (Attachment attachment : attachmentList) {
                helper.addAttachment(attachment.getFileName(), new File(attachment.getFilePath()));
            }
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
