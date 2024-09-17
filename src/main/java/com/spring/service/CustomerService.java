package com.spring.service;

import com.spring.entities.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    Customer findByEmail(String email);

    void updateCustomer(Customer customer);
}
