package com.example.skillsync.model;

import jakarta.persistence.*;

@Entity //create a table
@Table //map it to a database(db) table
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //db automatically generates unique ids when a new entity is saved

    private Long id;
    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name ="course_id")
    private Course course;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }





}
