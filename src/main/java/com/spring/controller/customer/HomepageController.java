package com.spring.controller.customer;

import com.spring.entities.Booking;
import com.spring.entities.Car;
import com.spring.entities.IdealCar;
import com.spring.repository.BookingRepository;
import com.spring.repository.CarRepository;
import com.spring.repository.IdealCarRepository;
import com.spring.service.BookingService;
import com.spring.service.CarService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class HomepageController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CarService carService;

    @Autowired
    private IdealCarRepository idealCarRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Transactional
    @RequestMapping(value = "/result-search", method = {RequestMethod.POST, RequestMethod.GET})
    public String resultSearchBookings(@ModelAttribute("booking") Booking booking,
                                       @ModelAttribute("car") Car car,
                                       @ModelAttribute(name = "idealCar") IdealCar idealCar,
                                       @RequestParam("location") String location,
                                       @RequestParam("pickupDateTime") String pickupDateTimeStr,
                                       @RequestParam("dropOffDateTime") String dropOffDateTimeStr,
                                       @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
                                       @RequestParam(value = "size", defaultValue = "1") int pageSize,
                                       @RequestParam(value = "sortOption", defaultValue = "newest") String sortOption,
                                       @RequestParam(value = "displayType", defaultValue = "1") String displayType,
                                       BindingResult result,
                                       Model model) {

        if (result.hasErrors()) {
            return "layout/customer/ResultView";
        }

        // Convert datetime strings to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime pickupDateTime = LocalDateTime.parse(pickupDateTimeStr, formatter);
        LocalDateTime dropOffDateTime = LocalDateTime.parse(dropOffDateTimeStr, formatter);

        // Ensure drop-off time is after pickup time
        if (dropOffDateTime.isBefore(pickupDateTime)) {
            model.addAttribute("errorMessage", "Drop-off date time must be later than pick-up date time, please try again.");
            return "layout/customer/ResultView";
        }

        // Create and save IdealCar
        idealCar = new IdealCar();
        idealCar.setLocation(location);
        idealCar.setPickupDateTime(pickupDateTime);
        idealCar.setDropOffDateTime(dropOffDateTime);
        idealCarRepository.save(idealCar);

        List<Booking> bookingList = bookingRepository.findAll();
        for (Booking eachBooking : bookingList) {
            eachBooking.setIdealCar(idealCar);
            bookingRepository.save(eachBooking);
        }

        // Fetch bookings based on search criteria
        Page<Booking> page = bookingService.findBookings(location, pickupDateTime, dropOffDateTime, sortOption, pageNumber, pageSize);

        if (page.isEmpty()) {
            model.addAttribute("errorMessage", "No cars match your search criteria. Please try adjusting your input.");
            return "layout/customer/Homepage";
        }

        List<Car> cars = carService.extractDistinctAvailableCars(page.getContent());
        Page<Car> pagedCars = new PageImpl<>(cars, PageRequest.of(pageNumber - 1, pageSize), cars.size());

        List<Car> carsItem = carService.extractDistinctAvailableCars(bookingService.findNumberOfCars(location, pickupDateTime, dropOffDateTime));

        long numberOfCar = cars.size();
        // Count the total number of cars in the result
        long carAvailable = carsItem.size();

        // Completed rides
        List<Booking> completedBookings = bookingService.findCompletedBookings();
        int completedRides = 0;
        for (Map.Entry<Car, Integer> entry : carService.countCompletedRides(completedBookings).entrySet()) {
            if (entry.getValue() >= 1) {
                completedRides += entry.getValue();
            }
        }

        // Get feedback ratings
        Map<Car, Double> carRatings = new HashMap<>();
        List<Car> listCarRatings = carRepository.findAll();
        for (Car carItem : listCarRatings) {
            double averageRating = carService.calculateAverageRating(car.getId());
            carRatings.put(carItem, averageRating);
        }

        // Set model attributes for view
        model.addAttribute("NumberOfCar", numberOfCar);
        model.addAttribute("CarAvailable", carAvailable);
        model.addAttribute("CarCount", completedRides);
        model.addAttribute("carRatings", carRatings);
        model.addAttribute("pickupDate", idealCar.getPickupDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        model.addAttribute("pickupTime", idealCar.getPickupDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        model.addAttribute("dropoffDate", idealCar.getDropOffDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        model.addAttribute("dropoffTime", idealCar.getDropOffDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        model.addAttribute("cars", pagedCars.getContent());
        model.addAttribute("carsItem", carsItem);
        model.addAttribute("pageNums", IntStream.rangeClosed(1, page.getTotalPages()).boxed().collect(Collectors.toList()));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("page", pagedCars);
        model.addAttribute("bookings", page.getContent());
        model.addAttribute("sortOption", sortOption);
        model.addAttribute("displayType", displayType);
        model.addAttribute("idealCar", idealCar);
        model.addAttribute("location", location);
        model.addAttribute("pickupDateTime", pickupDateTimeStr);
        model.addAttribute("dropOffDateTime", dropOffDateTimeStr);

        return "layout/customer/ResultView";
    }
}
