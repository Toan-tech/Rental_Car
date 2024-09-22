package com.spring.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Column(name = "Fuel_Consumption", precision = 18, scale = 3, nullable = false)
    private BigDecimal fuelConsumption;

    @Column(name = "Base_Price", precision = 18, scale = 3, nullable = false)
    private BigDecimal basePrice;

    @Column(name = "Deposit", precision = 18, scale = 3, nullable = false)
    private BigDecimal deposit;

    @Column(name = "Address", nullable = false)
    private String address;

    @Column(name = "Description", nullable = false)
    private String description;

    @ElementCollection(targetClass = AdditionalFunctions.class)
    @CollectionTable(name = "car_additional_functions", joinColumns = @JoinColumn(name = "car_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "additional_function")
    private Set<AdditionalFunctions> additionalFunctions;

    @ElementCollection(targetClass = TermOfUse.class)
    @CollectionTable(name = "car_terms_of_use", joinColumns = @JoinColumn(name = "car_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "term_of_use")
    private Set<TermOfUse> termsOfUse;

    @Column(name = "Images", columnDefinition = "VARCHAR(600) NOT NULL")
    private String images;

    @Column(name = "Status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_owner_id", nullable = false)
    private CarOwner carOwner;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    private List<Booking> bookings = new ArrayList<>();

    public double getRatingAvgStar() {
        int star = 0;
        for (Booking booking : bookings) {
            if (booking.getFeedback().getRatings() == null) {
                star += 0;
            } else {
                star += booking.getFeedback().getRatings().ordinal() + 1;
            }
        }
        return (double) star / (double)bookings.size();
    }
}
