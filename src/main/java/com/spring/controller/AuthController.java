package com.spring.controller;

import com.spring.entities.CarOwner;
import com.spring.entities.Customer;
import com.spring.entities.RoleEnum;
import com.spring.entities.Users;
import com.spring.model.UsersDTO;
import com.spring.repository.UsersRepository;
import com.spring.service.CarOwnerService;
import com.spring.service.CustomerService;
import com.spring.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class AuthController {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersService usersService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CarOwnerService carOwnerService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("users", new Users());
        return "auth/auth-page";
    }

    @PostMapping("/auth")
    public String registerForm(@ModelAttribute UsersDTO usersDTO, Model model) {
        if (!usersDTO.getPassword().equals(usersDTO.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "auth/auth-page";
        }

        Users users = new Users();
        users.setName(usersDTO.getName());
        users.setEmail(usersDTO.getEmail());
        users.setPhone(usersDTO.getPhone());
        users.setPassword(passwordEncoder.encode(usersDTO.getPassword()));
        users.setRole(RoleEnum.valueOf(usersDTO.getRole()));

        try {
            usersService.save(users);
            if ("Customer".equals(usersDTO.getRole())) {
                Customer customer = new Customer();
                customer.setName(usersDTO.getName());
                customer.setEmail(usersDTO.getEmail());
                customer.setPhoneNo(usersDTO.getPhone());
                customer.setNationalIdNo("893");
                customer.setDateOfBirth(LocalDate.from(LocalDateTime.now()));
                customer.setAddress("NULL");
                customer.setDrivingLicense("NULL");
                customer.setWallet(BigDecimal.valueOf(0.0));
                customerService.save(customer);
            } else if ("Car_Owner".equals(usersDTO.getRole())) {
                CarOwner carOwner = new CarOwner();
                carOwner.setName(usersDTO.getName());
                carOwner.setEmail(usersDTO.getEmail());
                carOwner.setPhoneNo(usersDTO.getPhone());
                carOwner.setNationalIdNo("893");
                carOwner.setDateOfBirth(LocalDate.from(LocalDateTime.now()));
                carOwner.setAddress("NULL");
                carOwner.setDrivingLicense("NULL");
                carOwner.setWallet(BigDecimal.valueOf(0.0));
                carOwnerService.save(carOwner);
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while saving data");
            return "auth/auth-page";
        }

        return "redirect:/login";
    }

}
