package com.spring.service;

import com.spring.entities.CarOwner;
import com.spring.repository.CarOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CarOwnerServiceImpl implements CarOwnerService{
    @Autowired
    CarOwnerRepository carOwnerRepository;

    @Override
    public CarOwner findByEmail(String email) {
        return carOwnerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + email));
    }

    @Override
    public void updateCarOwner(CarOwner carOwner) {
        carOwnerRepository.save(carOwner);
    }
}
