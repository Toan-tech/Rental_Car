package com.spring.rest;

import com.spring.entities.*;
import com.spring.repository.BookingRepository;
import com.spring.repository.CarRepository;
import com.spring.service.CarOwnerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class EditStatus {
    private static final String UPLOAD_IMAGES = "D:\\Code\\MockProject-toan\\src\\main\\resources\\static\\images\\car_owner";
    private static final String UPLOAD_FILE = "D:\\Code\\MockProject-toan\\src\\main\\resources\\static\\documents\\car_owner";

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarOwnerServiceImpl carOwnerServiceImpl;

    @PostMapping("/car-owner/bookingstatus")
    public String bookStatus(
            @RequestParam(value = "payment", required = false) Integer payment,
            @RequestParam(value = "deposit", required = false) Integer deposit
    ) {
        boolean authorized = false;
        if (payment != null) {
            Booking booking = bookingRepository.findById(payment).orElse(null);
            Car car = booking.getCar();
            authorized = validateRole(car.getId());
            if (!authorized) {
                return "You are not authorized to perform this action";
            } else {
                car.setCarStatus(CarStatus.Available);
                carRepository.save(car);
                changeStatus(payment, bookingRepository, BookingStatus.Completed);
            }
        } else {
            Booking booking = bookingRepository.findById(deposit).orElse(null);
            Car car = booking.getCar();
            authorized = validateRole(car.getId());
            if (!authorized) {
                return "You are not authorized to perform this action";
            } else {
                changeStatus(deposit, bookingRepository, BookingStatus.Confirmed);
            }
        }
        return "Authorized validated";
    }

    @PostMapping("/car-owner/mycar/editdetails")
    public String editCarDetails(
            @RequestParam("carId") Integer carId,
            @RequestParam("mileage") String mileage,
            @RequestParam("fuelConsumption") String fuelConsumption,
            @RequestParam("address") String address,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "additionalFunction", required = false) String additionalFunction,
            @RequestParam("terms") String terms,
            @RequestParam("basePrice") String basePrice,
            @RequestParam("deposit") String deposit,
            @RequestParam(value = "carStatus", required = false) String status,
            @RequestParam(value = "file3", required = false) MultipartFile file3,
            @RequestParam(value = "file4", required = false) MultipartFile file4,
            @RequestParam(value = "file5", required = false) MultipartFile file5,
            @RequestParam(value = "file6", required = false) MultipartFile file6
    ) throws IOException {
        boolean authorized = validateRole(carId);
        if (!authorized) {
            return "unauthorized";
        } else {
            Car car = carRepository.findById(carId).orElse(null);
            System.out.println(mileage);
            BigDecimal carMileage = new BigDecimal(mileage);
            BigDecimal carBasePrice = new BigDecimal(basePrice);
            BigDecimal carDeposit = new BigDecimal(deposit);
            BigDecimal carFuelConsumption = null;
            if (!fuelConsumption.isEmpty()) {
                carFuelConsumption = new BigDecimal(fuelConsumption);
            }
            MultipartFile[] files = {file3, file4, file5, file6};
            String[] imagesArray = car.getImages().split(", ");

            for (int i = 3; i <= 6; i++) {
                if (files[i - 3] != null) {
                    Path path = Paths.get("src/main/resources", imagesArray[i]);
                    if (Files.exists(path)) {
                        Files.delete(path);
                    }
                    imagesArray[i] = upImages(files[i - 3]);
                }
            }

            String carImages = String.join(", ", imagesArray);
            CarStatus carStatus;
            if (status.equals("undefined")) {
                carStatus = CarStatus.Booked;
            } else {
                carStatus = CarStatus.valueOf(status);
            }

            car.setMileage(carMileage);
            car.setBasePrice(carBasePrice);
            car.setDeposit(carDeposit);
            car.setFuelConsumption(carFuelConsumption);
            car.setImages(carImages);
            car.setAddress(address);
            car.setDescription(description);
            car.setAdditionalFunctions(additionalFunction);
            car.setTermsOfUse(terms);
            car.setCarStatus(carStatus);
            carRepository.save(car);
            return "Success";
        }
    }

    @PostMapping("/car-owner/mycar/add")
    @ResponseBody
    public String addCar(@RequestParam("file0") MultipartFile file0,
                         @RequestParam("file1") MultipartFile file1,
                         @RequestParam("file2") MultipartFile file2,
                         @RequestParam("file3") MultipartFile file3,
                         @RequestParam("file4") MultipartFile file4,
                         @RequestParam("file5") MultipartFile file5,
                         @RequestParam("file6") MultipartFile file6,
                         @RequestParam("license") String license,
                         @RequestParam("color") String color,
                         @RequestParam("brand") String brand,
                         @RequestParam("model") String model,
                         @RequestParam("production-year") Integer productionYear,
                         @RequestParam("seat-number") Integer seatNumber,
                         @RequestParam("transmission") String transmission,
                         @RequestParam("fuel") String fuel,
                         @RequestParam("mileage") String mileage,
                         @RequestParam("city") String city,
                         @RequestParam("district") String district,
                         @RequestParam("ward") String ward,
                         @RequestParam(value = "home-number", required = false) String homeNumber,
                         @RequestParam(value = "fuel-consumption", required = false) String fuelConsumption,
                         @RequestParam(value = "description", required = false) String description,
                         @RequestParam(value = "function", required = false) List<String> functions,
                         @RequestParam(value = "base-price", required = false) String basePrice,
                         @RequestParam(value = "deposit", required = false) String deposit,
                         @RequestParam(value = "term", required = false) List<Integer> terms,
                         @RequestParam(value = "otherterm", required = false) String otherTerm
    ) {
        String address = city + ", " + district + ", " + ward;
        if (!homeNumber.isEmpty()) {
            address += ", " + homeNumber;
        }
        String carName = brand + " " + model;

        String carFunction = "";
        if (functions != null) {
            carFunction = String.join(", ", functions);
        }

        String carTerm = "";
        if (terms != null) {
            Integer[] termArray = {0, 0, 0, 0};
            for (int i = 0; i < termArray.length; i++) {
                for (Integer j : terms) {
                    if ((i + 1) == j) {
                        termArray[i] = j;
                        break;
                    }
                }
            }
            carTerm = Arrays.stream(termArray)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
        }

        if (!otherTerm.isEmpty()) {
            carTerm += "_" + otherTerm;
        }

        BigDecimal carMileage = new BigDecimal(mileage);

        BigDecimal carFuelConsumption = null;
        if (!fuelConsumption.isEmpty()) {
            carFuelConsumption = new BigDecimal(fuelConsumption);
        }

        BigDecimal carBasePrice = null;
        if (!basePrice.isEmpty()) {
            carBasePrice = new BigDecimal(basePrice);
        }

        BigDecimal carDeposit = null;
        if (!deposit.isEmpty()) {
            carDeposit = new BigDecimal(deposit);
        }

        StringBuilder carImages = new StringBuilder();
        MultipartFile[] files = {file0, file1, file2, file3, file4, file5, file6};
        for (int i = 0; i < files.length; i++) {
            if (i <= 2) {
                carImages.append(upFiles(files[i])).append(", ");
            } else if (i < 6) {
                carImages.append(upImages(files[i])).append(", ");
            } else {
                carImages.append(upImages(files[i]));
            }
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CarOwner carOwner = carOwnerServiceImpl.findByEmail(user.getUsername());
        Car car = Car.builder()
                .additionalFunctions(carFunction)
                .address(address)
                .basePrice(carBasePrice)
                .brand(brand)
                .color(color)
                .deposit(carDeposit)
                .description(description)
                .fuelConsumption(carFuelConsumption)
                .fuelType(fuel)
                .licensePlate(license)
                .mileage(carMileage)
                .model(model)
                .name(carName)
                .numberOfSeats(seatNumber)
                .productionYears(productionYear)
                .termsOfUse(carTerm)
                .transmissionType(transmission)
                .images(carImages.toString())
                .carStatus(CarStatus.Available)
                .carOwner(carOwner)
                .build();

        carRepository.save(car);
        return "Success";
    }

    private void changeStatus(Integer bookingId, BookingRepository bookingRepository, BookingStatus bookingStatus) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        booking.setStatus(bookingStatus);
        bookingRepository.save(booking);
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

    private String upImages(MultipartFile file) {
        try {
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String uploadFilePath = UPLOAD_IMAGES + File.separator + uniqueFileName;
            file.transferTo(new File(uploadFilePath));
            return "/static/images/car_owner/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String upFiles(MultipartFile file) {
        try {
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String uploadFilePath = UPLOAD_FILE + File.separator + uniqueFileName;
            file.transferTo(new File(uploadFilePath));
            return "/static/documents/car_owner/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
