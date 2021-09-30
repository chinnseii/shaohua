/*
 * @Date: 2021-09-16 11:11:48
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-17 11:49:07
 * @FilePath: \notec:\Users\BP-chenshengwei\Desktop\prc\stzb\src\main\java\com\kaoqin\stzb\service\EmailService.java
 */
package com.kaoqin.stzb.service;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

/**
 * The Email service.
 *
 * @author felord.cn
 * @since 2020 /1/14 23:22
 */
@Component
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送纯文本邮件.
     *
     * @param to      目标email 地址
     * @param subject 邮件主题
     * @param text    纯文本内容
     */
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    /**
     * 发送HTML邮件.
     *
     * @param to      目标email 地址
     * @param subject 邮件主题
     * @param text    HTML邮件主体
     */
    public boolean sendHtmlMail(String to, String subject, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, true);
            javaMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            log.error(message+"发送html邮件时发生异常", e);
            return false;
        }
    }
}