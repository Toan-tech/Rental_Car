package com.spring.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Ratings", nullable = false)
    @Enumerated(EnumType.STRING)
    private RatingStar ratings;

    @Column(name = "Content", nullable = false)
    private String content;

    @Column(name = "Date_Time", nullable = false)
    private LocalDateTime dateTime;

    @OneToOne(mappedBy = "feedback", fetch = FetchType.LAZY)
    private Booking booking;
}
