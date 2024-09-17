package com.spring.service;

import com.spring.entities.Users;
import com.spring.model.UsersDTO;
import com.spring.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface UsersService {
    Users findByEmail(String email);

    void registerUser(UsersDTO usersDTO);

    void updatePassword(Users users, String newPassword);
}
