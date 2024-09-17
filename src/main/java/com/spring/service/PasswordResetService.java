package com.spring.service;

import com.spring.entities.PasswordResetToken;
import com.spring.entities.Users;

public interface PasswordResetService {
    void createPasswordResetTokenForUser(Users users, String token);
    PasswordResetToken findByToken(String token);
    void deleteToken(PasswordResetToken token);
    boolean tokenExists(String token);
}
