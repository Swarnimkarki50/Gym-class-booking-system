package com.finalproject.gymclassbooking.controller;

import com.finalproject.gymclassbooking.model.AppUser;
import com.finalproject.gymclassbooking.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class CurrentUserAdvice {

    private final UserRepository userRepository;

    public CurrentUserAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("currentUser")
    public AppUser currentUser(Authentication authentication) {
        return findCurrentUser(authentication).orElse(null);
    }

    @ModelAttribute("currentUserName")
    public String currentUserName(Authentication authentication) {
        return findCurrentUser(authentication)
                .map(AppUser::getName)
                .filter(name -> !name.isBlank())
                .orElse("");
    }

    @ModelAttribute("currentUserInitial")
    public String currentUserInitial(Authentication authentication) {
        String name = currentUserName(authentication).trim();
        return name.isEmpty() ? "U" : name.substring(0, 1).toUpperCase();
    }

    private java.util.Optional<AppUser> findCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return java.util.Optional.empty();
        }
        return userRepository.findByEmail(authentication.getName());
    }
}
