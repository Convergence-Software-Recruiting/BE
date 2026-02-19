package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin;

import com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin.docs.LockerApplicationAdminControllerDocs;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.ApplicationDecisionRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.ApplicationResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.RentalResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.ApplicationService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/locker/applications")
@RequiredArgsConstructor
public class LockerApplicationAdminController implements LockerApplicationAdminControllerDocs {

    private final ApplicationService applicationService;
    private final RentalService rentalService;

    @Override
    @GetMapping
    public ResponseEntity<Page<ApplicationResponse>> getApplications(
            @RequestParam(required = false) ApplicationStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ApplicationResponse> applications = applicationService.getAllApplications(status, pageable);
        return ResponseEntity.ok(applications);
    }

    @Override
    @GetMapping("/pending-count")
    public ResponseEntity<Long> getPendingCount() {
        return ResponseEntity.ok(applicationService.getPendingCount());
    }

    @Override
    @PostMapping("/{id}/approve")
    public ResponseEntity<RentalResponse> approveApplication(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String adminId = authentication.getName();
        RentalResponse response = rentalService.approveApplication(id, adminId);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectApplication(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationDecisionRequest request,
            Authentication authentication
    ) {
        String adminId = authentication.getName();
        rentalService.rejectApplication(id, request, adminId);
        return ResponseEntity.ok().build();
    }
}
