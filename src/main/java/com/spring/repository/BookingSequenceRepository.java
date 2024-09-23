package com.spring.repository;

import com.spring.entities.BookingSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingSequenceRepository extends JpaRepository<BookingSequence, Long> {
    BookingSequence findByDate(LocalDate date);
}
