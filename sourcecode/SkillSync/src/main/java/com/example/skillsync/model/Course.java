package com.example.skillsync.model;

import jakarta.persistence.*;

@Entity //create a table
@Table //map it to a database(db) table
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //db automatically generates unique ids when a new entity is saved
    private Long id;
    private String title;
    private String category;
    private String difficulty;
    private int courseScore;

    public Course(String title, String category, String difficulty, int courseScore) {
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.courseScore = courseScore;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long courseId) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public int getCourseScore() {
        return courseScore;
    }

    public void setCourseScore(int courseScore) {
        this.courseScore = courseScore;
    }



}
