package com.example.convergencesoftwarerecruitingbe.domain.locker.repository;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Rental;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ReturnReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findAllByReturnedAtIsNull();

    List<Rental> findAllByLockerIdOrderByApprovedAtDesc(Long lockerId);

    @Query("SELECT r FROM Rental r WHERE r.returnedAt IS NULL " +
            "AND r.rentalEndDate BETWEEN :start AND :end")
    List<Rental> findExpiringRentals(@Param("start") LocalDate start, @Param("end") LocalDate end);

    Optional<Rental> findByApplicationId(Long applicationId);

    long countByReturnedAtIsNull();

    @Modifying
    @Query("UPDATE Rental r SET r.returnedAt = :now, r.returnReason = :reason " +
            "WHERE r.returnedAt IS NULL")
    int returnAllActiveRentals(@Param("now") LocalDateTime now, @Param("reason") ReturnReason reason);
}
