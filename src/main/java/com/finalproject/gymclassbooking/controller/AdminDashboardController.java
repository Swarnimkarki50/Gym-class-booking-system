package com.finalproject.gymclassbooking.controller;

import com.finalproject.gymclassbooking.model.AppUser;
import com.finalproject.gymclassbooking.model.BookingStatus;
import com.finalproject.gymclassbooking.model.Role;
import com.finalproject.gymclassbooking.service.BookingService;
import com.finalproject.gymclassbooking.service.GymClassService;
import com.finalproject.gymclassbooking.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final UserService userService;
    private final GymClassService gymClassService;
    private final BookingService bookingService;

    public AdminDashboardController(UserService userService, GymClassService gymClassService, BookingService bookingService) {
        this.userService = userService;
        this.gymClassService = gymClassService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public String dashboard(Model model) {
        var users = userService.findAllUsers();
        var classes = gymClassService.findAllClasses();
        var bookings = bookingService.findAllBookings();

        model.addAttribute("totalUsers", users.size());
        model.addAttribute("totalMembers", users.stream().filter(user -> user.getRole() == Role.USER).count());
        model.addAttribute("totalAdmins", users.stream().filter(user -> user.getRole() == Role.ADMIN).count());
        model.addAttribute("totalClasses", classes.size());
        model.addAttribute("activeClasses", classes.stream().filter(gymClass -> gymClass.isActive()).count());
        model.addAttribute("totalBookings", bookings.size());
        model.addAttribute("activeBookings", bookings.stream().filter(booking -> booking.getStatus() == BookingStatus.BOOKED).count());
        model.addAttribute("recentBookings", bookings.stream().limit(5).toList());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model, Authentication authentication) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("currentEmail", authentication.getName());
        return "admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        AppUser user = userService.getById(id);
        if (user.getEmail().equals(authentication.getName())) {
            redirectAttributes.addFlashAttribute("error", "You cannot delete your own admin account.");
            return "redirect:/admin/users";
        }
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/bookings")
    public String bookings(Model model) {
        model.addAttribute("bookings", bookingService.findAllBookings());
        return "admin/bookings";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookingService.cancelBookingAsAdmin(id);
        redirectAttributes.addFlashAttribute("success", "Booking cancelled.");
        return "redirect:/admin/bookings";
    }
}
