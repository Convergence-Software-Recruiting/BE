package com.example.convergencesoftwarerecruitingbe.domain.locker.repository;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Rental;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ReturnReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long>, JpaSpecificationExecutor<Rental> {

    List<Rental> findAllByReturnedAtIsNull();

    @Query("SELECT r FROM Rental r JOIN FETCH r.locker WHERE r.returnedAt IS NULL")
    List<Rental> findAllActiveWithLocker();

    List<Rental> findAllByLockerIdOrderByApprovedAtDesc(Long lockerId);

    @Query("SELECT r FROM Rental r WHERE r.returnedAt IS NULL " +
            "AND r.rentalEndDate BETWEEN :start AND :end")
    List<Rental> findExpiringRentals(@Param("start") LocalDate start, @Param("end") LocalDate end);

    Optional<Rental> findByApplicationId(Long applicationId);

    Optional<Rental> findByLockerIdAndReturnedAtIsNull(Long lockerId);

    long countByReturnedAtIsNull();

    boolean existsByRenterPhoneAndReturnedAtIsNull(String renterPhone);

    boolean existsByRenterEmailAndReturnedAtIsNull(String renterEmail);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rental r " +
            "JOIN r.application a WHERE a.studentIdHash = :hash AND r.returnedAt IS NULL")
    boolean existsActiveRentalByStudentIdHash(@Param("hash") String studentIdHash);

    @Modifying
    @Query("UPDATE Rental r SET r.returnedAt = :now, r.returnReason = :reason " +
            "WHERE r.returnedAt IS NULL")
    int returnAllActiveRentals(@Param("now") LocalDateTime now, @Param("reason") ReturnReason reason);
}
