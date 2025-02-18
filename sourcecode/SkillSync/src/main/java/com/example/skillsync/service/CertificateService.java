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

    public List<Certificate> getCertificatesForUser(User user,
                                                    Optional<String> sortBy,
                                                    Optional<String> search,
                                                    Optional<String> category,
                                                    Optional<String> difficulty) {
        List<Certificate> certificates = certificateRepository.findByUser(user);

        System.out.println("Fetching certificates for user with ID: " + user.getId());
        System.out.println("Number of certificates found: " + certificates.size());

        // Filtering by search term on course title
        if (search.isPresent() && !search.get().isEmpty()) {
            String searchTerm = search.get().toLowerCase();
            certificates.removeIf(cert -> !cert.getCourse().getTitle().toLowerCase().contains(searchTerm));
        }

        // Filtering by course category
        if (category.isPresent() && !category.get().isEmpty()) {
            String catFilter = category.get().toLowerCase();
            certificates.removeIf(cert -> !cert.getCourse().getCategory().toLowerCase().equals(catFilter));
        }

        // Filtering by course difficulty
        if (difficulty.isPresent() && !difficulty.get().isEmpty()) {
            String diffFilter = difficulty.get().toLowerCase();
            certificates.removeIf(cert -> !cert.getCourse().getDifficulty().toLowerCase().equals(diffFilter));
        }

        // Sorting by most recent (createdAt)
        if (sortBy.isPresent() && sortBy.get().equals("mostRecent")) {
            certificates.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        }

        return certificates;
    }
}