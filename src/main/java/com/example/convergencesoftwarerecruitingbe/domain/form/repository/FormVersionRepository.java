package com.example.convergencesoftwarerecruitingbe.domain.form.repository;

import com.example.convergencesoftwarerecruitingbe.domain.form.entity.FormVersion;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.FormVersionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormVersionRepository extends JpaRepository<FormVersion, Long> {
    Optional<FormVersion> findTopByFormIdAndStatusOrderByVersionDesc(Long formId, FormVersionStatus status);
    Optional<FormVersion> findTopByStatusOrderByPublishedAtDesc(FormVersionStatus status);
}
