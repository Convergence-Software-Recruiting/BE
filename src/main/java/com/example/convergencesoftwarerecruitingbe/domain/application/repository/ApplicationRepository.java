package com.example.convergencesoftwarerecruitingbe.domain.application.repository;

import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Application;
import com.example.convergencesoftwarerecruitingbe.global.common.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByFormIdAndStudentNo(Long formId, String studentNo);

    boolean existsByFormIdAndResultCode(Long formId, String resultCode);

    Page<Application> findByStatus(ApplicationStatus status, Pageable pageable);

    List<Application> findAllByOrderBySubmittedAtDesc();

    java.util.Optional<Application> findByFormIdAndResultCode(Long formId, String resultCode);
}
