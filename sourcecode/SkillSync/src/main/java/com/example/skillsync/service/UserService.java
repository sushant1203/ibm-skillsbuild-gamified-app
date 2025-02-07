package com.example.skillsync.service;

import com.example.skillsync.model.User;
import com.example.skillsync.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    // injects UserRepository into the class
    @Autowired
    private UserRepository userRepository;


    // interface for the user repository
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // handles the registration of the user
    public void registerUser(String name, String email, String username, String password) {
        //create a user object, populate its attributes and save it
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setScore(0);//set a default score at registration(0)
        userRepository.save(user);
    }
}
