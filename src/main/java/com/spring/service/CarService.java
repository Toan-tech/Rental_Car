package com.spring.service;

import com.spring.entities.*;
import com.spring.repository.CarRepository;
import com.spring.repository.FeedBackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public double calculateAverageRating(Integer carId) {
        List<String> ratingList = feedBackRepository.findALLStarByCarID(carId);

        double rating = 0.0;

        for (String ratingValue : ratingList) {
            rating += convertRatingToValue(ratingValue);
        }
        if (!ratingList.isEmpty()) {
            rating = (double) rating / ratingList.size();
        }
        return Math.ceil(rating * 2) / 2.0;
    }

    private double convertRatingToValue(String enumValue) {
        return switch (enumValue) {
            case "one_star" -> 1;
            case "two_stars" -> 2;
            case "three_stars" -> 3;
            case "four_stars" -> 4;
            case "five_stars" -> 5;
            default -> 0;
        };
    }
}

