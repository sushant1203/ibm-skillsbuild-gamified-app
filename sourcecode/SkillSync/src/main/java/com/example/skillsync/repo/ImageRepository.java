package com.example.skillsync.repo;

import com.example.skillsync.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByName(String name);
    void deleteAllByName(String name);
}
