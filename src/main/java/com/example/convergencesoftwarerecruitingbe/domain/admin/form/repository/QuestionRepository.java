package com.example.convergencesoftwarerecruitingbe.domain.admin.form.repository;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByFormIdOrderByOrderNoAsc(Long formId);

    boolean existsByFormIdAndOrderNo(Long formId, Integer orderNo);

    boolean existsByFormIdAndOrderNoAndIdNot(Long formId, Integer orderNo, Long id);

    @Query("select coalesce(max(q.orderNo), 0) from Question q where q.form.id = :formId")
    Integer findMaxOrderNoByFormId(@Param("formId") Long formId);
}
