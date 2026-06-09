package com.finalproject.gymclassbooking.controller;

import com.finalproject.gymclassbooking.service.GymClassService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GymClassController {

    private final GymClassService gymClassService;

    public GymClassController(GymClassService gymClassService) {
        this.gymClassService = gymClassService;
    }

    @GetMapping("/classes/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("gymClass", gymClassService.getClass(id));
        return "classes/detail";
    }
}
