package com.spring.repository;

import com.spring.entities.FeedBack;
import com.spring.entities.RatingStar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
public interface FeedBackRepository extends JpaRepository<FeedBack, Integer> {
    @Query("SELECT f.ratings FROM FeedBack f JOIN Booking b ON f = b.feedback JOIN Car c ON b.car = c WHERE c.id = :param")
    List<String> findALLStarByCarID(@Param("param") Integer carId);
}
