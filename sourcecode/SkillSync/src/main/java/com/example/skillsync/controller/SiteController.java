package com.example.skillsync.controller;

import com.example.skillsync.model.User;
import com.example.skillsync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SiteController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user") // This function allows the current logged in user info to be accessed across the whole site (when logged in)
    public User addUserToModel(Model model) { // Needed to add user to all pages for profile pictures
        User user = userService.getLoggedInUser();
        if (user == null) {
            return null;  // No login check
        }
        model.addAttribute("user", user);
        return user;
    }
}
