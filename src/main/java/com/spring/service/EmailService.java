package com.spring.service;

public interface EmailService {
    void sendPasswordResetEmail(String toEmail, String resetUrl);
    void sendSimpleMailMessage(String carName, String bookingDate, String toEmail);
}
