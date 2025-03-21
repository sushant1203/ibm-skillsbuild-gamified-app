package com.example.skillsync.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "badge")
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String badgeName;
    private int pointsRequired;
    private LocalDateTime awardedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    // Constructors
    public Badge() {
    }

    public Badge(String badgeName, int pointsRequired, LocalDateTime awardedAt, User user) {
        this.badgeName = badgeName;
        this.pointsRequired = pointsRequired;
        this.awardedAt = awardedAt;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public int getPointsRequired() {
        return pointsRequired;
    }

    public void setPointsRequired(int pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

    public LocalDateTime getAwardedAt() {
        return awardedAt;
    }

    public void setAwardedAt(LocalDateTime awardedAt) {
        this.awardedAt = awardedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}