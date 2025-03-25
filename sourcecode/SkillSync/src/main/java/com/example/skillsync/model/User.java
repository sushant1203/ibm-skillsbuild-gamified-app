package com.example.skillsync.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "user")  // Matches your existing SQL table name
public class User {

    // No-args constructor (required by JPA)
    public User() {
        this.score = 0;
        this.lastNotifiedDate = LocalDate.now().minusDays(3);
        this.incompleteCourseIds = new ArrayList<>();
        this.hasIncompleteCourses = false;
        this.streak = 0;
        this.sentRequests = new ArrayList<>();
        this.receivedRequests = new ArrayList<>();
        this.friendshipsInitiated = new ArrayList<>();
        this.friendshipsReceived = new ArrayList<>();
    }

    // Custom constructor
    public User(String username, String email, String password, String name) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.score = 0;
        this.lastNotifiedDate = LocalDate.now().minusDays(3);
        this.incompleteCourseIds = new ArrayList<>();
        this.hasIncompleteCourses = false;
        this.streak = 0;
        this.sentRequests = new ArrayList<>();
        this.receivedRequests = new ArrayList<>();
        this.friendshipsInitiated = new ArrayList<>();
        this.friendshipsReceived = new ArrayList<>();
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

    @Column(name = "has_incomplete_courses", nullable = false)
    private boolean hasIncompleteCourses;

    @ElementCollection
    @CollectionTable(name = "user_incomplete_courses", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "course_id")
    private List<Long> incompleteCourseIds;

    private LocalDate lastLogin;

    private int streak;

    // Friend Requests Sent
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<FriendRequest> sentRequests;

    // Friend Requests Received
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<FriendRequest> receivedRequests;

    // Friendships (One user can have many friendships)
    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    private List<Friendship> friendshipsInitiated;

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
    private List<Friendship> friendshipsReceived;

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
        this.incompleteCourseIds = incompleteCourseIds != null ? incompleteCourseIds : new ArrayList<>();
        this.hasIncompleteCourses = !this.incompleteCourseIds.isEmpty();
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public List<FriendRequest> getSentRequests() { return sentRequests; }
    public void setSentRequests(List<FriendRequest> sentRequests) { this.sentRequests = sentRequests; }

    public List<FriendRequest> getReceivedRequests() { return receivedRequests; }
    public void setReceivedRequests(List<FriendRequest> receivedRequests) { this.receivedRequests = receivedRequests; }

    public List<Friendship> getFriendshipsInitiated() { return friendshipsInitiated; }
    public void setFriendshipsInitiated(List<Friendship> friendshipsInitiated) { this.friendshipsInitiated = friendshipsInitiated; }

    public List<Friendship> getFriendshipsReceived() { return friendshipsReceived; }
    public void setFriendshipsReceived(List<Friendship> friendshipsReceived) { this.friendshipsReceived = friendshipsReceived; }
}