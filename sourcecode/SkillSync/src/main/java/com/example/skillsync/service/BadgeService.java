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
        badgeThresholds.put("Bronze 2", 0);
        badgeThresholds.put("Bronze 1", 15);
        badgeThresholds.put("Silver 2", 30);
        badgeThresholds.put("Silver 1", 45);
        badgeThresholds.put("Gold 2", 70);
        badgeThresholds.put("Gold 1", 90);
        badgeThresholds.put("Platinum 2", 110);
        badgeThresholds.put("Platinum 1", 130);
        badgeThresholds.put("Diamond 2", 150);
        badgeThresholds.put("Diamond 1", 170);
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