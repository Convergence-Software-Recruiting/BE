package com.example.convergencesoftwarerecruitingbe.domain.admin.application.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response.AdminApplicationDetailResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response.AdminApplicationListResponse;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.domain.application.repository.ApplicationAnswerRepository;
import com.example.convergencesoftwarerecruitingbe.domain.application.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationAnswerRepository applicationAnswerRepository;

    @Transactional(readOnly = true)
    public Page<AdminApplicationListResponse> listApplications(ApplicationStatus status, Pageable pageable) {
        Page<Application> page = (status == null)
                ? applicationRepository.findAll(pageable)
                : applicationRepository.findByStatus(status, pageable);

        return page.map(AdminApplicationListResponse::from);
    }

    @Transactional(readOnly = true)
    public AdminApplicationDetailResponse getApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));

        var answers = applicationAnswerRepository.findByApplicationId(id);
        return AdminApplicationDetailResponse.of(application, answers);
    }

    @Transactional
    public void updateStatus(Long id, ApplicationStatus status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
        application.changeStatus(status);
        applicationRepository.save(application);
    }

    @Transactional
    public void updateMemo(Long id, String memo) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
        application.changeAdminMemo(memo);
        applicationRepository.save(application);
    }
}
