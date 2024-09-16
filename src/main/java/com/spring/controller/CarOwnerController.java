package com.spring.controller;

import com.spring.entities.*;
import com.spring.repository.BookingRepository;
import com.spring.repository.CarOwnerRepository;
import com.spring.repository.CarRepository;
import com.spring.repository.FeedBackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class CarOwnerController {
    private static final String UPLOAD_FILE = "D:\\FPT Soft Learning\\IT_Java_FullStack\\JWD\\Assignment\\Mock Project\\Toan\\src\\main\\resources\\static\\documents";
    private static final String UPLOAD_IMAGES = "D:\\FPT Soft Learning\\IT_Java_FullStack\\JWD\\Assignment\\Mock Project\\Toan\\src\\main\\resources\\static\\images";

    @Autowired
    private CarOwnerRepository carOwnerRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FeedBackRepository feedBackRepository;

    @GetMapping(value = {"/"})
    public String home(Model model) {
        return "layout/Car_Owner/Homepage_LoggedIn_CarOwner";
    }

    @GetMapping(value = "/mycar/add")
    public String addCar(Model model) {
        return "layout/Car_Owner/AddCar";
    }

    @PostMapping("/mycar/add")
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
        if (!homeNumber.equals("")) {
            address += ", " + homeNumber;
        }
        String carName = brand + " " + model;

        String carFunction = "";
        if (functions != null) {
            carFunction = functions.stream().collect(Collectors.joining(", "));
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
            String termString = Arrays.stream(termArray)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            carTerm = termString;
        }

        if (!otherTerm.equals("")) {
            carTerm += "_" + otherTerm;
        }

        BigDecimal carMileage = new BigDecimal(mileage);

        BigDecimal carFuelConsumption = null;
        if (!fuelConsumption.equals("")) {
            carFuelConsumption = new BigDecimal(fuelConsumption);
        }

        BigDecimal carBasePrice = null;
        if (!basePrice.equals("")) {
            carBasePrice = new BigDecimal(basePrice);
        }

        BigDecimal carDeposit = null;
        if (!deposit.equals("")) {
            carDeposit = new BigDecimal(deposit);
        }

        String carImages = "";
        MultipartFile[] files = {file0, file1, file2, file3, file4, file5, file6};
        for (int i = 0; i < files.length; i++) {
            if (i <= 2) {
                carImages = carImages + upFiles(files[i]) + ", ";
            } else if (i < 6) {
                carImages = carImages + upImages(files[i]) + ", ";
            } else {
                carImages = carImages + upImages(files[i]);
            }
        }
        CarOwner carOwner = carOwnerRepository.findById(1).orElse(null);
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
                .images(carImages)
                .carStatus(CarStatus.Available)
                .carOwner(carOwner)
                .build();

        carRepository.save(car);
        return "Success";
    }


    @GetMapping(value = "/mycar")
    public String listCar(Model model,
                          @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
                          @RequestParam(value = "number", defaultValue = "3") Integer number,
                          @RequestParam(value = "sort", defaultValue = "1") Integer sort
    ) {
        CarOwner carOwner = carOwnerRepository.findById(1).orElse(null);
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
        List<Double> ratings = new ArrayList<>();
        List<Booking> bookings = new ArrayList<>();

        for (Car car : carPage) {
            Integer numberOfRide = bookingRepository.countByCarAndStatus(car, BookingStatus.Completed);
            numberOfRides.add(numberOfRide);

            Double rate = rating(car.getId());

            ratings.add(rate);

            Booking booking = booking(car);
            bookings.add(booking);
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
        model.addAttribute("rate", ratings);
        model.addAttribute("bookings", bookings);

        return "layout/Car_Owner/List";
    }

    @GetMapping(value = "mycar/edit")
    public String showEditCar(Model model,
                              @RequestParam("carid") Integer carId
    ) {
        boolean authorized = validateRole(carId);
        if (!authorized) {
            return "redirect:/mycar";
        } else {
            Car car = carRepository.findById(carId).orElse(null);
            Double rate = rating(car.getId());
            Integer numberOfRide = bookingRepository.countByCarAndStatus(car, BookingStatus.Completed);
            Booking booking = booking(car);

            model.addAttribute("car", car);
            model.addAttribute("rate", rate);
            model.addAttribute("numberOfRide", numberOfRide);
            model.addAttribute("booking", booking);
            return "layout/Car_Owner/Edit";
        }
    }

    @PostMapping("mycar/edit")
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
                return "redirect:/mycar";
            } else {
                carPayment.setCarStatus(CarStatus.Available);
                carRepository.save(carPayment);
                changeStatus(payment, bookingRepository, BookingStatus.Completed);
                return "redirect:/mycar/edit?carid=" + carPayment.getId();
            }
        } else {
            Booking booking = bookingRepository.findById(deposit).orElse(null);
            Car carDeposit = booking.getCar();
            authorized = validateRole(carDeposit.getId());
            if (!authorized) {
                return "redirect:/mycar";
            } else {
                changeStatus(deposit, bookingRepository, BookingStatus.Confirmed);
                return "redirect:/mycar/edit?carid=" + carDeposit.getId();
            }
        }
    }

    @PostMapping("/mycar/editdetails")
    @ResponseBody
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
            if (!fuelConsumption.equals("")) {
                carFuelConsumption = new BigDecimal(fuelConsumption);
            }
            MultipartFile[] files = {file3, file4, file5, file6};
            String[] imagesArray = car.getImages().split(", ");

            for (int i = 3; i <= 6; i++) {
                if (files[i - 3] != null) {
                    Path path = Paths.get("src/main/resources/static", imagesArray[i]);
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

    private String upFiles(MultipartFile file) {
        try {
            // Lưu tệp vào thư mục chỉ định
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); //unique name
            String uploadFilePath = UPLOAD_FILE + File.separator + uniqueFileName;
            file.transferTo(new File(uploadFilePath));
            return "/documents/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String upImages(MultipartFile file) {
        try {
            // Lưu tệp vào thư mục chỉ định
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); //unique name
            String uploadFilePath = UPLOAD_IMAGES + File.separator + uniqueFileName;
            file.transferTo(new File(uploadFilePath));
            return "/images/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private int switchToInt(String enumValue) {
        switch (enumValue) {
            case "one_star":
                return 1;
            case "two_stars":
                return 2;
            case "three_stars":
                return 3;
            case "four_stars":
                return 4;
            case "five_stars":
                return 5;
            default:
                return 0;
        }
    }

    private Double rating(Integer carId) {
        List<String> ratingList = feedBackRepository.findALLStarByCarID(carId);

        Double rating = 0.0;

        for (String ratingValue : ratingList) {
            rating += switchToInt(ratingValue);
        }
        if (ratingList.size() > 0) {
            rating = (double) rating / ratingList.size();
        }
        return Math.ceil(rating * 2) / 2.0;
    }

    private Booking booking(Car car) {
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
//        TODO: update security
        CarOwner carOwner = carOwnerRepository.findById(1).orElse(null);
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
}

