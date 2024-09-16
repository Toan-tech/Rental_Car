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
    private Integer id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Date_Of_Birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "National_ID_No", nullable = false)
    private String nationalIdNo;

    @Column(name = "Phone_No", nullable = false)
    private String phoneNo;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "Address", nullable = false)
    private String address;

    @Column(name = "Driving_License", nullable = false)
    private String drivingLicense;

    @Column(name = "Wallet", precision = 18, scale = 2, nullable = false)
    private BigDecimal wallet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Booking> bookings = new ArrayList<>();
}
