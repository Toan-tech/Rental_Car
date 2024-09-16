package com.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetUrl) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Đặt lại mật khẩu của bạn");
        simpleMailMessage.setText("Bạn đã yêu cầu đặt lại mật khẩu. Vui lòng nhấp vào đường dẫn sau để đặt lại mật khẩu của bạn:\n" + resetUrl);
        simpleMailMessage.setFrom(fromEmail);
        javaMailSender.send(simpleMailMessage);
    }
}
