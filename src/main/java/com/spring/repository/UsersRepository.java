package com.spring.repository;

import com.spring.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);

    Users findUsersByEmail(String email);

    boolean existsByEmail (String email);
}
