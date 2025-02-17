package com.example.skillsync.repo;

import com.example.skillsync.model.Course;
import com.example.skillsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository  extends JpaRepository<Course, Long> {
    List<Course> findByCategory(String category);
    List<Course> findByDifficulty(String difficulty);
    List<Course> findByCategoryAndDifficulty(String category,String difficulty);
    boolean existsByTitle(String title);
    List<Course> findByTitle(String title);

}
