package com.finalproject.gymclassbooking.config;

import com.finalproject.gymclassbooking.model.AppUser;
import com.finalproject.gymclassbooking.model.GymClass;
import com.finalproject.gymclassbooking.model.Role;
import com.finalproject.gymclassbooking.repository.GymClassRepository;
import com.finalproject.gymclassbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GymClassRepository gymClassRepository;
    private final PasswordEncoder passwordEncoder;
    private final boolean seedEnabled;

    public DataSeeder(
            UserRepository userRepository,
            GymClassRepository gymClassRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.seed.enabled:true}") boolean seedEnabled
    ) {
        this.userRepository = userRepository;
        this.gymClassRepository = gymClassRepository;
        this.passwordEncoder = passwordEncoder;
        this.seedEnabled = seedEnabled;
    }

    @Override
    public void run(String... args) {
        if (!seedEnabled) {
            return;
        }
        seedUsers();
        seedClasses();
    }

    private void seedUsers() {
        if (!userRepository.existsByEmail("admin@gym.com")) {
            AppUser admin = new AppUser();
            admin.setName("Admin User");
            admin.setEmail("admin@gym.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
        if (!userRepository.existsByEmail("user@gym.com")) {
            AppUser user = new AppUser();
            user.setName("Demo User");
            user.setEmail("user@gym.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(Role.USER);
            userRepository.save(user);
        }
    }

    private void seedClasses() {
        if (gymClassRepository.count() > 0) {
            return;
        }
        gymClassRepository.save(gymClass(
                "Morning HIIT Burn",
                "Sarah Kim",
                "HIIT",
                "A fast-paced full-body session with intervals, bodyweight strength, and cardio blocks.",
                new BigDecimal("18.00"),
                18,
                LocalDateTime.now().plusDays(2).withHour(7).withMinute(30),
                45,
                "https://images.unsplash.com/photo-1518611012118-696072aa579a?auto=format&fit=crop&w=1200&q=80"
        ));
        gymClassRepository.save(gymClass(
                "Power Yoga Flow",
                "Mina Park",
                "Yoga",
                "Build flexibility, balance, and calm through a guided vinyasa flow for all levels.",
                new BigDecimal("15.00"),
                20,
                LocalDateTime.now().plusDays(3).withHour(18).withMinute(0),
                60,
                "https://images.unsplash.com/photo-1506126613408-eca07ce68773?auto=format&fit=crop&w=1200&q=80"
        ));
        gymClassRepository.save(gymClass(
                "Strength Foundations",
                "Daniel Lee",
                "Strength",
                "Learn safe lifting technique with coached squats, presses, rows, and core work.",
                new BigDecimal("22.00"),
                12,
                LocalDateTime.now().plusDays(4).withHour(19).withMinute(15),
                50,
                "https://images.unsplash.com/photo-1534367507873-d2d7e24c797f?auto=format&fit=crop&w=1200&q=80"
        ));
        gymClassRepository.save(gymClass(
                "Spin Endurance Ride",
                "Alex Morgan",
                "Cycling",
                "A music-driven indoor cycling workout focused on stamina, climbs, and sprint intervals.",
                new BigDecimal("17.00"),
                16,
                LocalDateTime.now().plusDays(5).withHour(8).withMinute(0),
                45,
                "https://images.unsplash.com/photo-1599058917212-d750089bc07e?auto=format&fit=crop&w=1200&q=80"
        ));
    }

    private GymClass gymClass(
            String name,
            String instructor,
            String category,
            String description,
            BigDecimal price,
            int capacity,
            LocalDateTime startsAt,
            int durationMinutes,
            String imagePath
    ) {
        GymClass gymClass = new GymClass();
        gymClass.setName(name);
        gymClass.setInstructor(instructor);
        gymClass.setCategory(category);
        gymClass.setDescription(description);
        gymClass.setPrice(price);
        gymClass.setCapacity(capacity);
        gymClass.setStartsAt(startsAt);
        gymClass.setDurationMinutes(durationMinutes);
        gymClass.setImagePath(imagePath);
        gymClass.setActive(true);
        return gymClass;
    }
}
