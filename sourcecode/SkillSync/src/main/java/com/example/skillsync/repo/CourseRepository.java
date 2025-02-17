package com.example.skillsync.repo;

import com.example.skillsync.model.Course;
import com.example.skillsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository  extends JpaRepository<Course, Long> {
    Course findByTitle(String title);
    List<Course> findByCategoryAndDifficulty(String category,String difficulty);
    boolean existsByTitle(String title);

}
