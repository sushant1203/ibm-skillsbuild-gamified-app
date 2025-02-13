package com.example.skillsync.controller;

import com.example.skillsync.model.User;
import com.example.skillsync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

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

    //displays the login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    //shows the user dashboard after login
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";
    }

    @GetMapping("/")
    public String showHome() {
        return "landing";
    }
    @GetMapping("/courses")
    public String showCourses() {
        return "courses";
    }

}