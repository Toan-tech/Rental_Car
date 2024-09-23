package com.spring.service;

import com.spring.entities.Booking;
import com.spring.entities.FeedBack;
import com.spring.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public FeedBack getFeedbackByBooking(Booking booking) {
        return ratingRepository.findByBooking(booking);
    }

}
