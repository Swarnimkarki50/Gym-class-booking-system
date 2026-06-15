package com.finalproject.gymclassbooking.repository;

import com.finalproject.gymclassbooking.model.AppUser;
import com.finalproject.gymclassbooking.model.Booking;
import com.finalproject.gymclassbooking.model.BookingStatus;
import com.finalproject.gymclassbooking.model.GymClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserOrderByCreatedAtDesc(AppUser user);

    List<Booking> findAllByOrderByCreatedAtDesc();

    boolean existsByUser(AppUser user);

    boolean existsByGymClass(GymClass gymClass);

    long countByGymClassAndStatus(GymClass gymClass, BookingStatus status);

    boolean existsByUserAndGymClassAndStatus(AppUser user, GymClass gymClass, BookingStatus status);
}
