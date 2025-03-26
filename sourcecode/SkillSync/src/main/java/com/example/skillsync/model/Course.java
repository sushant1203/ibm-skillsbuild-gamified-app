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
    private String links;
    private String imagePath;

    
    public Course() {}

    public Course(String title, String category, String difficulty, int courseScore, String links,String imagePath ) {
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.courseScore = courseScore;
        this.links = links;
        this.imagePath = imagePath;

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

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

}
