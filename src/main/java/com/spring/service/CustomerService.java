package com.spring.service;

import com.spring.entities.Customer;
import com.spring.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    Customer findByEmail(String email);

    void updateCustomer(Customer customer);
}
