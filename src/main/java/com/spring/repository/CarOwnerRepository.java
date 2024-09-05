package com.spring.repository;

import com.spring.entities.CarOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarOwnerRepository extends JpaRepository<CarOwner, Integer> {
}
