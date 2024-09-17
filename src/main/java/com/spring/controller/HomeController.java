package com.spring.controller;

import com.spring.entities.CarOwner;
import com.spring.entities.Customer;
import com.spring.entities.Users;
import com.spring.repository.CarOwnerRepository;
import com.spring.repository.CustomerRepository;
import com.spring.repository.UsersRepository;
import com.spring.service.CarOwnerService;
import com.spring.service.CustomerService;
import com.spring.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;

@Controller
public class HomeController {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CarOwnerRepository carOwnerRepository;

    @Autowired
    UsersService usersService;

    @Autowired
    CustomerService customerService;

    @Autowired
    CarOwnerService carOwnerService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("/home-page")
    public String homePage(Model model) {
        return "layout/home/home-page-as-guest";
    }

    @GetMapping("/Homepage")
    public String homePageAsCustomer() {
        return "layout/customer/Homepage";
    }

    @GetMapping("/car-owner")
    public String homePageAsCarOwner() {
        return "layout/home/home-page-as-car-owner";
    }

    @RequestMapping("/home")
    public String defaultAfterLogin(HttpServletRequest request) {
        if (request.isUserInRole("Customer")) {
            return "redirect:/customer";
        } else if (request.isUserInRole("Car_Owner")) {
            return "redirect:/car-owner";
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/my-profile")
    public String userProfile(HttpSession session, Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();

            Customer customer = customerRepository.findCustomerByEmail(email);
            CarOwner carOwner = carOwnerRepository.findCarOwnerByEmail(email);

            if (customer != null) {
                model.addAttribute("profile", customer);

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
            } else if (carOwner != null) {
                model.addAttribute("profile", carOwner);

                String address = carOwner.getAddress();
                if (address != null) {
                    String[] addressParts = address.split(", ");
                    if (addressParts.length == 4) {
                        model.addAttribute("houseNumber", addressParts[0]);
                        model.addAttribute("ward", addressParts[1]);
                        model.addAttribute("district", addressParts[2]);
                        model.addAttribute("city", addressParts[3]);
                    }
                }
            } else {
                return "redirect:/login";
            }
            return "layout/home/my-profile";
        }
        return "redirect:/login";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(Principal principal,
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

            Users users = usersRepository.findUsersByEmail(userEmail);
            Customer customer = customerRepository.findCustomerByEmail(userEmail);
            CarOwner carOwner = carOwnerRepository.findCarOwnerByEmail(userEmail);

            String fullAddress = String.format("%s, %s, %s, %s", houseNumber, ward, district, city);

            if (customer != null) {
                customer.setName(name);
                customer.setDateOfBirth(dateOfBirth);
                customer.setPhoneNo(phoneNo);
                customer.setEmail(email);
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

                if (users != null) {
                    users.setName(name);
                    users.setPhone(phoneNo);
                    usersRepository.save(users);
                }

            } else if (carOwner != null) {
                carOwner.setName(name);
                carOwner.setDateOfBirth(dateOfBirth);
                carOwner.setPhoneNo(phoneNo);
                carOwner.setEmail(email);
                carOwner.setNationalIdNo(nationalIdNo);
                carOwner.setAddress(fullAddress);

                if (drivingLicense != null && !drivingLicense.isEmpty()) {
                    if (carOwner.getDrivingLicense() != null) {
                        deleteFile(carOwner.getDrivingLicense());
                    }
                    String drivingLicensePath = saveFile(drivingLicense);
                    carOwner.setDrivingLicense(drivingLicensePath);
                }

                carOwnerRepository.save(carOwner);

                if (users != null) {
                    users.setName(name);
                    users.setPhone(phoneNo);
                    usersRepository.save(users);
                }
            }

            return "redirect:/my-profile";
        }

        return "redirect:/login";
    }

    @PostMapping("/changePassword")
    public String changePassword(Principal principal,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {

        if (principal != null) {
            String userEmail = principal.getName();

            Users user = usersRepository.findUsersByEmail(userEmail);

            if (user == null) {
                model.addAttribute("error", "User not found.");
                return "redirect:/login";
            }

            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match.");
                return "redirect:/changePassword";
            }

            String encodedPassword = passwordEncoder.encode(newPassword);

            user.setPassword(encodedPassword);
            usersRepository.save(user);

            return "redirect:/login?passwordChanged";
        }

        return "redirect:/login";
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
