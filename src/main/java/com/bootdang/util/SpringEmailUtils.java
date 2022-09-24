package com.bootdang.util;


import org.apache.naming.factory.SendMailFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.security.auth.Subject;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Component
public class SpringEmailUtils {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 简单邮件
     * @param context
     * @param email
     * @param Subject
     * @return
     */
    public boolean sendMessage(String context,String email,String Subject){
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(from);//发送人
            simpleMailMessage.setTo(email);//收件人
            simpleMailMessage.setSubject(Subject);//主题
            simpleMailMessage.setText(context);//发送内容
            javaMailSender.send(simpleMailMessage);
        }catch (Exception e){
            e.getMessage();
            return false;
        }
        return true;
    }

    /**
     * 发送带html的邮件
     * @param context
     * @param email
     * @param Subject
     * @return
     */
    public boolean sendMessageHtml(String context,String email,String Subject){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper per = new MimeMessageHelper(mimeMessage, true);
            per.setFrom(from);
            per.setTo(email);
            per.setSubject(Subject);
            per.setText(context,true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void main (String[] args) {
    }
    public String getEmailHtml(String to,String code,String title){
        String[] tos = to.split("@");
        String sub = tos[0].substring(0,2);
        String email = sub+"*******@"+tos[1];
        String html = "<div style=\"padding: 0px 0px 0px 20px;box-sizing: border-box;color: #333333;font-family: \"microsoft yahei\";font-size: 14px\">" +
                "<h3 style=\"font-weight: normal;font-size: 18px;\">javaBarry博客</h3>" +
                "<h4 style=\"color:#2672EC;font-size: 40px;margin-top: 24px;font-weight: normal;\">"+title+"验证码</h4>" +
                "<div style=\"margin-top: 40px;\">您好，您正在使用<a href=\"javascript:;\" target=\"_blank\" style=\"color: #2672EC;text-decoration: none;\">"+email+"</a>注册账号。</div>" +
                "<div style=\"margin-top: 30px;\">您的验证码为：<em style=\"font-style: normal;font-weight: 600;\">"+code+"</em></div>" +
                "<div style=\"margin-top: 35px;\">谢谢！</div>" +
                "<div style=\"margin-top: 10px;\">javaBarry博客 版权所有</div>" +
                "</div>";
        return html;
    }
}
