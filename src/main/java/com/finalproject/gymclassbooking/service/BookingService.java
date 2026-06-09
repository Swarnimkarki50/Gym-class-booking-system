package com.finalproject.gymclassbooking.service;

import com.finalproject.gymclassbooking.model.AppUser;
import com.finalproject.gymclassbooking.model.Booking;
import com.finalproject.gymclassbooking.model.BookingStatus;
import com.finalproject.gymclassbooking.model.GymClass;
import com.finalproject.gymclassbooking.repository.BookingRepository;
import com.finalproject.gymclassbooking.repository.GymClassRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final GymClassRepository gymClassRepository;

    public BookingService(BookingRepository bookingRepository, GymClassRepository gymClassRepository) {
        this.bookingRepository = bookingRepository;
        this.gymClassRepository = gymClassRepository;
    }

    @Transactional
    public Booking createBooking(AppUser user, Long classId) {
        GymClass gymClass = gymClassRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class was not found."));
        validateBooking(user, gymClass);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setGymClass(gymClass);
        booking.setTotalPrice(gymClass.getPrice() == null ? BigDecimal.ZERO : gymClass.getPrice());
        booking.setStatus(BookingStatus.BOOKED);
        return bookingRepository.save(booking);
    }

    public List<Booking> findUserBookings(AppUser user) {
        return bookingRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public void cancelBooking(Long bookingId, AppUser user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking was not found."));
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only cancel your own bookings.");
        }
        booking.setStatus(BookingStatus.CANCELLED);
    }

    private void validateBooking(AppUser user, GymClass gymClass) {
        if (!gymClass.isActive()) {
            throw new IllegalArgumentException("This class is not available.");
        }
        if (gymClass.getStartsAt() == null || gymClass.getStartsAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("This class has already started.");
        }
        if (bookingRepository.existsByUserAndGymClassAndStatus(user, gymClass, BookingStatus.BOOKED)) {
            throw new IllegalArgumentException("You already booked this class.");
        }
        long bookedSeats = bookingRepository.countByGymClassAndStatus(gymClass, BookingStatus.BOOKED);
        if (bookedSeats >= gymClass.getCapacity()) {
            throw new IllegalArgumentException("This class is fully booked.");
        }
    }
}
