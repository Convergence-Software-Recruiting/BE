package com.example.convergencesoftwarerecruitingbe.domain.locker.repository;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Application a " +
            "WHERE a.studentIdHash = :hash AND a.status IN :statuses")
    boolean existsByStudentIdHashAndStatusIn(
            @Param("hash") String studentIdHash,
            @Param("statuses") List<ApplicationStatus> statuses
    );

    boolean existsByPhoneAndStatusIn(String phone, List<ApplicationStatus> statuses);

    boolean existsByEmailAndStatusIn(String email, List<ApplicationStatus> statuses);

    @Query("SELECT a FROM Application a WHERE :status IS NULL OR a.status = :status ORDER BY a.createdAt DESC")
    Page<Application> findAllByStatusFilter(@Param("status") ApplicationStatus status, Pageable pageable);

    long countByStatus(ApplicationStatus status);

    List<Application> findAllByLockerIdOrderByCreatedAtDesc(Long lockerId);
}
