package com.example.skillsync.service;

import com.example.skillsync.model.Badge;
import com.example.skillsync.model.User;
import com.example.skillsync.repo.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class BadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    // Define badge thresholds in order (LinkedHashMap preserves insertion order)
    private static final Map<String, Integer> badgeThresholds = new LinkedHashMap<>();
    static {
        badgeThresholds.put("Bronze II", 0);
        badgeThresholds.put("Bronze I", 15);
        badgeThresholds.put("Silver II", 30);
        badgeThresholds.put("Silver I", 45);
        badgeThresholds.put("Gold II", 70);
        badgeThresholds.put("Gold I", 90);
        badgeThresholds.put("Platinum II", 110);
        badgeThresholds.put("Platinum I", 130);
        badgeThresholds.put("Diamond II", 150);
        badgeThresholds.put("Diamond I", 170);
    }

    /**
     * Check the user's current score and award any badge(s) that the user qualifies for
     * but hasn't been awarded yet.
     */
    public void checkAndAwardBadges(User user) {
        int userScore = user.getScore();
        for (Map.Entry<String, Integer> entry : badgeThresholds.entrySet()) {
            String badgeName = entry.getKey();
            int pointsRequired = entry.getValue();
            if (userScore >= pointsRequired && !badgeRepository.existsByUserAndBadgeName(user, badgeName)) {
                Badge badge = new Badge();
                badge.setUser(user);
                badge.setBadgeName(badgeName);
                badge.setPointsRequired(pointsRequired);
                badge.setAwardedAt(LocalDateTime.now());
                badgeRepository.save(badge);
                System.out.println("Awarded badge: " + badgeName + " to user: " + user.getUsername());
            }
        }
    }
}