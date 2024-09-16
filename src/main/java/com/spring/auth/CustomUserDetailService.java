package com.spring.auth;

import com.spring.entities.Users;
import com.spring.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Users> usersOptional = usersRepository.findByEmail(email);
        if (usersOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        Users usersDB = usersOptional.get();

        return User.withUsername(usersDB.getEmail())
                .password(usersDB.getPassword())
                .authorities(usersDB.getRole().name())
                .build();
    }
}
