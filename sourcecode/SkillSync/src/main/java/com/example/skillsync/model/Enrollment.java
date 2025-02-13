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
    private Course courses;


}
