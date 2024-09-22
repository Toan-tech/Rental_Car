package com.spring.controller.customer;

import com.spring.entities.*;
import com.spring.repository.*;
import com.spring.service.BookingService;
import com.spring.service.CarService;
import com.spring.service.EmailService;
import com.spring.service.EmailServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
public class CustomerController {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private IdealCarRepository idealCarRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/ResultView")
    public String showResultView(Model model) {
        model.addAttribute("booking", new Booking());
        model.addAttribute("car", new Car());
        return "layout/customer/ResultView";
    }

    @GetMapping("/viewDetails")
    public String viewBookingDetails(@RequestParam("carId") Integer carId, Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();

            Customer customer = customerRepository.findCustomerByEmail(email);

            if (customer != null) {
                model.addAttribute("loggedIn", true);
            } else {
                model.addAttribute("loggedIn", false);
            }
        }

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
    public String viewDetails(@RequestParam("carId") Integer carId, Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();

            Customer customer = customerRepository.findCustomerByEmail(email);

            if (customer != null) {
                model.addAttribute("loggedIn", true);
            } else {
                model.addAttribute("loggedIn", false);
            }
        }

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
    public String termView(@RequestParam("carId") Integer carId, Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();

            Customer customer = customerRepository.findCustomerByEmail(email);

            if (customer != null) {
                model.addAttribute("loggedIn", true);
            } else {
                model.addAttribute("loggedIn", false);
            }
        }

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
                                  Model model,
                                  Principal principal) {
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
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        long numberOfDays = ChronoUnit.DAYS.between(idealCar.getPickupDateTime().toLocalDate(), idealCar.getDropOffDateTime().toLocalDate()) + 1;

        String email = principal.getName();
        Customer customer = customerRepository.findCustomerByEmail(email);

        String address = customer.getAddress();
        if (address != null) {
            String[] addressParts = address.split(", ");
            if (addressParts.length == 4) {
                model.addAttribute("houseNumber", addressParts[0]);
                model.addAttribute("ward", addressParts[1]);
                model.addAttribute("district", addressParts[2]);
                model.addAttribute("city", addressParts[3]);
            }
        }

        model.addAttribute("car", car);
        model.addAttribute("numberOfDays", numberOfDays);
        model.addAttribute("booking", foundBooking);
        model.addAttribute("pickupDate", idealCar.getPickupDateTime().toLocalDate().format(dateFormatter));
        model.addAttribute("pickupTime", idealCar.getPickupDateTime().toLocalTime().format(timeFormatter));  // Định dạng giờ có AM/PM
        model.addAttribute("dropoffDate", idealCar.getDropOffDateTime().toLocalDate().format(dateFormatter));
        model.addAttribute("dropoffTime", idealCar.getDropOffDateTime().toLocalTime().format(timeFormatter));  // Định dạng giờ có AM/PM
        model.addAttribute("idealCar", idealCar);
        model.addAttribute("customerBookingInformation", customer);

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
                                RedirectAttributes redirectAttributes,
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

        // Check car availability
        boolean isCarAvailable = carService.isCarAvailable(carId, pickupLocation, pickupDate, pickupTime, dropoffDate, dropoffTime);

        if (isCarAvailable) {
            model.addAttribute("carNotAvailable", false);
        } else {
            model.addAttribute("carNotAvailable", true);
            model.addAttribute("carId", carId);
            return "layout/customer/edit/EditBooking";
        }

        idealCar.setLocation(pickupLocation);
        idealCar.setPickupDateTime(LocalDateTime.of(pickupDate, pickupTime));
        idealCar.setDropOffDateTime(LocalDateTime.of(dropoffDate, dropoffTime));

        idealCarRepository.save(idealCar);

        redirectAttributes.addFlashAttribute("successMessage", "Booking details updated successfully!");

        return "redirect:" + UriComponentsBuilder.fromPath("/Overview")
                .queryParam("carId", carId)
                .build().toUriString();
    }


    @PostMapping("/Overview")
    public String updateRenterInformation(Principal principal,
                                          @RequestParam("carId") Integer carId,
                                          @RequestParam("name") String name,
                                          @RequestParam("dateOfBirth") LocalDate dateOfBirth,
                                          @RequestParam("phoneNo") String phoneNo,
                                          @RequestParam("email") String email,
                                          @RequestParam("nationalIdNo") String nationalIdNo,
                                          @RequestParam("houseNumber") String houseNumber,
                                          @RequestParam("city") String city,
                                          @RequestParam("district") String district,
                                          @RequestParam("ward") String ward,
                                          @RequestParam(value = "drivingLicense", required = false) MultipartFile drivingLicense) {
        if (principal != null) {
            String userEmail = principal.getName();
            Customer customer = customerRepository.findCustomerByEmail(userEmail);

            if (customer != null) {
                String fullAddress = String.format("%s, %s, %s, %s", houseNumber, ward, district, city);

                // Update customer information
                customer.setName(name);
                customer.setDateOfBirth(dateOfBirth);
                customer.setPhoneNo(phoneNo);
                customer.setNationalIdNo(nationalIdNo);
                customer.setAddress(fullAddress);

                if (drivingLicense != null && !drivingLicense.isEmpty()) {
                    if (customer.getDrivingLicense() != null) {
                        deleteFile(customer.getDrivingLicense());
                    }
                    String drivingLicensePath = saveFile(drivingLicense);
                    customer.setDrivingLicense(drivingLicensePath);
                }
                customerRepository.save(customer);

                // Fetch car information and ideal car related to the booking
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

                // Create a new Booking
                Booking booking = new Booking();
                booking.setDriverInfo(idealCar.getLocation());
                booking.setStartDateTime(idealCar.getPickupDateTime());
                booking.setEndDateTime(idealCar.getDropOffDateTime());
                booking.setCar(car);
                booking.setCustomer(customer);
                booking.setIdealCar(idealCar);
                bookingService.createBooking(booking);

                // Save the booking
                bookingRepository.save(booking);
            }
        }
        // Redirect to Booking Payment page after updating
        return "redirect:/BookingPayment?carId=" + carId;
    }

    @GetMapping("/BookingPayment")
    public String bookingPayment(@RequestParam("carId") Integer carId,
                                 Principal principal,
                                 Model model) {
        String email = principal.getName();
        Customer customer = customerRepository.findCustomerByEmail(email);
        model.addAttribute("customer", customer);

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
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        long numberOfDays = ChronoUnit.DAYS.between(idealCar.getPickupDateTime().toLocalDate(), idealCar.getDropOffDateTime().toLocalDate()) + 1;

        model.addAttribute("car", car);
        model.addAttribute("numberOfDays", numberOfDays);
        model.addAttribute("booking", foundBooking);
        model.addAttribute("pickupDate", idealCar.getPickupDateTime().toLocalDate().format(dateFormatter));
        model.addAttribute("pickupTime", idealCar.getPickupDateTime().toLocalTime().format(timeFormatter));  // Định dạng giờ có AM/PM
        model.addAttribute("dropoffDate", idealCar.getDropOffDateTime().toLocalDate().format(dateFormatter));
        model.addAttribute("dropoffTime", idealCar.getDropOffDateTime().toLocalTime().format(timeFormatter));  // Định dạng giờ có AM/PM
        model.addAttribute("idealCar", idealCar);

        return "layout/customer/BookingPayment";
    }

    @PostMapping("/BookingPayment")
    public String submitBookingPayment(@RequestParam("carId") Integer carId,
                                       @RequestParam("paymentMethod") PaymentMethod paymentMethod,
                                       Principal principal,
                                       Model model
    ) {
        try {
            String email = principal.getName();
            Customer customer = customerRepository.findCustomerByEmail(email);
            model.addAttribute("customer", customer);

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

            long numberOfDays = ChronoUnit.DAYS.between(idealCar.getPickupDateTime().toLocalDate(), idealCar.getDropOffDateTime().toLocalDate()) + 1;

            Car car = foundBooking.getCar();
            BigDecimal basePrice = car.getBasePrice();
            BigDecimal deposit = car.getDeposit();
            BigDecimal total = basePrice.multiply(BigDecimal.valueOf(numberOfDays)).setScale(2, RoundingMode.HALF_UP);

            if (deposit.compareTo(total) > 0) {
                // Refund excess amount to wallet
                BigDecimal excessAmount = deposit.subtract(total);
                customer.setWallet(customer.getWallet().add(excessAmount));
                car.setDeposit(total);
            } else {
                // Handle payment via selected method
                if (paymentMethod == PaymentMethod.My_Wallet) {
                    if (customer.getWallet().compareTo(total) >= 0) {
                        // Deduct total amount from wallet
                        customer.setWallet(customer.getWallet().subtract(total));
                        foundBooking.setStatus(BookingStatus.Confirmed);
                    } else {
                        model.addAttribute("error", "Insufficient balance. Please top up your wallet.");
                        return "redirect:/BookingPayment?carId=" + carId;
                    }
                } else {
                    // For Cash and Bank Transfer, set status to "Pending deposit"
                    foundBooking.setStatus(BookingStatus.Pending_Deposit);
                }
            }

            // Set the payment method
            foundBooking.setPaymentMethod(paymentMethod);

            // Save the updated booking and customer
            bookingRepository.save(foundBooking);
            customerRepository.save(customer);

            model.addAttribute("message", "Payment processed successfully");

            String carName = foundBooking.getCar().getName();
            String bookingDate = foundBooking.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            // Send the email
            emailService.sendSimpleMailMessage(carName, bookingDate, email);
            model.addAttribute("messageEmail", "We've sent a notification to your email");
        } catch (Exception e) {
            model.addAttribute("error", "Your email was not found");
            e.printStackTrace();
        }
        return "redirect:/bookingSuccess?carId=" + carId;
    }

    @GetMapping("/bookingSuccess")
    public String bookingSuccess(@RequestParam("carId") Integer carId,
                                 Principal principal,
                                 Model model
    ) {
        String email = principal.getName();
        Customer customer = customerRepository.findCustomerByEmail(email);
        model.addAttribute("customer", customer);

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
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        long numberOfDays = ChronoUnit.DAYS.between(idealCar.getPickupDateTime().toLocalDate(), idealCar.getDropOffDateTime().toLocalDate()) + 1;

        model.addAttribute("car", car);
        model.addAttribute("numberOfDays", numberOfDays);
        model.addAttribute("booking", foundBooking);
        model.addAttribute("pickupDate", idealCar.getPickupDateTime().toLocalDate().format(dateFormatter));
        model.addAttribute("pickupTime", idealCar.getPickupDateTime().toLocalTime().format(timeFormatter));  // Định dạng giờ có AM/PM
        model.addAttribute("dropoffDate", idealCar.getDropOffDateTime().toLocalDate().format(dateFormatter));
        model.addAttribute("dropoffTime", idealCar.getDropOffDateTime().toLocalTime().format(timeFormatter));  // Định dạng giờ có AM/PM
        model.addAttribute("idealCar", idealCar);

        return "layout/customer/FinishPayment";
    }


    private String saveFile(MultipartFile file) {
        try {
            String uploadDir = new File("src/main/resources/static/images/driving-license").getAbsolutePath();

            String fileName = file.getOriginalFilename();
            File destinationFile = new File(uploadDir + File.separator + fileName);

            file.transferTo(destinationFile);

            return "images/driving-license/" + fileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void deleteFile(String filePath) {
        try {
            String absolutePath = new File("src/main/resources/static/" + filePath).getAbsolutePath();
            File file = new File(absolutePath);

            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
