package com.example.convergencesoftwarerecruitingbe.domain.application.repository;

import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByFormVersionIdAndStudentNo(Long formVersionId, String studentNo);

    Page<Application> findByStatus(ApplicationStatus status, Pageable pageable);
}