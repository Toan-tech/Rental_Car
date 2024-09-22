package com.spring.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Date_Of_Birth")
    private LocalDate dateOfBirth;

    @Column(name = "National_ID_No")
    private String nationalIdNo;

    @Column(name = "Phone_No")
    private String phoneNo;

    @Column(name = "Email")
    private String email;

    @Column(name = "Address")
    private String address;

    @Column(name = "Driving_License")
    private String drivingLicense;

    @Column(name = "Wallet", precision = 18, scale = 3)
    private BigDecimal wallet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Booking> bookings = new ArrayList<>();
}
