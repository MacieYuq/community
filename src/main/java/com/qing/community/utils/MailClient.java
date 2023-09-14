package com.qing.community.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailClient {
    //声明一个logger，方便记录邮件发送失败时的log
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    //注入javamailsender组件
    @Autowired
    private JavaMailSender mailSender;

    //注入发件人信息
    @Value("${spring.mail.username}")
    private String from;

    //公开发送方法，包括收件人，主题，和内容
    public void sendMail(String receiver, String subject, String content){
        try {
            //创建mimemessage对象，并通过helper构建邮件具体信息
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(receiver);
            helper.setSubject(subject);
            helper.setText(content, true);//加true表示邮件内容支持html格式
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("邮件发送失败"+ e.getMessage());
        }

    }
}
