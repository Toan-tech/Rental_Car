package com.spring.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @GetMapping("/home-page")
    public String homePage(Model model) {
        return "home/home-page-as-guest";
    }

    @GetMapping("/customer")
    public String homePageAsCustomer() {
        return "home/home-page-as-customer";
    }

    @GetMapping("/car-owner")
    public String homePageAsCarOwner() {
        return "home/home-page-as-car-owner";
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
}
