package com.spring.repository;

import com.spring.entities.Booking;
import com.spring.entities.BookingStatus;
import com.spring.entities.Car;
import com.spring.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Integer countByCarAndStatus(Car car, BookingStatus bookingStatus);
    List<Booking> findAllByCar(Car car);
    List<Booking> findByCustomer(Customer customer);

    Page<Booking> findAll(Pageable pageable);

    Page<Booking> findByCustomer(Customer customer, Pageable pageable);

}
