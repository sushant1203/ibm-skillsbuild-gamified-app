package com.example.skillsync.service;

import com.example.skillsync.model.Option;
import com.example.skillsync.model.Question;
import com.example.skillsync.model.Quiz;
import com.example.skillsync.repo.OptionRepository;
import com.example.skillsync.repo.QuestionRepository;
import com.example.skillsync.repo.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


import java.util.List;
import java.util.Map;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    // Fetch the quiz data from the database
    public Quiz getQuizData(Long courseId) {
        Quiz quiz = quizRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + courseId));
        return quiz;
    }

    // Check if the selected option is correct
    public boolean isOptionCorrect(Long optionId) {
        return optionRepository.findById(optionId)
                .map(Option::isCorrect)
                .orElse(false);
    }

    // Calculate the score based on the answers submitted
    public int calculateScore(Long courseId, Map<String, String> answers) {
        System.out.println("Answers: " + answers);
        Quiz quiz = getQuizData(courseId);
        int totalQuestions = quiz.getQuestions().size();
        int correctCount = 0;

        // Loop through each question and check if the selected option is correct
        for (Question question : quiz.getQuestions()) {
            String paramKey = "answer_" + question.getId(); // Construct the parameter key
            String answer = answers.get(paramKey); // Get the selected option
            System.out.println("Answer: " + answer);
            if (answer != null) {
                try {
                    Long selectedOptionId = Long.parseLong(answer);
                    if (isOptionCorrect(selectedOptionId)) {
                        // Increment the correct count if the selected option is correct
                        correctCount++;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid option ID: " + answer);
                }
            }
        }
        return (int) ((correctCount / (double) totalQuestions) * 100);
    }
}