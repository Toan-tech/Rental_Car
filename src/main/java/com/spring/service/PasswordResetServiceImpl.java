package com.spring.service;

import com.spring.entities.PasswordResetToken;
import com.spring.entities.Users;
import com.spring.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasswordResetServiceImpl implements PasswordResetService{
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public void createPasswordResetTokenForUser(Users users, String token) {
        PasswordResetToken existingToken = passwordResetTokenRepository.findByToken(token).orElse(null);
        if (existingToken != null) {
            passwordResetTokenRepository.delete(existingToken);
        }

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUsers(users);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token).orElse(null);
    }

    @Override
    public void deleteToken(PasswordResetToken token) {
        passwordResetTokenRepository.delete(token);
    }

    @Override
    public boolean tokenExists(String token) {
        return passwordResetTokenRepository.findByToken(token).isPresent();
    }
}
