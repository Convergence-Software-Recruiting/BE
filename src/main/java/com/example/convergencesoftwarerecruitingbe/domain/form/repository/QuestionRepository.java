package com.example.convergencesoftwarerecruitingbe.domain.form.repository;

import com.example.convergencesoftwarerecruitingbe.domain.form.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByFormVersionIdOrderByOrderNoAsc(Long formVersionId);
}
