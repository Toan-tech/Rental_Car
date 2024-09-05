package com.spring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Booking_No", nullable = false)
    private String bookingNo;

    @Column(name = "Start_Date_Time", nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy,HH:mm")
    @NotNull(message = "Pick-up date and time are required")
    private LocalDateTime startDateTime;

    @Column(name = "End_Date_Time", nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy,HH:mm")
    @NotNull(message = "Drop-off date and time are required")
    private LocalDateTime endDateTime;

    @Column(name = "Driver_Info", nullable = false)
    @NotBlank(message = "Pick-up location is required")
    private String driverInfo;

    @Column(name = "Payment_Method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "Status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "feedback_id", nullable = true)
    private FeedBack feedback;
}
