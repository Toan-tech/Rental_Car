package com.spring.repository;

import com.spring.entities.Booking;
import com.spring.entities.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<FeedBack, Integer> {
    FeedBack findByBooking(Booking booking);


}
