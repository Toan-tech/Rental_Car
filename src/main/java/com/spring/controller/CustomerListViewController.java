package com.spring.controller;

import com.spring.entities.Booking;
import com.spring.entities.BookingStatus;
import com.spring.entities.Car;
import com.spring.entities.FeedBack;
import com.spring.repository.SearchRepository;
import com.spring.service.RatingService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CustomerListViewController {
    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private RatingService ratingService;

    @Transactional
    @RequestMapping(value = "/ListView", method = {RequestMethod.POST, RequestMethod.GET})
    public String searchListView(
            @ModelAttribute("booking") @Valid Booking booking,
            @ModelAttribute("car") Car car,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "1") int pageSize,
            @RequestParam(value = "sortOption", defaultValue = "newest") String sortOption,
            @RequestParam(value = "pickupDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate pickupDate,
            @RequestParam(value = "pickupTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime pickupTime,
            @RequestParam(value = "dropoffDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dropoffDate,
            @RequestParam(value = "dropoffTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime  dropoffTime,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "layout/customer/Homepage";
        }

        // Combine date and time into LocalDateTime
        LocalDateTime startDateTime = LocalDateTime.of(pickupDate, pickupTime);
        LocalDateTime endDateTime = LocalDateTime.of(dropoffDate, dropoffTime);

        if (endDateTime.isBefore(startDateTime)) {
            model.addAttribute("errorMessage", "Drop-off date time must be later than pick-up date time, please try again.");
            return "layout/customer/ListView";
        }

        // Further logic
        Sort sort = searchRepository.getSortOption(sortOption);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        // Fetch bookings based on search criteria
        List<Booking> bookings = searchRepository.searchBooking(
                booking.getDriverInfo(),
                booking.getStartDateTime(),
                booking.getEndDateTime()
        );

        // Extract the list of cars from the bookings
        List<Car> cars = bookings.stream()
                .map(Booking::getCar)
                .distinct()
                .collect(Collectors.toList());

        Page<Booking> page = searchRepository.findDriverInfo(
                booking.getDriverInfo(),
                booking.getStartDateTime(),
                booking.getEndDateTime(),
                pageable
        );

        if (page.isEmpty()) {
            model.addAttribute("errorMessage", "No cars match your credentials, please try again.");
            return "layout/customer/ListView";
        }

        // Get total count of cars matching the criteria
        long numberOfCar = cars.size();

        // CompletedRides
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

        // Pagination
        int totalPages = (int) Math.ceil((double) bookings.size() / pageSize);
        List<Integer> pageNums = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNums.add(i);
        }

        // Set pre-filled values
        model.addAttribute("pickupDate", pickupDate);
        model.addAttribute("pickupTime", pickupTime);
        model.addAttribute("dropoffDate", dropoffDate);
        model.addAttribute("dropoffTime", dropoffTime);

        // Additional model attributes for pagination, ratings, etc.
        model.addAttribute("NumberOfCar", numberOfCar);
        model.addAttribute("feedback", feedback);
        model.addAttribute("CarCount", completedRides);
        model.addAttribute("pageNums", pageNums);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("page", page);
        model.addAttribute("sortOption", sortOption);
        model.addAttribute("bookings", bookings);
        model.addAttribute("cars", cars);

        return "layout/customer/ListView";
    }
}
