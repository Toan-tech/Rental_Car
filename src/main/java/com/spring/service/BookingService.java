package com.spring.service;

import com.spring.entities.*;
import com.spring.repository.BookingSequenceRepository;
import com.spring.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private BookingSequenceRepository bookingSequenceRepository;

    public Page<Booking> findBookings(String driverInfo, LocalDateTime startDate, LocalDateTime endDate,
                                      String sortOption, int pageNumber, int pageSize) {
        Sort sort = getSortOption(sortOption);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return searchRepository.findBookingsByIdealCarAndAvailableStatus(driverInfo, startDate, endDate, pageable);
    }

    public List<Booking> findNumberOfCars(String driverInfo, LocalDateTime startDate, LocalDateTime endDate) {
        return searchRepository.findByIdealCar(driverInfo, startDate, endDate);
    }

    public List<Booking> findCompletedBookings() {
        return searchRepository.findByStatus(BookingStatus.Completed);
    }

    public Sort getSortOption(String sortOption) {
        return switch (sortOption) {
            case "oldest" -> Sort.by(Sort.Direction.ASC, "startDateTime");
            case "priceLowHigh" -> Sort.by(Sort.Direction.ASC, "car.basePrice");
            case "priceHighLow" -> Sort.by(Sort.Direction.DESC, "car.basePrice");
            default -> Sort.by(Sort.Direction.DESC, "startDateTime");
        };
    }

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
