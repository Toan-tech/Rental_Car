package com.spring.service;

import com.spring.entities.CarOwner;
import org.springframework.stereotype.Service;

@Service
public interface CarOwnerService {
    CarOwner findByEmail(String email);

    void updateCarOwner(CarOwner carOwner);
}
