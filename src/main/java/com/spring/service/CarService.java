package com.spring.service;

import com.spring.entities.Booking;
import com.spring.entities.Car;
import com.spring.entities.RatingStar;
import com.spring.repository.CarRepository;
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

    public List<Car> extractDistinctCars(List<Booking> bookings) {
        return bookings.stream()
                .map(Booking::getCar)
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

    public double calculateAverageRating(Car car) {
        List<Booking> bookings = car.getBookings();

        // Map để lưu feedbacks với car id
        Map<Booking, Integer> bookingCarIdMap = new HashMap<>();
        for (Booking booking : bookings) {
            bookingCarIdMap.put(booking, car.getId());
        }

        // Tính tổng số sao và số lượng feedbacks
        double totalRating = 0;
        int count = 0;

        for (Map.Entry<Booking, Integer> entry : bookingCarIdMap.entrySet()) {
            Booking booking = entry.getKey();
            if (booking.getFeedback() != null) {
                RatingStar rating = booking.getFeedback().getRatings();
                totalRating += convertRatingToValue(rating);
                count++;
            }
        }

        if (count == 0) {
            return 0; // Không có feedback
        }

        // Tính trung bình số sao
        double averageRating = totalRating / count;

        // Làm tròn trung bình số sao đến nửa sao gần nhất
        return roundToNearestHalf(averageRating);
    }

    private double convertRatingToValue(RatingStar ratingStar) {
        return switch (ratingStar) {
            case five_stars -> 5.0;
            case four_stars -> 4.0;
            case three_stars -> 3.0;
            case two_stars -> 2.0;
            case one_star -> 1.0;
            default -> 0;
        };
    }

    // Hàm làm tròn đến nửa sao gần nhất
    private double roundToNearestHalf(double value) {
        if (value <= 0) return 0.0;
        return Math.ceil(value * 2) / 2.0;
    }
}

