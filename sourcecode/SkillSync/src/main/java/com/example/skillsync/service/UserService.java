package com.example.skillsync.service;

import com.example.skillsync.model.User;
import com.example.skillsync.repo.UserRepository;
import com.example.skillsync.utils.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
}
