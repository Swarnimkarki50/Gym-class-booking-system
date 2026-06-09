package com.finalproject.gymclassbooking.service;

import com.finalproject.gymclassbooking.model.GymClass;
import com.finalproject.gymclassbooking.repository.BookingRepository;
import com.finalproject.gymclassbooking.repository.GymClassRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class GymClassService {

    private final GymClassRepository gymClassRepository;
    private final BookingRepository bookingRepository;

    public GymClassService(GymClassRepository gymClassRepository, BookingRepository bookingRepository) {
        this.gymClassRepository = gymClassRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<GymClass> findActiveClasses(String keyword) {
        if (StringUtils.hasText(keyword)) {
            return gymClassRepository.searchActive(keyword.trim());
        }
        return gymClassRepository.findByActiveTrueOrderByStartsAtAscNameAsc();
    }

    public List<GymClass> findAllClasses() {
        return gymClassRepository.findAll();
    }

    public GymClass getClass(Long id) {
        return gymClassRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Class was not found."));
    }

    @Transactional
    public GymClass save(GymClass gymClass) {
        return gymClassRepository.save(gymClass);
    }

    @Transactional
    public void delete(Long id) {
        GymClass gymClass = getClass(id);
        if (bookingRepository.existsByGymClass(gymClass)) {
            gymClass.setActive(false);
            gymClassRepository.save(gymClass);
            return;
        }
        gymClassRepository.delete(gymClass);
    }
}
