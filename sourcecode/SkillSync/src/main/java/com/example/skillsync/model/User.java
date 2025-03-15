package com.example.skillsync.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table
public class User {

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

    // Getters and setters
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

    public List<FriendRequest> getSentRequests() { return sentRequests; }
    public void setSentRequests(List<FriendRequest> sentRequests) { this.sentRequests = sentRequests; }

    public List<FriendRequest> getReceivedRequests() { return receivedRequests; }
    public void setReceivedRequests(List<FriendRequest> receivedRequests) { this.receivedRequests = receivedRequests; }

    public List<Friendship> getFriendshipsInitiated() { return friendshipsInitiated; }
    public void setFriendshipsInitiated(List<Friendship> friendshipsInitiated) { this.friendshipsInitiated = friendshipsInitiated; }

    public List<Friendship> getFriendshipsReceived() { return friendshipsReceived; }
    public void setFriendshipsReceived(List<Friendship> friendshipsReceived) { this.friendshipsReceived = friendshipsReceived; }
}
