package com.spring.service;

import org.jetbrains.annotations.NotNull;
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

    @Override
    public void sendSimpleMailMessage(String carName, String bookingDate, String toEmail) {
        String subject = "Your car has been booked";
        String body = "Congratulations! Your car " + carName + " has been booked at " + bookingDate +
                ". Please go to your wallet to check if the deposit has been paid and go to your car’s " +
                "details page to confirm the deposit. Thank you!";

        // Create a SimpleMailMessage object
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(fromEmail);
        javaMailSender.send(message);
    }
}
