package com.example.convergencesoftwarerecruitingbe.domain.locker.service;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.ApplicationCreateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.ApplicationResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Locker;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.event.ApplicationCreatedEvent;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.ApplicationRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.LockerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final LockerRepository lockerRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public ApplicationResponse createApplication(ApplicationCreateRequest request) {
        List<ApplicationStatus> statuses = List.of(ApplicationStatus.SUBMITTED, ApplicationStatus.APPROVED);

        boolean hasDuplicateStudentId = applicationRepository.existsByStudentIdHashAndStatusIn(
                hashStudentId(request.getStudentId()),
                statuses
        );
        boolean hasDuplicatePhone = applicationRepository.existsByPhoneAndStatusIn(request.getPhone(), statuses);
        boolean hasDuplicateEmail = applicationRepository.existsByEmailAndStatusIn(request.getEmail(), statuses);

        if (hasDuplicateStudentId || hasDuplicatePhone || hasDuplicateEmail) {
            throw new IllegalStateException("이미 신청 내역이 있습니다");
        }

        Locker locker = lockerRepository.findById(request.getLockerId())
                .orElseThrow(() -> new EntityNotFoundException("Locker not found: " + request.getLockerId()));

        locker.reserve();

        Application application = Application.create(
                locker,
                request.getApplicantName(),
                request.getStudentId(),
                request.getPhone(),
                request.getEmail(),
                request.getGrade(),
                request.getMajor()
        );

        Application saved = applicationRepository.save(application);

        applicationEventPublisher.publishEvent(new ApplicationCreatedEvent(
                saved.getId(),
                saved.getEmail(),
                saved.getApplicantName(),
                locker.getNumber()
        ));

        return ApplicationResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getAllApplications(ApplicationStatus status, Pageable pageable) {
        return applicationRepository.findAllByStatusFilter(status, pageable)
                .map(ApplicationResponse::from);
    }

    @Transactional(readOnly = true)
    public long getPendingCount() {
        return applicationRepository.countByStatus(ApplicationStatus.SUBMITTED);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicationsByLocker(Long lockerId) {
        return applicationRepository.findAllByLockerIdOrderByCreatedAtDesc(lockerId)
                .stream()
                .map(ApplicationResponse::from)
                .toList();
    }

    private static String hashStudentId(String studentId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(studentId.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte hashByte : hashBytes) {
                sb.append(String.format("%02x", hashByte));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 해시 생성에 실패했습니다", e);
        }
    }
}
