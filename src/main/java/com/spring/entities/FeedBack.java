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

    @Column(name = "Ratings")
    @Enumerated(EnumType.STRING)
    private RatingStar ratings;

    @Column(name = "Content")
    private String content;

    @Column(name = "Date_Time")
    private LocalDateTime dateTime;

    @OneToOne(mappedBy = "feedback", fetch = FetchType.LAZY)
    private Booking booking;
}
