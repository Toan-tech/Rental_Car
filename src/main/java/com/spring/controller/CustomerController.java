package com.spring.controller;

import com.spring.entities.Booking;
import com.spring.entities.RatingStar;
import com.spring.repository.SearchRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private SearchRepository searchRepository;

    @GetMapping({"/", "/Homepage"})
    public String home(Model model) {
        model.addAttribute("booking", new Booking());
        return "layout/customer/Homepage";
    }

    @Transactional
    @PostMapping("/Homepage")
    public String searchBookings(@ModelAttribute("booking") Booking booking, Model model) {
        List<Booking> bookings = searchRepository.searchBooking(
                booking.getDriverInfo(),
                booking.getStartDateTime(),
                booking.getEndDateTime()
        );

        if (bookings.isEmpty()) {
            model.addAttribute("errorMessage", "No cars match your credentials, please try again.");
            return "layout/customer/Homepage";
        }

        if (booking.getEndDateTime().isBefore(booking.getStartDateTime())) {
            model.addAttribute("errorMessage", "Drop-off date time must be later than pick-up date time, please try again.");
            return "layout/customer/Homepage";
        }

        model.addAttribute("bookings", bookings);
        return "layout/customer/ThumbView";
    }

    @Transactional
    @RequestMapping(value = "/ThumbView", method = {RequestMethod.POST, RequestMethod.GET})
    public String searchThumbView(
            @ModelAttribute("booking") @Valid Booking booking,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "3") int pageSizeDefault,
            @RequestParam(value = "sortOption", required = false) String sortOption,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "layout/customer/Homepage";
        }

        Sort sort = searchRepository.getSortOption(sortOption);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSizeDefault, sort);

        Page<Booking> page = null;
        if (booking.getDriverInfo() == null || booking.getDriverInfo().isBlank()) {
            page = searchRepository.findAll(pageable);
        } else {
            page = searchRepository.findDriverInfo(
                    booking.getDriverInfo(),
                    booking.getStartDateTime(),
                    booking.getEndDateTime(),
                    pageable
            );
        }

        int completedRides = (int) page.getContent().stream()
                .filter(b -> "Completed".equals(b.getStatus()))
                .count();

        int starRating = 0;
        if (!page.getContent().isEmpty() && page.getContent().getFirst().getFeedback() != null) {
            RatingStar feedback = page.getContent().getFirst().getFeedback().getRatings();
            starRating = switch (feedback) {
                case one_star -> 1;
                case two_stars -> 2;
                case three_stars -> 3;
                case four_stars -> 4;
                case five_stars -> 5;
                default -> 0;
            };
        }

        List<Integer> pageSizes = Arrays.asList(3, 6, 9, 12, 15);

        int totalPages = page.getTotalPages();
        List<Integer> pageNums = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNums.add(i);
        }

        model.addAttribute("pageSizes", pageSizes);
        model.addAttribute("feedback", starRating);
        model.addAttribute("count", completedRides);
        model.addAttribute("pageNums", pageNums);
        model.addAttribute("pageSize", pageSizeDefault);
        model.addAttribute("page", page);
        return "layout/customer/ThumbView";
    }
}
