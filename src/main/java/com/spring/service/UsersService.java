package com.spring.service;

import com.spring.entities.Users;
import com.spring.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    @Autowired
    UsersRepository usersRepository;

    public void save(Users users) {
        usersRepository.save(users);
    }
}
