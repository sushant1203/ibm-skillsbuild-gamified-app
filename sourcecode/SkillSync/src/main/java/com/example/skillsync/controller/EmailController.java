package com.example.skillsync.controller;

import com.example.skillsync.model.User;
import com.example.skillsync.service.EmailService;
import com.example.skillsync.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

@RestController
@RequestMapping("/test-email")
public class EmailController {
    private final EmailService emailService;
    private final UserService userService;

    public EmailController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    // Existing endpoint
    @GetMapping("/send")
    public String sendTestEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return "User with email " + email + " not found!";
        }

        Context context = new Context();
        context.setVariable("username", user.getUsername());
        context.setVariable("message", "This is a reminder to complete your course!");

        try {
            emailService.sendHtmlEmail(
                    email,
                    "Reminder Email",
                    "email-template",
                    context
            );
            return "Reminder email sent to " + email + "!";
        } catch (MessagingException e) {
            return "Failed to send email: " + e.getMessage();
        }
    }

    // NEW TEST ENDPOINT
    @GetMapping("/test-system")
    public String testEmailSystem() {
        String testEmail = "test@example.com"; // Change to your test email
        User user = userService.findByEmail(testEmail);

        if (user == null) {
            return "⚠️ Test failed: User " + testEmail + " not found. Create test user first.";
        }

        try {
            // Test plain text email
            emailService.sendReminderEmail(user);

            // Test HTML email
            Context context = new Context();
            context.setVariable("username", user.getUsername());
            context.setVariable("message", "This is a SYSTEM TEST email");

            emailService.sendHtmlEmail(
                    testEmail,
                    "System Test Email",
                    "email-template",
                    context
            );

            return "System test completed. Check inbox for emails at " + testEmail;
        } catch (MessagingException e) {
            return "Test failed: " + e.getMessage();
        }
    }
}