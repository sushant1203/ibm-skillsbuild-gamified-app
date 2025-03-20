package com.example.skillsync.repo;

import com.example.skillsync.model.Enrollment;
import com.example.skillsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByUser(User user); // this automatically fetches all enrollments linked to a specific user

    boolean existsByUserIdAndCourseId(Long id, Long courseId);
}
