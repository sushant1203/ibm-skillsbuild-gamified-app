package com.example.skillsync.service;


import com.example.skillsync.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardService {

    @Autowired
    private UserService userService;


    public List<User> getTopUsers(int limit) {
        return userService.getTopUsersByScore(limit);
    }

}
