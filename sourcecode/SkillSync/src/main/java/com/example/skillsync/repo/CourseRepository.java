package com.example.skillsync.repo;

import com.example.skillsync.model.Course;
import com.example.skillsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository  extends JpaRepository<Course, Long> {


}
