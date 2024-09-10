package com.spring.rest;

import com.spring.entities.Car;
import com.spring.entities.CarOwner;
import com.spring.repository.CarOwnerRepository;
import com.spring.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/mycar/")
public class RestCarOwner {
    @Autowired
    CarOwnerRepository carOwnerRepository;

    @Autowired
    CarRepository carRepository;

    @PostMapping("/validatelicense")
    public boolean validateLicense(@RequestParam("license") String license) {
        CarOwner carOwner = carOwnerRepository.findById(1).orElse(null);
        List<Car> carList = carRepository.findAllByCarOwner(carOwner);
        for (Car car : carList) {
            if (car.getLicensePlate().contains(license)) {
                return true;
            }
        }
        return false;
    }
}
