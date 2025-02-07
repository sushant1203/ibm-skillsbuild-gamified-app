package com.example.skillsync.controller;

import com.example.skillsync.model.User;
import com.example.skillsync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        //check if username or email already exists
        if (userService.findByUsername(username) != null) {
            model.addAttribute("error","username already exists!choose another one.");
            return "register";//reloads registration page with error message, applies same with email
        }
        if (userService.findByEmail(email) != null) {
            model.addAttribute("error","email is already registered!use another email.");
            return "register";
        }

        // UserService handles the registration of the user
        userService.registerUser( name, email, username, password);

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
        return "home";
    }

}