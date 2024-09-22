package com.spring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class IdealCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Location")
    private String location;

    @Column(name = "Pickup_Date_Time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "Please enter pick up date and time")
    private LocalDateTime pickupDateTime;

    @Column(name = "Drop_Off_Date_Time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "Please enter drop-off date and time")
    private LocalDateTime dropOffDateTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idealCar")
    private List<Booking> bookings = new ArrayList<>();
}
