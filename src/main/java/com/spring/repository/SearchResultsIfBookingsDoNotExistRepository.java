package com.spring.repository;

import com.spring.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SearchResultsIfBookingsDoNotExistRepository extends JpaRepository<Car, Integer> {
    @Query("SELECT d FROM Car d JOIN d.idealCar k " +
            "WHERE d.address LIKE %:location% ")
    Page<Car> findCarsByIdealCar(
            @Param("location") String location,
            Pageable pageable);

    @Query("SELECT COUNT(d) > 0 FROM Car d JOIN d.bookings p " +
            "WHERE d.address LIKE %:location% ")
    boolean checkEditIfNotExist(@Param("location") String newLocation);
}
