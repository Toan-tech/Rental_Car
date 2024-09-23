package com.spring.repository;

import com.spring.entities.IdealCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdealCarRepository extends JpaRepository<IdealCar, Integer> {
}
