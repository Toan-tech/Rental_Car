package com.spring.controller;

import com.spring.entities.Booking;
import com.spring.entities.BookingStatus;
import com.spring.entities.Car;
import com.spring.entities.FeedBack;
import com.spring.repository.CarRepository;
import com.spring.repository.SearchRepository;
import com.spring.service.RatingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomepageController {
    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private CarRepository carRepository;

    @GetMapping({"/", "/Homepage"})
    public String home(Model model) {
        model.addAttribute("booking", new Booking());
        model.addAttribute("car", new Car());
        return "layout/customer/Homepage";
    }

    @Transactional
    @PostMapping("/Homepage")
    public String searchBookings(@ModelAttribute("booking") Booking booking,
                                 @ModelAttribute("car") Car car,
                                 Model model
    ) {
        // Fetch bookings based on search criteria
        List<Booking> bookings = searchRepository.searchBooking(
                booking.getDriverInfo(),
                booking.getStartDateTime(),
                booking.getEndDateTime()
        );

        // Check if the bookings list is empty
        if (bookings.isEmpty()) {
            model.addAttribute("errorMessage", "No cars match your credentials, please try again.");
            return "layout/customer/Homepage";
        }

        // Validate if the end date is after the start date
        if (booking.getEndDateTime().isBefore(booking.getStartDateTime())) {
            model.addAttribute("errorMessage", "Drop-off date time must be later than pick-up date time, please try again.");
            return "layout/customer/Homepage";
        }

        // Extract the list of cars from the bookings
        List<Car> cars = bookings.stream()
                .map(Booking::getCar)
                .distinct()
                .collect(Collectors.toList());

        // Count the total number of cars in the result
        long numberOfCar = cars.size();

        // Completed rides
        List<Booking> carListThatCompletedBooking = searchRepository.findByStatus(BookingStatus.Completed);

        Map<Car, Integer> carBookingCountMap = new HashMap<>();

        for (Booking completedBooking : carListThatCompletedBooking) {
            car = completedBooking.getCar();
            carBookingCountMap.put(car, carBookingCountMap.getOrDefault(car, 0) + 1);
        }

        int completedRides = 0;

        for (Map.Entry<Car, Integer> entry : carBookingCountMap.entrySet()) {
            if (entry.getValue() >= 1) {
                completedRides += entry.getValue();
            }
        }

        // Get feedback ratings
        FeedBack feedback = ratingService.getFeedbackByBooking(booking);

        // Set model attributes for view
        model.addAttribute("NumberOfCar", numberOfCar);
        model.addAttribute("CarCount", completedRides);
        model.addAttribute("feedback", feedback);
        model.addAttribute("pickupDate", booking.getStartDateTime().toLocalDate());
        model.addAttribute("pickupTime", booking.getStartDateTime().toLocalTime());
        model.addAttribute("dropoffDate", booking.getEndDateTime().toLocalDate());
        model.addAttribute("dropoffTime", booking.getEndDateTime().toLocalTime());
        model.addAttribute("bookings", bookings);
        model.addAttribute("cars", cars);

        return "layout/customer/ThumbView";
//        return "layout/customer/ListView";
    }
}
