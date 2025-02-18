package com.example.skillsync.service;

import com.example.skillsync.model.Certificate;
import com.example.skillsync.model.Course;
import com.example.skillsync.model.User;
import com.example.skillsync.repo.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    // Method to automatically award a certificate when a quiz is passed
    public void awardCertificate(User user, Course course, int quizScore) {
        // Check if a certificate for this course already exists for the user
        List<Certificate> existingCertificates = certificateRepository.findByUser(user);
        boolean alreadyAwarded = existingCertificates.stream()
                .anyMatch(cert -> cert.getCourse().getId().equals(course.getId()));
        if (alreadyAwarded) {
            System.out.println("Certificate already awarded for this course.");
            return;
        }

        Certificate certificate = new Certificate();
        certificate.setUser(user);
        certificate.setCourse(course);
        certificate.setQuizScore(quizScore);
        certificate.setCreatedAt(LocalDateTime.now());

        certificateRepository.save(certificate);
        System.out.println("Certificate awarded for course: " + course.getTitle());
    }
}