package com.finalproject.gymclassbooking.controller;

import com.finalproject.gymclassbooking.model.AppUser;
import com.finalproject.gymclassbooking.model.GymClass;
import com.finalproject.gymclassbooking.model.Role;
import com.finalproject.gymclassbooking.repository.GymClassRepository;
import com.finalproject.gymclassbooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CriteriaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GymClassRepository gymClassRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanData() {
        gymClassRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void exposesOnlyAuthenticationAndStaticPagesPublicly() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/classes"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void signupEncryptsPasswordAndLoginLogoutWork() throws Exception {
        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .param("name", "New Member")
                        .param("email", "member@example.com")
                        .param("password", "secret123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        AppUser user = userRepository.findByEmail("member@example.com").orElseThrow();
        assertThat(user.getPassword()).isNotEqualTo("secret123");
        assertThat(passwordEncoder.matches("secret123", user.getPassword())).isTrue();

        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("email", "member@example.com")
                        .param("password", "secret123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/classes"));

        mockMvc.perform(post("/logout")
                        .with(user("member@example.com").roles("USER"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));
    }

    @Test
    void classManagementAliasesRequireAdminAndSupportCrudWithImage() throws Exception {
        mockMvc.perform(get("/classes/new").with(user("member@example.com").roles("USER")))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/classes/new").with(user("admin@gym.com").roles("ADMIN")))
                .andExpect(status().isOk());

        MockMultipartFile image = new MockMultipartFile(
                "imageFile",
                "class.png",
                "image/png",
                new byte[]{1, 2, 3}
        );
        LocalDateTime startsAt = LocalDateTime.now().plusDays(3).withSecond(0).withNano(0);

        mockMvc.perform(multipart("/classes")
                        .file(image)
                        .with(user("admin@gym.com").roles("ADMIN"))
                        .with(csrf())
                        .param("name", "Pilates Core")
                        .param("instructor", "Coach Mina")
                        .param("category", "Pilates")
                        .param("price", "19.00")
                        .param("capacity", "10")
                        .param("startsAt", startsAt.toString())
                        .param("durationMinutes", "45")
                        .param("description", "Core strength and mobility class.")
                        .param("active", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/classes"));

        GymClass saved = gymClassRepository.findAll().stream().findFirst().orElseThrow();
        assertThat(saved.getImagePath()).startsWith("/uploads/");

        mockMvc.perform(get("/classes/{id}/edit", saved.getId())
                        .with(user("admin@gym.com").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/classes/{id}/delete", saved.getId())
                        .with(user("admin@gym.com").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/classes"));

        assertThat(gymClassRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void keywordSearchMatchesNameInstructorAndDescription() {
        GymClass yoga = gymClass("Sunrise Yoga", "Mina Park", "Gentle stretching and balance");
        GymClass boxing = gymClass("Boxing Basics", "Alex Stone", "Learn safe combinations");
        gymClassRepository.save(yoga);
        gymClassRepository.save(boxing);

        assertThat(gymClassRepository.searchActive("sunrise")).extracting(GymClass::getName)
                .containsExactly("Sunrise Yoga");
        assertThat(gymClassRepository.searchActive("alex")).extracting(GymClass::getName)
                .containsExactly("Boxing Basics");
        assertThat(gymClassRepository.searchActive("balance")).extracting(GymClass::getName)
                .containsExactly("Sunrise Yoga");
    }

    private GymClass gymClass(String name, String instructor, String description) {
        GymClass gymClass = new GymClass();
        gymClass.setName(name);
        gymClass.setInstructor(instructor);
        gymClass.setCategory("Fitness");
        gymClass.setPrice(new BigDecimal("15.00"));
        gymClass.setCapacity(12);
        gymClass.setStartsAt(LocalDateTime.now().plusDays(2));
        gymClass.setDurationMinutes(45);
        gymClass.setDescription(description);
        gymClass.setImagePath("https://example.com/class.jpg");
        gymClass.setActive(true);
        return gymClass;
    }
}
