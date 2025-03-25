package com.example.skillsync.service;

import com.example.skillsync.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScheduledEmailService {

    private final EmailService emailService;
    private final UserService userService;

    public ScheduledEmailService(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    // Runs at 9:00 AM every day
    @Scheduled(cron = "0 0 9 * * *")  // Second Minute Hour Day Month Weekday
    public void sendDailyNotifications() {
        System.out.println("Starting daily email notifications...");

        List<User> users = userService.getAllUsers();
        int successCount = 0;
        int failCount = 0;

        for (User user : users) {
            try {
                emailService.sendReminderEmail(user);
                successCount++;
                System.out.println("Sent daily email to: " + user.getEmail());
            } catch (Exception e) {
                failCount++;
                System.err.println("Failed to send to " + user.getEmail() + ": " + e.getMessage());
            }
        }

        System.out.printf("Daily emails completed. Success: %d, Failed: %d%n",
                successCount, failCount);
    }
}