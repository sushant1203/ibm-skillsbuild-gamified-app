package com.example.skillsync.controller;

import com.example.skillsync.model.Badge;
import com.example.skillsync.model.Course;
import com.example.skillsync.model.Enrollment;
import com.example.skillsync.model.User;
import com.example.skillsync.repo.BadgeRepository;
import com.example.skillsync.repo.EnrollmentRepository;
import com.example.skillsync.service.CertificateService;
import com.example.skillsync.service.RecommendationService;
import com.example.skillsync.service.UserService;
import com.example.skillsync.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private BadgeRepository badgeRepository;

    // Show the registration page
    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    // Handle user registration
    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String username,
                               @RequestParam String password,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        List<String> errors = userService.registerUser(name, email, username, password);
        System.out.println(errors);
        if (errors != null) {
            model.addAttribute("errors", errors);
            return "register";
        }
        //redirects user to login page with success message
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");

        return "redirect:/login";
    }


    @PostMapping("editUsername") // handles username editing
    public String editUserName(@RequestParam String newUsername, @RequestParam String password, Model model){
        List<String> errors = userService.editUserName(newUsername,password); // UserService
        if (errors != null){
            model.addAttribute("errors", errors); // error check
            return "settings"; // Returns with error message
        }
        model.addAttribute("pass", "Username updated successfully");
        return "settings";
    }

    @PostMapping("editPassword")
    public String editPassword(@RequestParam String password, @RequestParam String newPassword, @RequestParam String newPasswordConfirm, Model model) {
        List<String> errors = userService.editPassword(newPassword, password, newPasswordConfirm);
        if (errors != null) {
            model.addAttribute("errors", errors);
            return "settings";
        }
        model.addAttribute("pass", "Password updated successfully");
        return "settings";
    }

    // Display the login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Show the user dashboard after login
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        String loggedUserName = principal.getName();
        User user = userService.findByUsername(loggedUserName);
        model.addAttribute("username",user.getUsername());

        userService.setStreak(user);
        // Check and award new badges based on user score
        badgeService.checkAndAwardBadges(user);

        model.addAttribute("username", user.getUsername());

        model.addAttribute("score", user.getScore());

        List<Course> recommendedCourses = recommendationService.recommendCourse(user.getId());
        model.addAttribute("recommendedCourses", recommendedCourses);

        List<Enrollment> enrollments = enrollmentRepository.findByUser(user);
        model.addAttribute("enrollments", enrollments);

        // Retrieve all badges and determine the current badge (highest points)
        List<Badge> badges = badgeRepository.findByUser(user);
        Badge currentBadge = badges.stream()
                .max(Comparator.comparing(Badge::getPointsRequired))
                .orElse(null);
        model.addAttribute("currentBadge", currentBadge);

        return "dashboard";
    }

    @GetMapping("/")
    public String showHome() {
        return "landing";
    }

    @GetMapping("/settings")
    public String showSettings(Model model) {
        return "settings";
    }
}