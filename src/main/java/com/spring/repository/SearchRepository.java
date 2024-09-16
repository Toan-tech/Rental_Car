package com.spring.repository;

import com.spring.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findAll(Pageable pageable);

    @Query("SELECT d FROM Booking d JOIN d.idealCar k " +
            "WHERE k.location LIKE %:driverInfo% " +
            "AND k.pickupDateTime >= :startDate " +
            "AND k.dropOffDateTime <= :endDate")
    List<Booking> findByIdealCar(
            @Param("driverInfo") String driverInfo,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT d FROM Booking d JOIN d.idealCar k " +
            "WHERE k.location LIKE %:driverInfo% " +
            "AND k.pickupDateTime >= :startDate " +
            "AND k.dropOffDateTime <= :endDate")
    Page<Booking> findBookingsByIdealCar(
            @Param("driverInfo") String driverInfo,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findBookingsByCarId(Integer carId);
}
