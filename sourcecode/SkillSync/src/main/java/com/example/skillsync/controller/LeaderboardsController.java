package com.example.skillsync.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LeaderboardsController {

    @GetMapping("/leaderboards")
    public String showLeaderboards() {
        // Simply return the leaderboards view (leaderboards.html)
        return "leaderboards";
    }
}