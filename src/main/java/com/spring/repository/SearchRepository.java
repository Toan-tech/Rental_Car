package com.spring.repository;

import com.spring.entities.Booking;
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

    // Find all with pagination
    Page<Booking> findAll(Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.driverInfo LIKE %:driverInfo% " +
            "AND b.startDateTime >= :startDate AND b.endDateTime <= :endDate")
    List<Booking> findDriverInfo(@Param("driverInfo") String driverInfo,
                                 @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate,
                                 Sort sort);

    // Helper method to get sorting option
    default Sort getSortOption(String sortOption) {
        return switch (sortOption) {
            case "oldest" -> Sort.by(Sort.Direction.ASC, "startDateTime");
            case "priceLowHigh" -> Sort.by(Sort.Direction.ASC, "price");
            case "priceHighLow" -> Sort.by(Sort.Direction.DESC, "price");
            default -> Sort.by(Sort.Direction.DESC, "startDateTime");
        };
    }
}
