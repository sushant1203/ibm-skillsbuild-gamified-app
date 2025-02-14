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
    public List<Course> recommendCourse(Long userId){ // this method takes a userId as input and returns a list of recommended courses
        User user = userRepository.findById(userId) // finds the User in the database
                .orElseThrow(() -> new RuntimeException("User not found")); // put exception if the user is not found put an error

        List<Enrollment> enrollments = enrollmentRepository.findByUser(user); // this retrieves all courses that the has been enrolled in

        if(enrollments.isEmpty()){ // it just  makes sure when the user has not taken recommendation course it still shows something
            return  courseRepository.findAll();
        }

        // two HashMaps are created
        // these maps store string keys mapped to integer values
        // the keys in these maps represent category and difficulty leve;
        Map<String,Integer> categoryCount = new HashMap<>();
        Map<String, Integer> difficultyCount = new HashMap<>();

        for (Enrollment enrollment: enrollments){ // it goes through all enrollment of user
            String category = enrollment.getCourse().getCategory(); // it extracts the course category
            String difficulty = enrollment.getCourse().getDifficulty(); //it extracts the course difficulty
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            difficultyCount.put(difficulty, difficultyCount.getOrDefault(difficulty, 0) + 1);
        }
        String preferredCategory = categoryCount.isEmpty() ? null :  // if category count is empty, it sets preferredCategory to null otherwise it proceeds the mos frequent
                Collections.max(categoryCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        String preferredDifficulty = difficultyCount.isEmpty() ? null :// if category count is empty, it sets preferredCategory to null otherwise it proceeds the most frequent
                Collections.max(difficultyCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        List<Course> recommendedCourses;

        if (preferredCategory != null && preferredDifficulty != null) {
            recommendedCourses = courseRepository.findByCategoryAndDifficulty(preferredCategory, preferredDifficulty);
        } else {
            recommendedCourses = courseRepository.findAll(); // Fallback
        }

        recommendedCourses.sort(Comparator.comparing(Course::getTitle));
        return recommendedCourses;
    }

}
