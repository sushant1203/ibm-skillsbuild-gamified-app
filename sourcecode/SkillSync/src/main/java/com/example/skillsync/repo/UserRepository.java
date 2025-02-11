package com.example.skillsync.repo;
import com.example.skillsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;



// This interface extends the JpaRepository interface which provides CRUD operations for the User entity.
public interface UserRepository extends JpaRepository<User, Long> {

    // find user by username
    User findByUsername(String username);

    // find user by email
    User findByEmail(String email);
}

