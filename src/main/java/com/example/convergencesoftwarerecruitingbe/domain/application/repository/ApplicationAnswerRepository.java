package com.example.convergencesoftwarerecruitingbe.domain.application.repository;

import com.example.convergencesoftwarerecruitingbe.domain.application.entity.ApplicationAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationAnswerRepository extends JpaRepository<ApplicationAnswer, Long> {

    @Query("""
            select aa
            from ApplicationAnswer aa
            join fetch aa.question q
            where aa.application.id = :applicationId
            """)
    List<ApplicationAnswer> findAllByApplicationIdWithQuestion(@Param("applicationId") Long applicationId);
}
