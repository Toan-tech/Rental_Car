package com.spring.service;

public interface EmailService {
    void sendPasswordResetEmail(String toEmail, String resetUrl);
}
