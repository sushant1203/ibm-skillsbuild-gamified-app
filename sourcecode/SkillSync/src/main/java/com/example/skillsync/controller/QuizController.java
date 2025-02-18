package com.example.skillsync.controller;

import com.example.skillsync.model.Course;
import com.example.skillsync.model.Enrollment;
import com.example.skillsync.model.Quiz;
import com.example.skillsync.model.User;
import com.example.skillsync.repo.*;
import com.example.skillsync.service.CertificateService;
import com.example.skillsync.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

@Controller
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CertificateService certificateService;


    // Display the quiz
    @GetMapping("quiz/{courseId}")
    public String showQuiz(@PathVariable Long courseId, Model model) {
        // Fetch the quiz data from the database
        Quiz quiz = quizService.getQuizData(courseId);
        model.addAttribute("quiz", quiz);
        model.addAttribute("courseId", courseId);

        return "quiz";
    }

    // Handle the quiz submission
    @PostMapping("/quiz/{courseId}")
    public String submitQuiz(@PathVariable Long courseId,
                             @RequestParam Map<String, String> answers,
                             Principal principal,
                             Model model) {
        // Calculate the score based on the answers submitted
        int scorePercentage = quizService.calculateScore(courseId, answers);
        model.addAttribute("score", scorePercentage);

        // Redirect to the success or failure page based on the score
        if (scorePercentage >= 80) {
            // Fetch the current user
            User user = userRepository.findByUsername(principal.getName());
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
            // Check if the user has already completed the course
            boolean isEnrolled = enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId);

            if (!isEnrolled) {
                // If the player did not complete the course then, the user's score is updated and a new enrollment entry is created
                user.setScore(user.getScore() + course.getCourseScore());
                userRepository.save(user);
                System.out.println("User score updated: " + user.getScore());
                Enrollment enrollment = new Enrollment();
                enrollment.setUser(user);
                enrollment.setCourse(courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found")));
                enrollmentRepository.save(enrollment);
            }
            certificateService.awardCertificate(user, course, scorePercentage);
            return "redirect:/quiz/success";
        } else {
            return "redirect:/quiz/failed";
        }
    }

    // Display the success page
    @GetMapping("/quiz/success")
    public String successPage() {
        return "quiz_success";
    }

    // Display the failure page
    @GetMapping("/quiz/failed")
    public String failedPage() {
        return "quiz_fail";
    }

}


