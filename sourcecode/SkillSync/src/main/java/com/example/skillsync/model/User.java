package com.example.skillsync.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "users")  // Explicit table name to avoid SQL keyword conflicts
public class User {

    // No-args constructor (required by JPA)
    public User() {
        this.score = 0;
        this.lastNotifiedDate = LocalDate.now().minusDays(3);
        this.incompleteCourseIds = new ArrayList<>();  // Initialize list
    }

    // Your custom constructor
    public User(String username, String email, String password, String name) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.score = 0;
        this.lastNotifiedDate = LocalDate.now().minusDays(3);
        this.incompleteCourseIds = new ArrayList<>();  // Initialize list
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int score;

    @Column(name = "last_notified_date")
    private LocalDate lastNotifiedDate;

    @Column(name = "has_incomplete_courses")
    private boolean hasIncompleteCourses = false;

    @ElementCollection
    @CollectionTable(name = "user_incomplete_courses", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "course_id")
    private List<Long> incompleteCourseIds;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public LocalDate getLastNotifiedDate() { return lastNotifiedDate; }
    public void setLastNotifiedDate(LocalDate lastNotifiedDate) {
        this.lastNotifiedDate = lastNotifiedDate;
    }

    public boolean isHasIncompleteCourses() {
        return hasIncompleteCourses;
    }

    public void setHasIncompleteCourses(boolean hasIncompleteCourses) {
        this.hasIncompleteCourses = hasIncompleteCourses;
    }

    public List<Long> getIncompleteCourseIds() {
        return incompleteCourseIds;
    }

    public void setIncompleteCourseIds(List<Long> incompleteCourseIds) {
        this.incompleteCourseIds = incompleteCourseIds;
        this.hasIncompleteCourses = !incompleteCourseIds.isEmpty();
    }

}