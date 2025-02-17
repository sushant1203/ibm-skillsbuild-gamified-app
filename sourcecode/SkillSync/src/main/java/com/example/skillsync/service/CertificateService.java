package com.example.skillsync.service;

import com.example.skillsync.model.Certificate;
import com.example.skillsync.model.User;
import com.example.skillsync.repo.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    public List<Certificate> getCertificatesForUser(User user, Optional<String> sortBy, Optional<String> filterBy) {
        List<Certificate> certificates = certificateRepository.findByUser(user);

        System.out.println("Fetching certificates for user with ID: " + user.getId());
        System.out.println("Number of certificates found: " + certificates.size());

//Filtering example: by course title
        if (filterBy.isPresent() && !filterBy.get().isEmpty()) {
            String filter = filterBy.get().toLowerCase();
            certificates.removeIf(cert -> !cert.getCourse().getTitle().toLowerCase().contains(filter));
        }

        //Sorting example: by issue date or course title
        if (sortBy.isPresent()) {
            switch (sortBy.get()) {
                case "createdAt":
                    certificates.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
                    break;
                case "courseTitle":
                    certificates.sort((a, b) -> a.getCourse().getTitle().compareToIgnoreCase(b.getCourse().getTitle()));
                    break;
            }
        }
        return certificates;
    }
}