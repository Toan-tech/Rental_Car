package com.spring.controller;

import com.spring.entities.Booking;
import com.spring.entities.BookingStatus;
import com.spring.entities.FeedBack;
import com.spring.repository.RatingRepository;
import com.spring.repository.SearchRepository;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @GetMapping({"/", "/Homepage"})
    public String home(Model model) {
        model.addAttribute("booking", new Booking());
        return "layout/customer/Homepage";
    }

    @Transactional
    @PostMapping("/Homepage")
    public String searchBookings(@ModelAttribute("booking") Booking booking, Model model) {
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

        // Count the total number of cars in the result
        long numberOfCar = bookings.size();

        // Completed rides
        int completedRides = 0;
        for (Booking b : bookings) {
            if (b.getStatus().equals(BookingStatus.Completed)) {
                completedRides++;
            }
        }

        // Get feedback ratings
        List<FeedBack> feedbackList = ratingRepository.findFeedbackByBooking(booking);
        int[] starRatings = new int[5];

        feedbackList.forEach(feedback -> {
            switch (feedback.getRatings()) {
                case one_star -> starRatings[0]++;
                case two_stars -> starRatings[1]++;
                case three_stars -> starRatings[2]++;
                case four_stars -> starRatings[3]++;
                case five_stars -> starRatings[4]++;
            }
        });

        // Set model attributes for view
        model.addAttribute("NumberOfCar", numberOfCar);
        model.addAttribute("CarCount", completedRides);
        model.addAttribute("pickupDate", booking.getStartDateTime().toLocalDate());
        model.addAttribute("pickupTime", booking.getStartDateTime().toLocalTime());
        model.addAttribute("dropoffDate", booking.getEndDateTime().toLocalDate());
        model.addAttribute("dropoffTime", booking.getEndDateTime().toLocalTime());
        model.addAttribute("bookings", bookings);

        return "layout/customer/ThumbView";
    }


    @Transactional
    @RequestMapping(value = "/ThumbView", method = {RequestMethod.POST, RequestMethod.GET})
    public String searchThumbView(
            @ModelAttribute("booking") @Valid Booking booking,
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
            return "layout/customer/ThumbView";
        }

        // Further logic
        Sort sort = searchRepository.getSortOption(sortOption);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<Booking> page = searchRepository.findDriverInfo(
                booking.getDriverInfo(),
                booking.getStartDateTime(),
                booking.getEndDateTime(),
                pageable
        );

        if (page.isEmpty()) {
            model.addAttribute("errorMessage", "No cars match your credentials, please try again.");
            return "layout/customer/ThumbView";
        }

        if (booking.getEndDateTime().isBefore(booking.getStartDateTime())) {
            model.addAttribute("errorMessage", "Drop-off date time must be later than pick-up date time, please try again.");
            return "layout/customer/ThumbView";
        }

        // Get total count of cars matching the criteria
        long numberOfCar = page.getTotalElements();

        // CompletedRides
        Page<Booking> confirmedBookings = searchRepository.findByStatus(BookingStatus.Completed, pageable);
        int completedRides = confirmedBookings.getSize();

        // Get feedback ratings
        List<FeedBack> feedbackList = ratingRepository.findFeedbackByBooking(booking);
        int[] starRatings = new int[5];

        feedbackList.forEach(feedback -> {
            switch (feedback.getRatings()) {
                case one_star -> starRatings[0]++;
                case two_stars -> starRatings[1]++;
                case three_stars -> starRatings[2]++;
                case four_stars -> starRatings[3]++;
                case five_stars -> starRatings[4]++;
            }
        });

        // Pagination
        int totalPages = page.getTotalPages();
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
        model.addAttribute("starRatings", List.of(starRatings));
        model.addAttribute("CarCount", completedRides);
        model.addAttribute("pageNums", pageNums);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("page", page);
        model.addAttribute("sortOption", sortOption);

        return "layout/customer/ThumbView";
    }
}
