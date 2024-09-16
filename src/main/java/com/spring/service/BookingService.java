package com.spring.service;

import com.spring.entities.Booking;
import com.spring.entities.BookingStatus;
import com.spring.entities.RatingStar;
import com.spring.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private SearchRepository searchRepository;

    public Page<Booking> findBookings(String driverInfo, LocalDateTime startDate, LocalDateTime endDate,
                                      String sortOption, int pageNumber, int pageSize) {
        Sort sort = getSortOption(sortOption);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return searchRepository.findBookingsByIdealCar(driverInfo, startDate, endDate, pageable);
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
}
