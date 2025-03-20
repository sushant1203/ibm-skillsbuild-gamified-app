package com.example.skillsync.controller;

import com.example.skillsync.model.Course;
import com.example.skillsync.model.Enrollment;
import com.example.skillsync.model.User;
import com.example.skillsync.repo.EnrollmentRepository;
import com.example.skillsync.service.RecommendationService;
import com.example.skillsync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    //shows the registration page
    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    //handles user registration
    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String username,
                               @RequestParam String password,
                               Model model) {
        List<String> errors;

        // UserService handles the registration of the user
        errors = userService.registerUser( name, email, username, password);
        System.out.println(errors);
        if (errors != null) {
            model.addAttribute("errors", errors);
            return "register";
        }

        //redirects user to login page with success message
        return "redirect:/login";
    }

    @PostMapping("editUsername") // handles username editing
    public String editUserName(@RequestParam String newUsername, @RequestParam String password, Model model){
        List<String> errors = userService.editUserName(newUsername,password); // UserService
        if (errors != null){
            model.addAttribute("errors", errors); // error check
            return "settings"; // Returns with error message
        }
        model.addAttribute("pass","Username updated successfully");
        return "settings"; // return with success
    }

    @PostMapping("editPassword") // handles password editing
    public String editPassword(@RequestParam String password, @RequestParam String newPassword, @RequestParam String newPasswordConfirm, Model model){
        List<String> errors = userService.editPassword( newPassword,  password,  newPasswordConfirm); // UserService
        if (errors != null){
            model.addAttribute("errors", errors); // error check
            return "settings"; // Returns with error message
        }
        model.addAttribute("pass","Password updated successfully");
        return "settings"; // return with success
    }


    //displays the login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    //shows the user dashboard after login
    @GetMapping("/dashboard")

    public String showDashboard(Model model, Principal principal) {
        String loggedUserName = principal.getName();
        User user = userService.findByUsername(loggedUserName);
        model.addAttribute("username",user.getUsername());
        model.addAttribute("score", user.getScore());
        List<Course> recommendedCourses = recommendationService.recommendCourse(user.getId());
        model.addAttribute("recommendedCourses", recommendedCourses);
        List<Enrollment> enrollments = enrollmentRepository.findByUser(user);
        model.addAttribute("enrollments", enrollments);
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