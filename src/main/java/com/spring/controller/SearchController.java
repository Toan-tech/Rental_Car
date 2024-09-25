package com.spring.controller;

import com.spring.entities.*;
import com.spring.repository.*;
import jakarta.transaction.Transactional;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private IdealCarRepository idealCarRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private SearchResultsIfBookingsDoNotExistRepository searchResultsIfBookingsDoNotExistRepository;

    @Autowired
    private SearchResultsIfBookingsExistRepository searchResultsIfBookingsExistRepository;

    @Transactional
    @RequestMapping(value = "/result-search", method = {RequestMethod.POST, RequestMethod.GET})
    public String resultSearchBookings(
            @RequestParam("location") String location,
            @RequestParam("pickupDateTime") String pickupDateTimeStr,
            @RequestParam("dropOffDateTime") String dropOffDateTimeStr,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "1") int pageSize,
            @RequestParam(value = "sortOption", defaultValue = "newest") String sortOption,
            @RequestParam(value = "displayType", defaultValue = "1") String displayType,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "home/Homepage_Customer";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime pickupDateTime = LocalDateTime.parse(pickupDateTimeStr, formatter);
        LocalDateTime dropOffDateTime = LocalDateTime.parse(dropOffDateTimeStr, formatter);

        if (dropOffDateTime.isBefore(pickupDateTime)) {
            model.addAttribute("errorMessage", "Drop-off date time must be later than pick-up date time, please try again.");
            return "layout/customer/ResultView";
        }

        IdealCar idealCar = new IdealCar();
        idealCar.setLocation(location);
        idealCar.setPickupDateTime(pickupDateTime);
        idealCar.setDropOffDateTime(dropOffDateTime);
        idealCarRepository.save(idealCar);

        Sort sort = getSortOption(sortOption);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<Car> page = null;
        List<Car> carList = carRepository.findByCarStatus(CarStatus.Available);

        for (Car car : carList) {
            if (car.getBookings().isEmpty()) {
                page = searchResultsIfBookingsDoNotExistRepository.findCarsByIdealCar(location, pageable);
            } else {
                page = searchResultsIfBookingsExistRepository.findBookingsByIdealCarAndAvailableStatus(location, pickupDateTime, pageable);
            }
            car.setIdealCar(idealCar);
            carRepository.save(car);
        }

        if (page == null || page.isEmpty()) {
            model.addAttribute("errorMessage", "No cars match your search criteria. Please try adjusting your input.");
            return "layout/customer/ResultView";
        }

        long carsAvailable = page.getTotalElements();

        List<Integer> numberOfRides = new ArrayList<>();
        for (Car car : page) {
            Integer numberOfRide = bookingRepository.countByCarAndStatus(car, BookingStatus.Completed);
            numberOfRides.add(numberOfRide);
        }

        int totalPages = page.getTotalPages();
        List<Integer> pageNums = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNums.add(i);
        }

        model.addAttribute("carsAvailable", carsAvailable);
        model.addAttribute("numberOfRides", numberOfRides);
        model.addAttribute("pickupDate", idealCar.getPickupDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        model.addAttribute("pickupTime", idealCar.getPickupDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        model.addAttribute("dropOffDate", idealCar.getDropOffDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        model.addAttribute("dropOffTime", idealCar.getDropOffDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        model.addAttribute("pageNums", pageNums);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("cars", page.getContent());
        model.addAttribute("sortOption", sortOption);
        model.addAttribute("displayType", displayType);
        model.addAttribute("location", location);
        model.addAttribute("pickupDateTime", pickupDateTimeStr);
        model.addAttribute("dropOffDateTime", dropOffDateTimeStr);

        return "layout/customer/ResultView";
    }

    public Sort getSortOption(String sortOption) {
        return switch (sortOption) {
            case "oldest" -> Sort.by(Sort.Direction.ASC, "startDateTime");
            case "priceLowHigh" -> Sort.by(Sort.Direction.ASC, "car.basePrice");
            case "priceHighLow" -> Sort.by(Sort.Direction.DESC, "car.basePrice");
            default -> Sort.by(Sort.Direction.DESC, "startDateTime");
        };
    }
}




