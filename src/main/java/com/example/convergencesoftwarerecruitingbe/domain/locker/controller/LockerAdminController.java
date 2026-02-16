package com.example.convergencesoftwarerecruitingbe.domain.locker.controller;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.ApplicationDecisionRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.ApplicationResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.LockerResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.RentalResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.LockerStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ReturnReason;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.ApplicationService;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.LockerService;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/locker/admin")
public class LockerAdminController {

    private final ApplicationService applicationService;
    private final RentalService rentalService;
    private final LockerService lockerService;

    @GetMapping("/applications")
    public ResponseEntity<Page<ApplicationResponse>> getApplications(
            @RequestParam(required = false) ApplicationStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(applicationService.getAllApplications(status, pageable));
    }

    @GetMapping("/applications/pending-count")
    public ResponseEntity<Long> getPendingCount() {
        return ResponseEntity.ok(applicationService.getPendingCount());
    }

    @PostMapping("/applications/{id}/approve")
    public ResponseEntity<RentalResponse> approveApplication(@PathVariable Long id, Authentication authentication) {
        String adminId = authentication.getName();
        return ResponseEntity.ok(rentalService.approveApplication(id, adminId));
    }

    @PostMapping("/applications/{id}/reject")
    public ResponseEntity<Void> rejectApplication(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationDecisionRequest request,
            Authentication authentication
    ) {
        String adminId = authentication.getName();
        rentalService.rejectApplication(id, request, adminId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rentals/active")
    public ResponseEntity<List<RentalResponse>> getActiveRentals() {
        return ResponseEntity.ok(rentalService.getActiveRentals());
    }

    @GetMapping("/rentals/expiring")
    public ResponseEntity<List<RentalResponse>> getExpiringRentals() {
        return ResponseEntity.ok(rentalService.getExpiringRentals(7));
    }

    @PostMapping("/rentals/{id}/return")
    public ResponseEntity<RentalResponse> returnRental(@PathVariable Long id, @RequestParam ReturnReason reason) {
        return ResponseEntity.ok(rentalService.returnRental(id, reason));
    }

    @PostMapping("/semester/close")
    public ResponseEntity<Map<String, Integer>> closeSemester() {
        int count = rentalService.autoReturnAll();
        lockerService.closeAllLockers();
        return ResponseEntity.ok(Map.of("returnedCount", count));
    }

    @PatchMapping("/lockers/{id}/status")
    public ResponseEntity<LockerResponse> updateLockerStatus(
            @PathVariable Long id,
            @RequestParam LockerStatus status,
            @RequestParam(required = false) String adminNote
    ) {
        return ResponseEntity.ok(lockerService.updateLockerStatus(id, status, adminNote));
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<LockerStatus, Long>> getStatistics() {
        return ResponseEntity.ok(lockerService.getLockerStatusStatistics());
    }
}
