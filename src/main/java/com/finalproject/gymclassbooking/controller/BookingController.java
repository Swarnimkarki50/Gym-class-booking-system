package com.finalproject.gymclassbooking.controller;

import com.finalproject.gymclassbooking.model.AppUser;
import com.finalproject.gymclassbooking.service.BookingService;
import com.finalproject.gymclassbooking.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @PostMapping("/bookings")
    public String create(
            @RequestParam Long classId,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        AppUser user = userService.getByEmail(authentication.getName());
        try {
            bookingService.createBooking(user, classId);
            redirectAttributes.addFlashAttribute("success", "Booking created successfully.");
            return "redirect:/bookings";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/classes/" + classId;
        }
    }

    @GetMapping("/bookings")
    public String myBookings(Authentication authentication, Model model) {
        AppUser user = userService.getByEmail(authentication.getName());
        model.addAttribute("bookings", bookingService.findUserBookings(user));
        return "bookings/list";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancel(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        AppUser user = userService.getByEmail(authentication.getName());
        try {
            bookingService.cancelBooking(id, user);
            redirectAttributes.addFlashAttribute("success", "Booking cancelled.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/bookings";
    }
}
