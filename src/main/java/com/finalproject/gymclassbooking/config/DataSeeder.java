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
        upsertDemoUser("Admin User", "admin@gym.com", "admin123", Role.ADMIN);
        upsertDemoUser("Demo User", "user@gym.com", "user123", Role.USER);
    }

    private void upsertDemoUser(String name, String email, String rawPassword, Role role) {
        AppUser user = userRepository.findByEmail(email).orElseGet(AppUser::new);
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);

        if (user.getPassword() == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        }

        userRepository.save(user);
    }

    private void seedClasses() {
        migrateLegacyPrices();
        if (gymClassRepository.count() > 0) {
            return;
        }
        gymClassRepository.save(gymClass(
                "Morning HIIT Burn",
                "Sarah Kim",
                "HIIT",
                "A fast-paced full-body session with intervals, bodyweight strength, and cardio blocks.",
                new BigDecimal("18000"),
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
                new BigDecimal("15000"),
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
                new BigDecimal("22000"),
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
                new BigDecimal("17000"),
                16,
                LocalDateTime.now().plusDays(5).withHour(8).withMinute(0),
                45,
                "https://images.unsplash.com/photo-1599058917212-d750089bc07e?auto=format&fit=crop&w=1200&q=80"
        ));
    }

    private void migrateLegacyPrices() {
        BigDecimal wonThreshold = new BigDecimal("1000");
        BigDecimal conversionRate = new BigDecimal("1000");

        gymClassRepository.findAll().stream()
                .filter(gymClass -> gymClass.getPrice() != null)
                .filter(gymClass -> gymClass.getPrice().compareTo(wonThreshold) < 0)
                .forEach(gymClass -> {
                    gymClass.setPrice(gymClass.getPrice().multiply(conversionRate));
                    gymClassRepository.save(gymClass);
                });
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
