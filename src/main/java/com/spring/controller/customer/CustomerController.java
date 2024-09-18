package com.spring.controller.customer;

import com.spring.entities.*;
import com.spring.repository.*;
import com.spring.service.BookingService;
import com.spring.service.CarService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class CustomerController {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private IdealCarRepository idealCarRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CarService carService;

    @GetMapping("/ResultView")
    public String showResultView(Model model) {
        model.addAttribute("booking", new Booking());
        model.addAttribute("car", new Car());
        return "layout/customer/ResultView";
    }

    @GetMapping("/viewDetails")
    public String viewBookingDetails(@RequestParam("carId") Integer carId, Model model) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + carId));

        List<Booking> bookings = searchRepository.findBookingsByCarId(carId);

        Booking foundBooking = null;
        for (Booking booking : bookings) {
            if (booking.getId() != 0) {
                foundBooking = booking;
                break;
            }
        }

        if (foundBooking == null) {
            throw new IllegalArgumentException("No valid booking found for car Id: " + carId);
        }

        IdealCar idealCar = idealCarRepository.findById(foundBooking.getIdealCar().getId())
                .orElseThrow(() -> new IllegalArgumentException("IdealCar not found"));

        model.addAttribute("car", car);
        model.addAttribute("booking", foundBooking);
        model.addAttribute("idealCar", idealCar);

        return "layout/customer/ViewDetails_BasicInformation";
    }

    @GetMapping("/details")
    public String viewDetails(@RequestParam("carId") Integer carId, Model model) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id:" + carId));

        List<Booking> bookings = searchRepository.findBookingsByCarId(carId);

        Booking foundBooking = null;
        for (Booking booking : bookings) {
            if (booking.getId() != 0) {  // Ensuring booking has a valid ID
                foundBooking = booking;
                break;  // Exit loop once a booking is found
            }
        }

        if (foundBooking == null) {
            throw new IllegalArgumentException("No valid booking found for car Id: " + carId);
        }

        IdealCar idealCar = idealCarRepository.findById(foundBooking.getIdealCar().getId())
                .orElseThrow(() -> new IllegalArgumentException("IdealCar not found"));

        model.addAttribute("car", car);
        model.addAttribute("booking", foundBooking);
        model.addAttribute("idealCar", idealCar);

        return "layout/customer/ViewDetails_Details";
    }

    @GetMapping("/term")
    public String termView(@RequestParam("carId") Integer carId, Model model) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id:" + carId));

        List<Booking> bookings = searchRepository.findBookingsByCarId(carId);

        Booking foundBooking = null;
        for (Booking booking : bookings) {
            if (booking.getId() != 0) {
                foundBooking = booking;
                break;
            }
        }

        if (foundBooking == null) {
            throw new IllegalArgumentException("No valid booking found for car Id: " + carId);
        }

        IdealCar idealCar = idealCarRepository.findById(foundBooking.getIdealCar().getId())
                .orElseThrow(() -> new IllegalArgumentException("IdealCar not found"));

        model.addAttribute("car", car);
        model.addAttribute("booking", foundBooking);
        model.addAttribute("idealCar", idealCar);

        return "layout/customer/ViewDetails_Term";
    }

    @GetMapping("/Overview")
    public String bookingOverview(@RequestParam("carId") Integer carId,
                                  Model model) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + carId));

        List<Booking> bookings = searchRepository.findBookingsByCarId(carId);

        Booking foundBooking = null;
        for (Booking booking : bookings) {
            if (booking.getId() != 0) {
                foundBooking = booking;
                break;
            }
        }

        if (foundBooking == null) {
            throw new IllegalArgumentException("No valid booking found for car Id: " + carId);
        }

        IdealCar idealCar = idealCarRepository.findById(foundBooking.getIdealCar().getId())
                .orElseThrow(() -> new IllegalArgumentException("IdealCar not found"));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        long numberOfDays = ChronoUnit.DAYS.between(idealCar.getPickupDateTime(), idealCar.getDropOffDateTime());

        model.addAttribute("car", car);
        model.addAttribute("numberOfDays", numberOfDays);
        model.addAttribute("booking", foundBooking);
        model.addAttribute("pickupDate", idealCar.getPickupDateTime().toLocalDate().format(dateFormatter));
        model.addAttribute("pickupTime", idealCar.getPickupDateTime().toLocalTime().format(timeFormatter));
        model.addAttribute("dropoffDate", idealCar.getDropOffDateTime().toLocalDate().format(dateFormatter));
        model.addAttribute("dropoffTime", idealCar.getDropOffDateTime().toLocalTime().format(timeFormatter));
        model.addAttribute("idealCar", idealCar);

        return "layout/customer/OverView";
    }


    @GetMapping("/editBooking")
    public String editBooking(@RequestParam("carId") Integer carId,
                              Model model) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + carId));

        List<Booking> bookings = searchRepository.findBookingsByCarId(carId);

        Booking foundBooking = null;
        for (Booking booking : bookings) {
            if (booking.getId() != 0) {  // Ensuring booking has a valid ID
                foundBooking = booking;
                break;  // Exit loop once a booking is found
            }
        }

        if (foundBooking == null) {
            throw new IllegalArgumentException("No valid booking found for car Id: " + carId);
        }

        IdealCar idealCar = idealCarRepository.findById(foundBooking.getIdealCar().getId())
                .orElseThrow(() -> new IllegalArgumentException("IdealCar not found"));

        model.addAttribute("car", car);
        model.addAttribute("booking", foundBooking);
        model.addAttribute("pickupDate", idealCar.getPickupDateTime().toLocalDate());
        model.addAttribute("pickupTime", idealCar.getPickupDateTime().toLocalTime());
        model.addAttribute("dropoffDate", idealCar.getDropOffDateTime().toLocalDate());
        model.addAttribute("dropoffTime", idealCar.getDropOffDateTime().toLocalTime());
        model.addAttribute("idealCar", idealCar);

        return "layout/customer/edit/EditBooking";
    }

    @PostMapping("/updateBooking")
    public String updateBooking(@RequestParam("carId") Integer carId,
                                @RequestParam("pickupLocation") String pickupLocation,
                                @RequestParam("pickupDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate pickupDate,
                                @RequestParam("pickupTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime pickupTime,
                                @RequestParam("dropoffDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dropoffDate,
                                @RequestParam("dropoffTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime dropoffTime,
                                RedirectAttributes redirectAttributes) {

        List<Booking> bookings = searchRepository.findBookingsByCarId(carId);

        Booking foundBooking = null;
        for (Booking booking : bookings) {
            if (booking.getId() != 0) {  // Ensuring booking has a valid ID
                foundBooking = booking;
                break;  // Exit loop once a booking is found
            }
        }

        if (foundBooking == null) {
            throw new IllegalArgumentException("No valid booking found for car Id: " + carId);
        }

        IdealCar idealCar = idealCarRepository.findById(foundBooking.getIdealCar().getId())
                .orElseThrow(() -> new IllegalArgumentException("IdealCar not found"));

        idealCar.setLocation(pickupLocation);
        idealCar.setPickupDateTime(LocalDateTime.of(pickupDate, pickupTime));
        idealCar.setDropOffDateTime(LocalDateTime.of(dropoffDate, dropoffTime));

        idealCarRepository.save(idealCar);

        redirectAttributes.addFlashAttribute("successMessage", "Booking details updated successfully!");

        return "redirect:" + UriComponentsBuilder.fromPath("/Overview")
                .queryParam("carId", carId)
                .build().toUriString();
    }

//    @PostMapping("/fillBookingInformation")
//    public String fillBookingInformation(@RequestParam("carId") Integer carId,
//                                     @RequestParam("customerName") String customerName,
//                                     @RequestParam("customerEmail") String customerEmail,
//                                     @RequestParam("customerPhone") String customerPhone,
//                                     RedirectAttributes redirectAttributes) {
//
//    }
}
