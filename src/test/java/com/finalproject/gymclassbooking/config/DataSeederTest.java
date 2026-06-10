package com.finalproject.gymclassbooking.config;

import com.finalproject.gymclassbooking.model.AppUser;
import com.finalproject.gymclassbooking.model.Role;
import com.finalproject.gymclassbooking.repository.GymClassRepository;
import com.finalproject.gymclassbooking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "app.seed.enabled=true")
class DataSeederTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GymClassRepository gymClassRepository;

    @Test
    void repairsDemoAccountCredentialsAndRoles() throws Exception {
        AppUser admin = userRepository.findByEmail("admin@gym.com").orElseThrow();
        admin.setPassword("invalid-password");
        admin.setRole(Role.USER);
        userRepository.save(admin);

        AppUser user = userRepository.findByEmail("user@gym.com").orElseThrow();
        user.setPassword("invalid-password");
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        new DataSeeder(userRepository, gymClassRepository, passwordEncoder, true).run();

        AppUser repairedAdmin = userRepository.findByEmail("admin@gym.com").orElseThrow();
        assertThat(passwordEncoder.matches("admin123", repairedAdmin.getPassword())).isTrue();
        assertThat(repairedAdmin.getRole()).isEqualTo(Role.ADMIN);

        AppUser repairedUser = userRepository.findByEmail("user@gym.com").orElseThrow();
        assertThat(passwordEncoder.matches("user123", repairedUser.getPassword())).isTrue();
        assertThat(repairedUser.getRole()).isEqualTo(Role.USER);
    }
}
