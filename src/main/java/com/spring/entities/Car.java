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

    @Column(name = "Name")
    private String name;

    @Column(name = "License_Plate")
    private String licensePlate;

    @Column(name = "Brand")
    private String brand;

    @Column(name = "Model")
    private String model;

    @Column(name = "Color")
    private String color;

    @Column(name = "Number_Of_Seats")
    private int numberOfSeats;

    @Column(name = "Production_Years")
    private int productionYears;

    @Column(name = "Transmission_Type")
    private String transmissionType;

    @Column(name = "Fuel_Type")
    private String fuelType;

    @Column(name = "Mileage", precision = 18, scale = 2)
    private BigDecimal mileage;

    @Column(name = "Fuel_Consumption", precision = 18, scale = 3)
    private BigDecimal fuelConsumption;

    @Column(name = "Base_Price", precision = 18, scale = 3)
    private BigDecimal basePrice;

    @Column(name = "Deposit", precision = 18, scale = 3)
    private BigDecimal deposit;

    @Column(name = "Address")
    private String address;

    @Column(name = "Description")
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

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_owner_id")
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
