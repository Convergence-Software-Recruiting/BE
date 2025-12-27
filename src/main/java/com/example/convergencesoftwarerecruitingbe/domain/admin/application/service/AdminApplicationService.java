package com.example.convergencesoftwarerecruitingbe.domain.admin.application.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response.AdminApplicationDetailResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response.AdminApplicationListItemResponse;
import com.example.convergencesoftwarerecruitingbe.domain.application.repository.ApplicationRepository;
import com.example.convergencesoftwarerecruitingbe.domain.application.repository.ApplicationAnswerRepository;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.ApplicationAnswer;
import com.example.convergencesoftwarerecruitingbe.global.common.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationAnswerRepository applicationAnswerRepository;

    @Transactional(readOnly = true)
    public List<AdminApplicationListItemResponse> list() {
        return applicationRepository.findAllByOrderBySubmittedAtDesc()
                .stream()
                .map(AdminApplicationListItemResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdminApplicationDetailResponse getDetail(Long applicationId) {
        Application application = findApplicationOrThrow(applicationId);
        List<ApplicationAnswer> answers = applicationAnswerRepository.findAllByApplicationIdWithQuestion(applicationId);
        return AdminApplicationDetailResponse.of(application, answers);
    }

    @Transactional
    public void updateStatus(Long applicationId, ApplicationStatus status) {
        Application application = findApplicationOrThrow(applicationId);
        application.updateStatus(status);
    }

    @Transactional
    public void updateMemo(Long applicationId, String memo) {
        Application application = findApplicationOrThrow(applicationId);
        application.updateAdminMemo(memo);
    }

    private Application findApplicationOrThrow(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
    }
}
