package com.example.skillsync.repo;

import com.example.skillsync.model.Certificate;
import com.example.skillsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByUser(User user);

    List<Certificate> findByCourse_Id(Long courseId);
}