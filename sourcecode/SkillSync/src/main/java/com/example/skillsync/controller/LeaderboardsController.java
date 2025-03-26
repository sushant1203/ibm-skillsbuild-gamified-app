package com.example.skillsync.controller;
import com.example.skillsync.model.User;
import com.example.skillsync.service.LeaderboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LeaderboardsController {

    private LeaderboardService leaderboardService;

    public LeaderboardsController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    // This method is called when the user navigates to the /leaderboards endpoint
// The limit parameter is used to specify the number of top users to retrieve
    @GetMapping("/leaderboards")
    public String showLeaderboard(@RequestParam(defaultValue = "10") int limit,
                                  @RequestParam(defaultValue = "global") String type,
                                  Model model) {
        // Retrieve the top users from the leaderboard service based on the type
        List<User> topUsers;
        if ("friends".equals(type)) {
            topUsers = leaderboardService.getTopFriendsUsers(limit);
        } else {
            topUsers = leaderboardService.getTopUsers(limit);
        }

        // Logging purposes
        System.out.println("Top users size: " + topUsers.size() + " and limit: " + limit);
        for (User user : topUsers) {
            System.out.println("Username: " + user.getUsername() + ", Name: " + user.getName() + ", Score: " + user.getScore());
        }

        model.addAttribute("topUsers", topUsers);
        model.addAttribute("limit", limit);
        model.addAttribute("type", type);

        return "leaderboards";
    }
}