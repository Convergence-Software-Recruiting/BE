package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin;

import com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin.docs.LockerAdminControllerDocs;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.LockerResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.LockerStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.LockerService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/locker")
@RequiredArgsConstructor
public class LockerAdminController implements LockerAdminControllerDocs {

    private final LockerService lockerService;

    @Override
    @GetMapping
    public ResponseEntity<List<LockerResponse>> getAllLockers() {
        return ResponseEntity.ok(lockerService.getAllLockers());
    }

    @Override
    @PatchMapping("/{id}/status")
    public ResponseEntity<LockerResponse> updateLockerStatus(
            @PathVariable Long id,
            @RequestParam LockerStatus status,
            @RequestParam(required = false) String adminNote
    ) {
        LockerResponse response = lockerService.updateLockerStatus(id, status, adminNote);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getStatistics() {
        Map<LockerStatus, Long> stats = lockerService.getLockerStatusStatistics();
        Map<String, Long> result = stats.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().name(),
                        Map.Entry::getValue
                ));
        return ResponseEntity.ok(result);
    }
}
