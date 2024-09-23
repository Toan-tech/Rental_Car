package com.spring.rest;

import com.spring.entities.*;
import com.spring.repository.BookingRepository;
import com.spring.repository.CarOwnerRepository;
import com.spring.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChangeBookingStatus {
    @Autowired
    private CarOwnerRepository carOwnerRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping("/bookingstatus")
    public String bookStatus(
            @RequestParam(value = "payment", required = false) Integer payment,
            @RequestParam(value = "deposit", required = false) Integer deposit
    ) {
        boolean authorized = false;
        if (payment != null) {
            Booking booking = bookingRepository.findById(payment).orElse(null);
            Car car = booking.getCar();
            authorized = validateRole(car.getId());
            if (!authorized) {
                return "You are not authorized to perform this action";
            } else {
                car.setStatus(CarStatus.Available);
                carRepository.save(car);
                changeStatus(payment, bookingRepository, BookingStatus.Completed);
            }
        } else {
            Booking booking = bookingRepository.findById(deposit).orElse(null);
            Car car = booking.getCar();
            authorized = validateRole(car.getId());
            if (!authorized) {
                return "You are not authorized to perform this action";
            } else {
                changeStatus(deposit, bookingRepository, BookingStatus.Confirmed);
            }
        }
        return "Authorized validated";
    }

    private void changeStatus(Integer bookingId, BookingRepository bookingRepository, BookingStatus bookingStatus) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        booking.setStatus(bookingStatus);
        bookingRepository.save(booking);
    }

    private boolean validateRole(Integer carId) {
        boolean authorized = false;
//        TODO: update security
        CarOwner carOwner = carOwnerRepository.findById(1).orElse(null);
        List<Car> carList = carOwner.getCars();
        for (Car car : carList) {
            if (car.getId() == carId) {
                authorized = true;
                break;
            }
        }
        return authorized;
    }
}
