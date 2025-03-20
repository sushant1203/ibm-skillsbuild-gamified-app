package com.example.skillsync.repo;
import com.example.skillsync.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Custom method to find all questions for a given quiz
    List<Question> findByQuizId(Long quizId);
}
