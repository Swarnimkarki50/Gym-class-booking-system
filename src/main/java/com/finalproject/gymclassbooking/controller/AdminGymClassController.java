package com.finalproject.gymclassbooking.controller;

import com.finalproject.gymclassbooking.model.GymClass;
import com.finalproject.gymclassbooking.service.FileStorageService;
import com.finalproject.gymclassbooking.service.GymClassService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminGymClassController {

    private static final String DEFAULT_IMAGE = "https://images.unsplash.com/photo-1518611012118-696072aa579a?auto=format&fit=crop&w=1200&q=80";

    private final GymClassService gymClassService;
    private final FileStorageService fileStorageService;

    public AdminGymClassController(GymClassService gymClassService, FileStorageService fileStorageService) {
        this.gymClassService = gymClassService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/admin/classes")
    public String dashboard(Model model) {
        model.addAttribute("classes", gymClassService.findAllClasses());
        return "admin/classes";
    }

    @GetMapping({"/admin/classes/new", "/classes/new"})
    public String newClass(Model model) {
        model.addAttribute("gymClass", new GymClass());
        model.addAttribute("formTitle", "Add Gym Class");
        return "admin/class-form";
    }

    @GetMapping({"/admin/classes/{id}/edit", "/classes/{id}/edit"})
    public String editClass(@PathVariable Long id, Model model) {
        model.addAttribute("gymClass", gymClassService.getClass(id));
        model.addAttribute("formTitle", "Edit Gym Class");
        return "admin/class-form";
    }

    @PostMapping({"/admin/classes", "/classes"})
    public String saveClass(
            @Valid @ModelAttribute("gymClass") GymClass gymClass,
            BindingResult bindingResult,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", gymClass.getId() == null ? "Add Gym Class" : "Edit Gym Class");
            return "admin/class-form";
        }
        try {
            String uploadedPath = fileStorageService.store(imageFile);
            if (uploadedPath != null) {
                gymClass.setImagePath(uploadedPath);
            } else if (gymClass.getImagePath() == null || gymClass.getImagePath().isBlank()) {
                gymClass.setImagePath(DEFAULT_IMAGE);
            }
        } catch (IllegalArgumentException exception) {
            bindingResult.rejectValue("imagePath", "upload", exception.getMessage());
            model.addAttribute("formTitle", gymClass.getId() == null ? "Add Gym Class" : "Edit Gym Class");
            return "admin/class-form";
        }
        gymClassService.save(gymClass);
        redirectAttributes.addFlashAttribute("success", "Gym class saved.");
        return "redirect:/admin/classes";
    }

    @PostMapping({"/admin/classes/{id}/delete", "/classes/{id}/delete"})
    public String deleteClass(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        gymClassService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Class post deleted successfully.");
        return "redirect:/admin/classes";
    }
}
