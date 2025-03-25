package com.example.skillsync.service;

import com.example.skillsync.model.User;
import com.example.skillsync.repo.UserRepository;
import com.example.skillsync.utils.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class UserService {

    // Dependency injection
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    // interface for the user repository
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // handles the registration of the user
    public List<String> registerUser(String name, String email, String username, String password) {
        List<String> errors = new ArrayList<>();

        //check if username or email already exists
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

        //create a user object, populate its attributes and save it
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setScore(0);//set a default score at registration(0)
        userRepository.save(user);

        return null;
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Using security to find logged in user
        return authentication == null ? null : userRepository.findByUsername(authentication.getName()); // Returns null if no user logged in else returns user
    }

    public void reAuthenticate(User user) {
        UserDetails updatedUserDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("USER")) // Adding security auth status
        );

        // Create an Authentication object using the updated UserDetails
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                updatedUserDetails, user.getPassword(), updatedUserDetails.getAuthorities()
        );

        // Set the updated authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

    }

    public List<String> editUserName(String newUsername, String password) {
        User user = getLoggedInUser();
        List<String> errors = new ArrayList<>();// Getting current user
        if (user == null) {
            errors.add("User authentication error"); // Not logged in
        }
        if (findByUsername(newUsername) != null) {
            errors.add("Username already exists! Choose another one.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) { // Checking password
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
        List<String> errors = new ArrayList<>();// Getting current user
        if (user == null) {
            errors.add("User authentication error"); // Not logged in
        }

        if (!passwordEncoder.matches(password, user.getPassword())) { // Checking password
            errors.add("Incorrect password!");

        }

        if (!newPassword.equals(newPasswordConfirm)) { // Match error
            errors.add("Passwords do not match!");

        }

        if (!PasswordValidator.isValid(newPassword)) { // Checks against password validation
            errors.add("Password must contain at least 8 characters, one uppercase letter, one digit, and one special character.");
        }

        if (!errors.isEmpty()) {
            return errors;
        }
        user.setPassword(passwordEncoder.encode(newPassword)); // Encodes for security
        userRepository.save(user);
        reAuthenticate(user);
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
