package com.spring.service;

import com.spring.entities.Booking;
import com.spring.entities.Car;
import com.spring.entities.CarStatus;
import com.spring.entities.IdealCar;
import com.spring.repository.CarRepository;
import com.spring.repository.IdealCarRepository;
import com.spring.repository.SearchResultsIfBookingsDoNotExistRepository;
import com.spring.repository.SearchResultsIfBookingsExistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private IdealCarRepository idealCarRepository;

    @Autowired
    private SearchResultsIfBookingsExistRepository searchResultsIfBookingsExistRepository;

    @Autowired
    private SearchResultsIfBookingsDoNotExistRepository searchResultsIfBookingsDoNotExistRepository;

    public boolean isCarAvailable(Integer carId, String newLocation, LocalDate pickupDate, LocalTime pickupTime, LocalDate dropOffDate, LocalTime dropOffTime) {
        LocalDateTime newPickupDateTime = LocalDateTime.of(pickupDate, pickupTime);
        LocalDateTime newDropOffDateTime = LocalDateTime.of(dropOffDate, dropOffTime);

        // Implement your search logic here using the provided parameters and return the result
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + carId));

        IdealCar idealCar = idealCarRepository.findById(car.getIdealCar().getId())
                .orElseThrow(() -> new IllegalArgumentException("IdealCar not found"));

        if (car.getBookings().isEmpty()) {
            return searchResultsIfBookingsDoNotExistRepository.checkEditIfNotExist(newLocation);
        } else {
            return searchResultsIfBookingsExistRepository.checkEditIfExist(newLocation, newPickupDateTime);
        }
    }
}

