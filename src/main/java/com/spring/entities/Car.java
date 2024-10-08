package com.spring.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "License_Plate", nullable = false)
    private String licensePlate;

    @Column(name = "Brand", nullable = false)
    private String brand;

    @Column(name = "Model", nullable = false)
    private String model;

    @Column(name = "Color", nullable = false)
    private String color;

    @Column(name = "Number_Of_Seats", nullable = false)
    private int numberOfSeats;

    @Column(name = "Production_Years", nullable = false)
    private int productionYears;

    @Column(name = "Transmission_Type", nullable = false)
    private String transmissionType;

    @Column(name = "Fuel_Type", nullable = false)
    private String fuelType;

    @Column(name = "Mileage", precision = 18, scale = 2, nullable = false)
    private BigDecimal mileage;

    @Column(name = "Fuel_Consumption", precision = 18, scale = 2, nullable = false)
    private BigDecimal fuelConsumption;

    @Column(name = "Base_Price", precision = 18, scale = 2, nullable = false)
    private BigDecimal basePrice;

    @Column(name = "Deposit", precision = 18, scale = 2, nullable = false)
    private BigDecimal deposit;

    @Column(name = "Address", nullable = false)
    private String address;

    @Column(name = "Description", nullable = false)
    private String description;

    @Column(name = "Additional_Functions", nullable = false)
    private String additionalFunctions;

    @Column(name = "Terms_Of_Use", nullable = false)
    private String termsOfUse;

    @Column(name = "Images", nullable = false)
    private String images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_owner_id", nullable = false)
    private CarOwner carOwner;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    private List<Booking> bookings = new ArrayList<>();
}
