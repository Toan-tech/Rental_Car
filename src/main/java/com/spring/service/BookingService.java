package com.spring.service;

import com.spring.entities.*;
import com.spring.repository.BookingSequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class BookingService {
    @Autowired
    private BookingSequenceRepository bookingSequenceRepository;

    public String generateBookingNumber() {
        LocalDate today = LocalDate.now();
        BookingSequence sequence = bookingSequenceRepository.findByDate(today);

        if (sequence == null) {
            sequence = new BookingSequence();
            sequence.setDate(today);
            sequence.setSequenceNumber(1);
        } else {
            sequence.setSequenceNumber(sequence.getSequenceNumber() + 1);
        }

        bookingSequenceRepository.save(sequence);

        return today.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + String.format("%04d", sequence.getSequenceNumber());
    }

    public void createBooking(Booking booking) {
        booking.setBookingNo(generateBookingNumber());
    }
}
