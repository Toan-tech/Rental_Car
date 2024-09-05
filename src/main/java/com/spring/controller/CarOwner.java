package com.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class CarOwner {
    @GetMapping(value = {"/"})
    public String home(Model model) {
        return "layout/Car_Owner/Homepage_LoggedIn_CarOwner";
    }

    @GetMapping(value="addcar")
    public String step1(Model model) {
        return "layout/Car_Owner/AddCar";
    }

    @GetMapping(value="list")
    public String listCar(Model model) {
        return "layout/Car_Owner/List";
    }

    @GetMapping(value="editdetail")
    public String editCar(Model model) {
        return "layout/Car_Owner/Edit";
    }

}
