package com.spring.repository;

import com.spring.entities.Booking;
import com.spring.entities.BookingStatus;
import com.spring.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Integer countByCarAndStatus(Car car, BookingStatus bookingStatus);
    List<Booking> findAllByCar(Car car);
}
