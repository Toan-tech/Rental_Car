package com.spring.repository;

import com.spring.entities.Booking;
import com.spring.entities.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack, Integer>{
    @Query("SELECT f.ratings FROM FeedBack f JOIN Booking b ON f = b.feedback JOIN Car c ON b.car = c WHERE c.id = :param")
    List<String> findALLStarByCarID(@Param("param") Integer carId);
}
