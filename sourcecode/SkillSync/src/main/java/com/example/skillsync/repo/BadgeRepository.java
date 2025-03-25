package com.example.skillsync.repo;

import com.example.skillsync.model.Badge;
import com.example.skillsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByUser(User user);
    boolean existsByUserAndBadgeName(User user, String badgeName);
}