package com.example.skillsync.controller;

import com.example.skillsync.model.Quiz;
import com.example.skillsync.repo.QuizRepository;
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
            // Need to save the score in the database
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


