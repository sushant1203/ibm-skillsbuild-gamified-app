package com.example.skillsync.repo;
import com.example.skillsync.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    // Custom method to find all options for a given question
    List<Option> findByQuestionId(Long questionId);
}
