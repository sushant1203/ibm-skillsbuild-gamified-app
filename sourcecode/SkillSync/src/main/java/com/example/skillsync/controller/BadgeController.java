package com.example.skillsync.controller;

import com.example.skillsync.model.User;
import com.example.skillsync.service.BadgeService;
import com.example.skillsync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class BadgeController {

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private UserService userService;

    @GetMapping("/badges")
    public String showBadges(Model model) {
        User user = userService.getLoggedInUser();
        if (user == null) {
            return "redirect:/login";
        }
        // Award badges if necessary
        badgeService.checkAndAwardBadges(user);

        model.addAttribute("score", user.getScore());

        // Retrieve all badge data as a list of maps
        List<Map<String, Object>> allBadges = badgeService.getAllBadgeDataForUser(user);
        model.addAttribute("allBadges", allBadges);

        // Determine current badge as the highest one (by points) that is owned
        Map<String, Object> currentBadge = allBadges.stream()
                .filter(b -> (Boolean) b.get("owned"))
                .reduce((first, second) -> second)
                .orElse(null);
        model.addAttribute("currentBadge", currentBadge);

        return "badges";
    }
}