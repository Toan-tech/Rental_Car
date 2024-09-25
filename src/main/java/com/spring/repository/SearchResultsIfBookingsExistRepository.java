package com.spring.repository;

import com.spring.entities.Booking;
import com.spring.entities.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SearchResultsIfBookingsExistRepository extends JpaRepository<Car, Integer> {
    @Query("SELECT d FROM Car d JOIN d.bookings p " +
            "WHERE d.address LIKE %:location% " +
            "AND p.endDateTime < :pickUp")
    Page<Car> findBookingsByIdealCarAndAvailableStatus(
            @Param("location") String location,
            @Param("pickUp") LocalDateTime pickUp,
            Pageable pageable);

    @Query("SELECT COUNT(d) > 0 FROM Car d JOIN d.bookings p " +
            "WHERE d.address LIKE %:location% " +
            "AND p.endDateTime < :newPickupDateTime")
    boolean checkEditIfExist(
            @Param("location") String newLocation,
            @Param("newPickupDateTime") LocalDateTime newPickupDateTime);
}

