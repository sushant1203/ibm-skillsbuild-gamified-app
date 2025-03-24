package com.example.skillsync.repo;
import com.example.skillsync.model.User;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.domain.Pageable;


// This interface extends the JpaRepository interface which provides CRUD operations for the User entity.
public interface UserRepository extends JpaRepository<User, Long> {

    // find user by username
    User findByUsername(String username);

    // find user by email
    User findByEmail(String email);

    // find top users by score
    @Query("SELECT u FROM User u ORDER BY u.score DESC")
    List<User> findTopUsersByScore(Pageable pageable);

    // find top friends users by score
    @Query("SELECT u FROM User u WHERE u.id IN (SELECT f.user2.id FROM Friendship f WHERE f.user1.id = :userId) ORDER BY u.score DESC")
    List<User> findTopFriendsUsersByScore(Long userId, Pageable pageable);
}

