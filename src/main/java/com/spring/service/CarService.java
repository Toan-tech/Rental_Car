package com.spring.service;

import com.spring.entities.*;
import com.spring.repository.BookingRepository;
import com.spring.repository.CarRepository;
import com.spring.repository.FeedBackRepository;
import com.spring.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SearchRepository searchRepository;

    public List<Car> extractDistinctAvailableCars(List<Booking> bookingList) {
        return bookingList.stream()
                .map(Booking::getCar)
                .filter(car -> CarStatus.Available.equals(car.getStatus()))
                .distinct()
                .collect(Collectors.toList());
    }

    public Map<Car, Integer> countCompletedRides(List<Booking> completedBookings) {
        Map<Car, Integer> carBookingCountMap = new HashMap<>();
        for (Booking completedBooking : completedBookings) {
            Car car = completedBooking.getCar();
            carBookingCountMap.put(car, carBookingCountMap.getOrDefault(car, 0) + 1);
        }
        return carBookingCountMap;
    }


    public boolean isCarAvailable(Integer carId, String pickupLocation, LocalDate pickupDate, LocalTime pickupTime, LocalDate dropoffDate, LocalTime dropoffTime) {
        LocalDateTime newPickupDateTime = LocalDateTime.of(pickupDate, pickupTime);
        LocalDateTime newDropOffDateTime = LocalDateTime.of(dropoffDate, dropoffTime);

        List<Booking> existingBookings = searchRepository.checkBookingAvailableByCarIdAndIdealCar(carId, pickupLocation, newPickupDateTime, newDropOffDateTime);

        return !existingBookings.isEmpty();
    }
}

