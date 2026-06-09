package com.finalproject.gymclassbooking.service;

import com.finalproject.gymclassbooking.model.AppUser;
import com.finalproject.gymclassbooking.model.Booking;
import com.finalproject.gymclassbooking.model.BookingStatus;
import com.finalproject.gymclassbooking.model.GymClass;
import com.finalproject.gymclassbooking.repository.BookingRepository;
import com.finalproject.gymclassbooking.repository.GymClassRepository;
import com.finalproject.gymclassbooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private GymClassRepository gymClassRepository;

    @Autowired
    private UserRepository userRepository;

    private AppUser user;
    private GymClass gymClass;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        gymClassRepository.deleteAll();
        userRepository.deleteAll();

        user = new AppUser();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user = userRepository.save(user);

        gymClass = new GymClass();
        gymClass.setName("Test HIIT");
        gymClass.setInstructor("Coach Test");
        gymClass.setCategory("HIIT");
        gymClass.setPrice(new BigDecimal("20.00"));
        gymClass.setCapacity(2);
        gymClass.setStartsAt(LocalDateTime.now().plusDays(2));
        gymClass.setDurationMinutes(45);
        gymClass.setDescription("A test class.");
        gymClass.setImagePath("https://example.com/class.jpg");
        gymClass.setActive(true);
        gymClass = gymClassRepository.save(gymClass);
    }

    @Test
    void createsBookingWithClassPrice() {
        Booking booking = bookingService.createBooking(user, gymClass.getId());

        assertThat(booking.getStatus()).isEqualTo(BookingStatus.BOOKED);
        assertThat(booking.getGymClass().getName()).isEqualTo("Test HIIT");
        assertThat(booking.getTotalPrice()).isEqualByComparingTo("20.00");
    }

    @Test
    void rejectsDuplicateClassBooking() {
        bookingService.createBooking(user, gymClass.getId());

        assertThatThrownBy(() -> bookingService.createBooking(user, gymClass.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already booked");
    }

    @Test
    void rejectsFullyBookedClass() {
        AppUser secondUser = new AppUser();
        secondUser.setName("Second User");
        secondUser.setEmail("second@example.com");
        secondUser.setPassword("password");
        secondUser = userRepository.save(secondUser);

        AppUser thirdUser = new AppUser();
        thirdUser.setName("Third User");
        thirdUser.setEmail("third@example.com");
        thirdUser.setPassword("password");
        thirdUser = userRepository.save(thirdUser);

        bookingService.createBooking(user, gymClass.getId());
        bookingService.createBooking(secondUser, gymClass.getId());

        AppUser finalThirdUser = thirdUser;
        assertThatThrownBy(() -> bookingService.createBooking(finalThirdUser, gymClass.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fully booked");
    }

    @Test
    void cancelsOwnBooking() {
        Booking booking = bookingService.createBooking(user, gymClass.getId());

        bookingService.cancelBooking(booking.getId(), user);

        Booking cancelled = bookingRepository.findById(booking.getId()).orElseThrow();
        assertThat(cancelled.getStatus()).isEqualTo(BookingStatus.CANCELLED);
    }
}
