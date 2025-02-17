package com.example.skillsync.service;

import com.example.skillsync.model.Course;
import com.example.skillsync.model.Enrollment;
import com.example.skillsync.model.User;
import com.example.skillsync.repo.CourseRepository;
import com.example.skillsync.repo.EnrollmentRepository;
import com.example.skillsync.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService { // this allows this class to interact with the database
    private final EnrollmentRepository enrollmentRepository; //
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;



    @Autowired // it tells Spring automatically  injects instance of EnrollmentRepository, CourseRepository and UserRepository into this class
    public RecommendationService(EnrollmentRepository enrollmentRepository,
                                 CourseRepository courseRepository,
                                 UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;


    }
    public List<Course> recommendCourse(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Enrollment> enrollments = enrollmentRepository.findByUser(user);

        // Step 1: Get IDs of courses the user has already taken
        Set<Long> enrolledCourseIds = enrollments.stream()
                .map(enrollment -> enrollment.getCourse().getId())
                .collect(Collectors.toSet());

        Map<String, Integer> categoryCount = new HashMap<>();
        Map<String, Integer> difficultyCount = new HashMap<>();

        for (Enrollment enrollment : enrollments) {
            String category = enrollment.getCourse().getCategory();
            String difficulty = enrollment.getCourse().getDifficulty();
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            difficultyCount.put(difficulty, difficultyCount.getOrDefault(difficulty, 0) + 1);
        }

        String preferredCategory = categoryCount.isEmpty() ? null :
                Collections.max(categoryCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        String preferredDifficulty = difficultyCount.isEmpty() ? null :
                Collections.max(difficultyCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        Set<Course> recommendedCourses = new LinkedHashSet<>(); // Keeps order, removes duplicates

        // Step 2: Fetch courses based on user preferences (category & difficulty)
        if (preferredCategory != null && preferredDifficulty != null) {
            recommendedCourses.addAll(courseRepository.findByCategoryAndDifficulty(preferredCategory, preferredDifficulty));
        }
        if (recommendedCourses.size() < 4 && preferredCategory != null) {
            recommendedCourses.addAll(courseRepository.findByCategory(preferredCategory));
        }
        if (recommendedCourses.size() < 4 && preferredDifficulty != null) {
            recommendedCourses.addAll(courseRepository.findByDifficulty(preferredDifficulty));
        }

        // Step 3: Remove already enrolled courses
        recommendedCourses.removeIf(course -> enrolledCourseIds.contains(course.getId()));

        // Step 4: If we still have fewer than 4 courses, fetch random courses
        if (recommendedCourses.size() < 4) {
            List<Course> additionalCourses = courseRepository.findAll(); // Fetch all available courses
            additionalCourses.removeIf(course -> enrolledCourseIds.contains(course.getId())); // Remove already taken
            Collections.shuffle(additionalCourses); // Randomize order
            for (Course course : additionalCourses) {
                if (recommendedCourses.size() >= 4) break;
                recommendedCourses.add(course);
            }
        }

        // Convert to sorted list and return exactly 4 courses
        return recommendedCourses.stream()
                .sorted(Comparator.comparing(Course::getTitle)) // Sort alphabetically
                .limit(4) // Ensure exactly 4 recommendations
                .collect(Collectors.toList());
    }




}
