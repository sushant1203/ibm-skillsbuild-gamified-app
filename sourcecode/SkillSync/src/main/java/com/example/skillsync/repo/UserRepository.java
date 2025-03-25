package com.example.skillsync.repo;

import com.example.skillsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

// This interface extends the JpaRepository interface, which provides CRUD operations for the User entity.
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username
    User findByUsername(String username);

    // Find user by email
    User findByEmail(String email);

    // Custom query to find users who haven't been notified in the last 3 days
    List<User> findByLastNotifiedDateBefore(LocalDate date);

    // New method: Find users with incomplete courses
    List<User> findByHasIncompleteCoursesTrue();
}
