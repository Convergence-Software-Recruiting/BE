package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin;

import com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin.docs.LockerRentalAdminControllerDocs;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.RentalResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ReturnReason;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.LockerService;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.RentalService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/locker/rentals")
@RequiredArgsConstructor
public class LockerRentalAdminController implements LockerRentalAdminControllerDocs {

    private final RentalService rentalService;
    private final LockerService lockerService;

    @Override
    @GetMapping("/active")
    public ResponseEntity<List<RentalResponse>> getActiveRentals() {
        return ResponseEntity.ok(rentalService.getActiveRentals());
    }

    @Override
    @GetMapping("/expiring")
    public ResponseEntity<List<RentalResponse>> getExpiringRentals() {
        return ResponseEntity.ok(rentalService.getExpiringRentals(7));
    }

    @Override
    @PostMapping("/{id}/return")
    public ResponseEntity<RentalResponse> returnRental(
            @PathVariable Long id,
            @RequestParam ReturnReason reason
    ) {
        RentalResponse response = rentalService.returnRental(id, reason);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/semester/close")
    public ResponseEntity<Map<String, Integer>> closeSemester() {
        int returnedCount = rentalService.autoReturnAll();
        lockerService.closeAllLockers();
        return ResponseEntity.ok(Map.of("returnedCount", returnedCount));
    }
}
