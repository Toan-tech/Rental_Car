package com.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {
    @GetMapping(value ={ "/"})
    public String home(Model model) {
        return "layout/customer/BookingDetails_CarInformation";
    }

    @GetMapping(value ={ "/1"})
    public String home1(Model model) {
        return "layout/customer/ViewDetails_Details";
    }

    @GetMapping(value ={ "/2"})
    public String home2(Model model) {
        return "layout/customer/ViewDetails_Term";
    }
}
