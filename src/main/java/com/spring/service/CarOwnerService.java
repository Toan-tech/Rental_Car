package com.spring.service;

import com.spring.entities.CarOwner;
import com.spring.repository.CarOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarOwnerService {
    @Autowired
    private CarOwnerRepository carOwnerRepository;

    public void save(CarOwner carOwner) {
        carOwnerRepository.save(carOwner);
    }
}
