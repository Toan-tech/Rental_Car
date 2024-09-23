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
public class CarOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @Column(name = "Wallet", precision = 18, scale = 2)
    private BigDecimal wallet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "carOwner")
    private List<Car> cars = new ArrayList<>();
}
