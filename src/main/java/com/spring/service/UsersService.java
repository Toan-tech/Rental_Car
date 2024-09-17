package com.spring.service;

import com.spring.entities.Users;
import com.spring.model.UsersDTO;

public interface UsersService {
    Users findByEmail(String email);
    void registerUser(UsersDTO usersDTO);
    void updatePassword(Users users, String newPassword);
}
