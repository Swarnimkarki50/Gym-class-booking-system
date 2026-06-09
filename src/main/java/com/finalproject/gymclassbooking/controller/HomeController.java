package com.finalproject.gymclassbooking.controller;

import com.finalproject.gymclassbooking.service.GymClassService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final GymClassService gymClassService;

    public HomeController(GymClassService gymClassService) {
        this.gymClassService = gymClassService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("classes", gymClassService.findActiveClasses(null).stream().limit(3).toList());
        return "home";
    }

    @GetMapping("/classes")
    public String classes(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("classes", gymClassService.findActiveClasses(keyword));
        return "classes/list";
    }
}
