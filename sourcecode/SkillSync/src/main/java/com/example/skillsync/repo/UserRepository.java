package com.example.skillsync.repo;

import com.example.skillsync.model.User;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.domain.Pageable;

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

    // find top users by score
    @Query("SELECT u FROM User u ORDER BY u.score DESC")
    List<User> findTopUsersByScore(Pageable pageable);

    // find top friends users by score
    @Query("SELECT u FROM User u WHERE u.id IN " +
           "(SELECT f.user2.id FROM Friendship f WHERE f.user1.id = :userId " +
           "UNION " +
           "SELECT f.user1.id FROM Friendship f WHERE f.user2.id = :userId) " +
           "ORDER BY u.score DESC")
    List<User> findTopFriendsUsersByScore(Long userId, Pageable pageable);
}
