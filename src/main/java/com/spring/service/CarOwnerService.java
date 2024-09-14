package com.spring.service;

import com.spring.entities.CarOwner;
import com.spring.entities.Customer;
import com.spring.repository.CarOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface CarOwnerService {
    CarOwner findByEmail(String email);

    void updateCarOwner(CarOwner carOwner);
}
