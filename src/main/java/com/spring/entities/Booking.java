package com.spring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Booking_No")
    private String bookingNo;

    @Column(name = "Driver_Info")
    private String driverInfo;

    @Column(name = "Start_Date_Time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "Please enter start date time")
    private LocalDateTime startDateTime;

    @Column(name = "End_Date_Time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "Please enter end date time")
    private LocalDateTime endDateTime;

    @Column(name = "Payment_Method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "feedback_id", nullable = true)
    private FeedBack feedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idealCar_id", nullable = true)
    private IdealCar idealCar;
}
