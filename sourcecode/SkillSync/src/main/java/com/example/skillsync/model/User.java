package com.example.skillsync.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity //create a table
@Table //map it to a database(db) table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //db automatically generates unique ids when a new entity is saved
    private Long id;

    @Column(nullable = false) //the column "name" cannot be empty, same applies to the other fields
    private String name;

    @Column(nullable = false,unique = true) //two emails cannot be the same in the db
    private String email;

    @Column(nullable = false, unique = true) //two usernames cannot be the same in the db
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int score;

    private LocalDate lastLogin;

    private int streak;

    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
}