package com.example.convergencesoftwarerecruitingbe.domain.locker.service;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.ApplicationDecisionRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.RentalResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Rental;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.SystemConfig;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ReturnReason;
import com.example.convergencesoftwarerecruitingbe.domain.locker.event.ApplicationApprovedEvent;
import com.example.convergencesoftwarerecruitingbe.domain.locker.event.ApplicationRejectedEvent;
import com.example.convergencesoftwarerecruitingbe.domain.locker.event.SemesterClosedEvent;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.ApplicationRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.RentalRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.SystemConfigRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final ApplicationRepository applicationRepository;
    private final SystemConfigRepository systemConfigRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public RentalResponse approveApplication(Long applicationId, String adminId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found: " + applicationId));

        SystemConfig config = systemConfigRepository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new EntityNotFoundException("SystemConfig not found"));

        application.approve(adminId);
        application.getLocker().markInUse();

        Rental rental = Rental.fromApplication(application, config.getRentalEndDate());
        Rental saved = rentalRepository.save(rental);

        applicationEventPublisher.publishEvent(new ApplicationApprovedEvent(
                application.getId(),
                application.getEmail(),
                application.getApplicantName(),
                application.getLocker().getNumber()
        ));

        return RentalResponse.from(saved);
    }

    @Transactional
    public void rejectApplication(Long applicationId, ApplicationDecisionRequest request, String adminId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found: " + applicationId));

        application.reject(request.getDecisionReason(), adminId);
        application.getLocker().makeEmpty();

        applicationEventPublisher.publishEvent(new ApplicationRejectedEvent(
                application.getId(),
                application.getEmail(),
                application.getApplicantName(),
                request.getDecisionReason()
        ));
    }

    @Transactional
    public RentalResponse returnRental(Long rentalId, ReturnReason reason) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found: " + rentalId));

        rental.returnLocker(reason);
        rental.getLocker().makeEmpty();

        return RentalResponse.from(rental);
    }

    @Transactional(readOnly = true)
    public List<RentalResponse> getActiveRentals() {
        return rentalRepository.findAllByReturnedAtIsNull()
                .stream()
                .map(RentalResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RentalResponse> getExpiringRentals(int days) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(days);

        return rentalRepository.findExpiringRentals(today, endDate)
                .stream()
                .map(RentalResponse::from)
                .toList();
    }

    @Transactional
    public int autoReturnAll() {
        LocalDateTime now = LocalDateTime.now();
        int count = rentalRepository.returnAllActiveRentals(now, ReturnReason.AUTO_END);

        log.info("자동 반납 처리: {} 건", count);
        applicationEventPublisher.publishEvent(new SemesterClosedEvent(count, now));

        return count;
    }
}
