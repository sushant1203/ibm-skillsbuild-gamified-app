package com.example.skillsync.controller;

import com.example.skillsync.model.User;
import com.example.skillsync.service.EmailService;
import com.example.skillsync.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
class EmailTestController {
    private final EmailService emailService;
    private final UserService userService;

    public EmailTestController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @GetMapping("/test")
    public String testEmail() {
        User testUser = userService.findByEmail("test@example.com");
        if (testUser != null) {
            try {
                emailService.sendReminderEmail(testUser);
                return "Test email sent to " + testUser.getEmail();
            } catch (Exception e) {
                return "Failed to send test email: " + e.getMessage();
            }
        }
        return "Test user not found";
    }

    @GetMapping("/test-all")
    public String testAllEmails() {
        try {
            emailService.sendNotificationToAllUsers(
                    "SkillSync System Test",
                    "This is a test email to verify our notification system is working."
            );
            return "Test emails sent to all users";
        } catch (Exception e) {
            return "Failed to send emails to all users: " + e.getMessage();
        }
    }
}