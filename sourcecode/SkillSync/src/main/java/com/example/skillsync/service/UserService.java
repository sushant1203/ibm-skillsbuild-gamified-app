package com.example.skillsync.service;

import com.example.skillsync.model.User;
import com.example.skillsync.repo.UserRepository;
import com.example.skillsync.utils.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // User retrieval methods
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : userRepository.findByUsername(authentication.getName());
    }

    // Registration and authentication methods
    public List<String> registerUser(String name, String email, String username, String password) {
        List<String> errors = new ArrayList<>();

        if (findByUsername(username) != null) {
            errors.add("Username already exists! Choose another one.");
        }
        if (findByEmail(email) != null) {
            errors.add("Email is already registered! Use another email.");
        }
        if (!PasswordValidator.isValid(password)) {
            errors.add("Password must contain at least 8 characters, one uppercase letter, one digit, and one special character.");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setScore(0);
        user.setStreak(1);
        user.setLastLogin(LocalDate.now());
        userRepository.save(user);

        return null;
    }

    public void reAuthenticate(User user) {
        UserDetails updatedUserDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );

        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                updatedUserDetails, user.getPassword(), updatedUserDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
    }

    // Profile editing methods
    public List<String> editUserName(String newUsername, String password) {
        User user = getLoggedInUser();
        List<String> errors = new ArrayList<>();

        if (user == null) {
            errors.add("User authentication error");
        }
        if (findByUsername(newUsername) != null) {
            errors.add("Username already exists! Choose another one.");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            errors.add("Incorrect password!");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        user.setUsername(newUsername);
        userRepository.save(user);
        reAuthenticate(user);
        return null;
    }

    public List<String> editPassword(String newPassword, String password, String newPasswordConfirm) {
        User user = getLoggedInUser();
        List<String> errors = new ArrayList<>();

        if (user == null) {
            errors.add("User authentication error");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            errors.add("Incorrect password!");
        }
        if (!newPassword.equals(newPasswordConfirm)) {
            errors.add("Passwords do not match!");
        }
        if (!PasswordValidator.isValid(newPassword)) {
            errors.add("Password must contain at least 8 characters, one uppercase letter, one digit, and one special character.");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        reAuthenticate(user);
        return null;
    }

    // get top 10 users by score
    public List<User> getTopUsersByScore(int limit) {
        return userRepository.findTopUsersByScore(PageRequest.of(0, limit));
    }

    public List<User> getTopFriendsUsersByScore(int limit) {

        return userRepository.findTopFriendsUsersByScore(getLoggedInUser().getId(), PageRequest.of(0, limit));
    }
    // Streak management
    public void setStreak(User user) {
        LocalDate lastLogin = user.getLastLogin();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        if (lastLogin != null) {
            if (lastLogin.equals(yesterday)) {
                user.setStreak(user.getStreak() + 1);
            } else if (lastLogin.isBefore(yesterday)) {
                user.setStreak(1);
            }
        } else {
            user.setStreak(1);
        }

        user.setLastLogin(today);
        userRepository.save(user);
    }
}


    // Additional methods can be added here
