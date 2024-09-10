package com.spring.repository;

import com.spring.entities.Car;
import com.spring.entities.CarOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findAllByCarOwner(CarOwner carOwner);
}
