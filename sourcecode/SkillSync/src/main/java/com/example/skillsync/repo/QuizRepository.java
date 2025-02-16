package com.example.skillsync.repo;

import com.example.skillsync.model.Course;
import com.example.skillsync.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByCourseId(Long courseId);
    List<Quiz> findByCourse(Course course);
}
