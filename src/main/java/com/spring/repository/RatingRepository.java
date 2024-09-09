package com.spring.repository;

import com.spring.entities.Booking;
import com.spring.entities.FeedBack;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<FeedBack, Integer> {

    List<FeedBack> findFeedbackByBooking(@Param("booking") Booking booking);
}
