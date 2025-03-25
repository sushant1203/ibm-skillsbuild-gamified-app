package com.example.skillsync.service;

import com.example.skillsync.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserService userService;

    @Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine, UserService userService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.userService = userService;
    }

    public void sendReminderEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("SkillSync - Course Reminder");
        message.setText("Dear " + user.getUsername() + ",\n\nThis is a reminder to complete your courses on SkillSync!");
        mailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String templateName, Context context) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String htmlContent = templateEngine.process(templateName, context);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    public void sendNotificationToAllUsers(String subject, String messageContent) {
        List<User> allUsers = userService.getAllUsers();

        for (User user : allUsers) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject(subject);
                message.setText("Dear " + user.getUsername() + ",\n\n" + messageContent);
                mailSender.send(message);
            } catch (Exception e) {
                System.err.println("Failed to send email to " + user.getEmail() + ": " + e.getMessage());
            }
        }
    }

    public void sendHtmlNotificationToAllUsers(String subject, String templateName, String message) {
        List<User> allUsers = userService.getAllUsers();

        for (User user : allUsers) {
            try {
                Context context = new Context();
                context.setVariable("username", user.getUsername());
                context.setVariable("message", message);

                sendHtmlEmail(user.getEmail(), subject, templateName, context);
            } catch (Exception e) {
                System.err.println("Failed to send HTML email to " + user.getEmail() + ": " + e.getMessage());
            }
        }
    }

}