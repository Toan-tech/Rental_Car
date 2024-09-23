package com.spring.controller;

import com.spring.entities.*;
import com.spring.repository.BookingRepository;
import com.spring.repository.CarRepository;
import com.spring.repository.FeedBackRepository;
import com.spring.service.CarOwnerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CarOwnerController {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarOwnerServiceImpl carOwnerServiceImpl;

    @GetMapping(value = "/car-owner/mycar/add")
    public String addCar(Model model) {
        return "layout/Car_Owner/AddCar";
    }

    @GetMapping(value = "/car-owner/mycar")
    public String listCar(Model model,
                          @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
                          @RequestParam(value = "number", defaultValue = "3") Integer number,
                          @RequestParam(value = "sort", defaultValue = "1") Integer sort
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CarOwner carOwner = carOwnerServiceImpl.findByEmail(user.getUsername());
        Sort sortType = null;
        if (sort == 1) {
            sortType = Sort.by(Sort.Order.asc("productionYears"));
        } else if (sort == 2) {
            sortType = Sort.by(Sort.Order.desc("productionYears"));
        } else if (sort == 3) {
            sortType = Sort.by(Sort.Order.asc("basePrice"));
        } else if (sort == 4) {
            sortType = Sort.by(Sort.Order.desc("basePrice"));
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, number, sortType);
        Page<Car> carPage = carRepository.findAllByCarOwner(carOwner, pageable);

        List<Integer> numberOfRides = new ArrayList<>();
        List<Booking> bookings = new ArrayList<>();
        List<Boolean> hasRates = new ArrayList<>();

        for (Car car : carPage) {
            Integer numberOfRide = bookingRepository.countByCarAndStatus(car, BookingStatus.Completed);
            numberOfRides.add(numberOfRide);

            Booking booking = bookCar(car);
            bookings.add(booking);

            Boolean hasRate = checkRate(car);
            hasRates.add(hasRate);

            if (car.getId() == 10){
                car.getRatingAvgStar();
                System.out.println(car.getRatingAvgStar());
            }
        }

        int totalPages = carPage.getTotalPages();
        List<Integer> pageNums = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNums.add(i);
        }

        model.addAttribute("pageNums", pageNums);
        model.addAttribute("cars", carPage);
        model.addAttribute("number", number);
        model.addAttribute("sort", sort);
        model.addAttribute("numberOfRides", numberOfRides);
        model.addAttribute("bookings", bookings);
        model.addAttribute("hasRates", hasRates);

        return "layout/Car_Owner/List";
    }

    @GetMapping(value = "/car-owner/mycar/edit")
    public String showEditCar(Model model,
                              @RequestParam("carid") Integer carId
    ) {
        boolean authorized = validateRole(carId);
        if (!authorized) {
            return "redirect:/car-owner/mycar";
        } else {
            Car car = carRepository.findById(carId).orElse(null);
            Integer numberOfRide = bookingRepository.countByCarAndStatus(car, BookingStatus.Completed);
            Booking booking = bookCar(car);
            Boolean hasRate = checkRate(car);

            model.addAttribute("car", car);
            model.addAttribute("numberOfRide", numberOfRide);
            model.addAttribute("booking", booking);
            model.addAttribute("hasRate", hasRate);

            return "layout/Car_Owner/Edit";
        }
    }

    @PostMapping("/car-owner/mycar/edit")
    public String editCar(
            @RequestParam(value = "payment", required = false) Integer payment,
            @RequestParam(value = "deposit", required = false) Integer deposit
    ) {
        boolean authorized;
        if (payment != null) {
            Booking booking = bookingRepository.findById(payment).orElse(null);
            Car carPayment = booking.getCar();
            authorized = validateRole(carPayment.getId());
            if (!authorized) {
                return "redirect:/car-owner/mycar";
            } else {
                carPayment.setCarStatus(CarStatus.Available);
                carRepository.save(carPayment);
                changeStatus(payment, bookingRepository, BookingStatus.Completed);
                return "redirect:/car-owner/mycar/edit?carid=" + carPayment.getId();
            }
        } else {
            Booking booking = bookingRepository.findById(deposit).orElse(null);
            Car carDeposit = booking.getCar();
            authorized = validateRole(carDeposit.getId());
            if (!authorized) {
                return "redirect:/car-owner/mycar";
            } else {
                changeStatus(deposit, bookingRepository, BookingStatus.Confirmed);
                return "redirect:/car-owner/mycar/edit?carid=" + carDeposit.getId();
            }
        }
    }

    private Booking bookCar(Car car) {
        List<Booking> bookingList = bookingRepository.findAllByCar(car);
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.In_Progress);

        for (Booking book : bookingList) {
            if (book.getStatus().equals(BookingStatus.Pending_Deposit)) {
                booking = book;
                break;
            } else if (book.getStatus().equals(BookingStatus.Pending_Payment)) {
                booking = book;
                break;
            }
        }
        return booking;
    }

    private boolean validateRole(Integer carId) {
        boolean authorized = false;
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CarOwner carOwner = carOwnerServiceImpl.findByEmail(user.getUsername());
        List<Car> carList = carOwner.getCars();
        for (Car car : carList) {
            if (car.getId() == carId) {
                authorized = true;
                break;
            }
        }
        return authorized;
    }

    private void changeStatus(Integer bookingId, BookingRepository bookingRepository, BookingStatus bookingStatus) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        booking.setStatus(bookingStatus);
        bookingRepository.save(booking);
    }

    private boolean checkRate (Car car){
        Boolean hasRating = false;
        for (Booking carBooking : car.getBookings()){
            if (carBooking.getFeedback() == null){
                continue;
            } else if (carBooking.getFeedback().getRatings() != null ){
                hasRating = true;
                break;
            }
        }
        return hasRating;
    }
}

