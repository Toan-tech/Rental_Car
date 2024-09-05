package com.spring.controller;

import com.spring.entities.Booking;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<Booking> bookings = searchBookings(booking, Sort.by(Sort.Direction.DESC, "startDateTime"));
        if (bookings.isEmpty()) {
            model.addAttribute("errorMessage", "No cars match your credentials, please try again.");
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
            @RequestParam(value = "sortOption", required = false) String sortOption,
            BindingResult result, Model model) {

        Page<Booking> page = getPaginatedBookings(pageNumber, sortOption);

        model.addAttribute("pageNums", getPageNumbers(page));
        model.addAttribute("page", page);
        return processSearch(booking, sortOption, result, model, "layout/customer/ThumbView");
    }

    @GetMapping("/ListView")
    public String listViewDetails(Model model) {
        model.addAttribute("booking", new Booking());
        return "layout/customer/ListView";
    }

    @Transactional
    @RequestMapping(value = "/ListView", method = {RequestMethod.POST, RequestMethod.GET})
    public String searchListView(
            @ModelAttribute("booking") @Valid Booking booking,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "sortOption", required = false) String sortOption,
            BindingResult result, Model model) {

        Page<Booking> page = getPaginatedBookings(pageNumber, sortOption);

        model.addAttribute("pageNums", getPageNumbers(page));
        model.addAttribute("page", page);
        return processSearch(booking, sortOption, result, model, "layout/customer/ListView");
    }

    private Page<Booking> getPaginatedBookings(Integer pageNumber, String sortOption) {
        int pageSize = 3;
        Sort sort = searchRepository.getSortOption(sortOption);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return searchRepository.findAll(pageable);
    }

    private List<Integer> getPageNumbers(Page<Booking> page) {
        List<Integer> pageNums = new ArrayList<>();
        for (int i = 1; i <= page.getTotalPages(); i++) {
            pageNums.add(i);
        }
        return pageNums;
    }

    private String processSearch(Booking booking, String sortOption, BindingResult result, Model model, String viewName) {
        if (result.hasErrors()) {
            return viewName;
        }

        if (booking.getEndDateTime().isBefore(booking.getStartDateTime())) {
            result.rejectValue("endDateTime", "error.booking", "Drop-off date and time must be later than pick-up date and time");
            return viewName;
        }

        List<Booking> bookings = searchBookings(booking, searchRepository.getSortOption(sortOption));
        model.addAttribute("carCount", bookings.size());
        model.addAttribute("bookings", bookings);

        return viewName;
    }

    private List<Booking> searchBookings(Booking booking, Sort sort) {
        if (booking.getDriverInfo() == null || booking.getDriverInfo().isBlank()) {
            return searchRepository.findAll();
        } else {
            return searchRepository.findDriverInfo(
                    booking.getDriverInfo(),
                    booking.getStartDateTime(),
                    booking.getEndDateTime(),
                    sort
            );
        }
    }
}
