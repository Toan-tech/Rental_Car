package com.spring.repository;

import com.spring.entities.CarOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarOwnerRepository extends JpaRepository<CarOwner, Integer> {
    CarOwner findCarOwnerByEmail(String email);

    Optional<CarOwner> findByEmail(String email);
}
