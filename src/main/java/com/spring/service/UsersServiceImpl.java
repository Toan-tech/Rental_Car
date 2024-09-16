package com.spring.service;

import com.spring.entities.CarOwner;
import com.spring.entities.Customer;
import com.spring.entities.RoleEnum;
import com.spring.entities.Users;
import com.spring.model.UsersDTO;
import com.spring.repository.CarOwnerRepository;
import com.spring.repository.CustomerRepository;
import com.spring.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CarOwnerRepository carOwnerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại: " + email));
    }

    @Override
    public void registerUser(UsersDTO usersDTO) {
        Users users = new Users();
        users.setName(usersDTO.getName());
        users.setEmail(usersDTO.getEmail());
        users.setPhone(usersDTO.getPhone());
        users.setPassword(passwordEncoder.encode(usersDTO.getPassword()));
        users.setRole(RoleEnum.valueOf(usersDTO.getRole()));

        usersRepository.save(users);

        if ("Customer".equals(usersDTO.getRole())) {
            saveCustomer(usersDTO);
        } else if ("Car_Owner".equals(usersDTO.getRole())) {
            saveCarOwner(usersDTO);
        }
    }

    @Override
    public void updatePassword(Users users, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        users.setPassword(encodedPassword);
        usersRepository.save(users);
    }

    private void saveCustomer(UsersDTO usersDTO) {
        Customer customer = new Customer();
        customer.setName(usersDTO.getName());
        customer.setEmail(usersDTO.getEmail());
        customer.setPhoneNo(usersDTO.getPhone());
        customer.setNationalIdNo("893");
        customer.setDateOfBirth(LocalDate.now());
        customer.setAddress("NULL");
        customer.setDrivingLicense("NULL");
        customer.setWallet(BigDecimal.ZERO);
        customerRepository.save(customer);
    }

    private void saveCarOwner(UsersDTO usersDTO) {
        CarOwner carOwner = new CarOwner();
        carOwner.setName(usersDTO.getName());
        carOwner.setEmail(usersDTO.getEmail());
        carOwner.setPhoneNo(usersDTO.getPhone());
        carOwner.setNationalIdNo("893");
        carOwner.setDateOfBirth(LocalDate.now());
        carOwner.setAddress("NULL");
        carOwner.setDrivingLicense("NULL");
        carOwner.setWallet(BigDecimal.ZERO);
        carOwnerRepository.save(carOwner);
    }
}
