package com.spring.repository;

import com.spring.entities.Car;
import com.spring.entities.CarOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.spring.entities.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findAllByCarOwner(CarOwner carOwner);
    Page<Car> findAllByCarOwner(CarOwner carOwner, Pageable pageable);

    List<Car> findByStatus(CarStatus carStatus);
}
